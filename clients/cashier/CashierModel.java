package clients.cashier;

import catalogue.Basket;
import catalogue.Product;
import debug.DEBUG;
import middle.*;

import java.util.Observable;

/**
 * Implements the Model of the cashier client
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */
public class CashierModel extends Observable
{
  private enum State { process, checked }

  private State       theState   = State.process;   // Current state
  private Product     theProduct = null;            // Current product
  private Basket      theBasket  = null;            // Bought items
  private String      pn = "";                      // Product being processed

  private StockReadWriter theStock     = null;
  private OrderProcessing theOrder     = null;
  private StateOf         worker   = new StateOf();
  private String          theAction  = "";
 
  /**
   * Construct the model of the Cashier
   * @param mf The factory to create the connection objects
   */

  public CashierModel(MiddleFactory mf)
  {
    try                                           // 
    {      
      theStock = mf.makeStockReadWriter();        // Database access
      theOrder = mf.makeOrderProcessing();        // Process order
    } catch ( Exception e )
    {
      DEBUG.error("CashierModel.constructor\n%s", e.getMessage() );
    }
    theState   = State.process;                  // Current state
    // Start a background check to see when a new order can be picked
    new Thread( () -> checkForNeedAttention() ).start();
  }
 
  /**
   * Semaphore used to get need attention order
   */
  class StateOf
  {
    private boolean held = false;
    
    /**
     * Claim exclusive access
     * @return true if claimed else false
     */
    public synchronized boolean claim()   // Semaphore
    {
      return held ? false : (held = true);
    }
    
    /**
     * Free the lock
     */
    public synchronized void free()     //
    {
      assert held;
      held = false;
    }

  }
  
  /**
   * Get the Basket of products
   * @return basket
   */
  public Basket getBasket()
  {
    return theBasket;
  }

  /**
   * Method run in a separate thread to check if there
   * is a need attention order
   */
  private void checkForNeedAttention()
  {
    while ( true )
    {
      try
      {
        //boolean isFree = worker.claim();     // Are we free
        if ( /*isFree &&*/ theBasket == null && theState == State.process)                        // T
        {                                    //
        	theBasket = theOrder.getOrderToNeedAttention(); // need attention
        	if(theBasket != null)
        		theAction = "Need Attention Order #" + theBasket.getOrderNum();  
        	else
        		theAction = "Next customer";            // New Customer
        	
        	// get expired reservation
        	int rNum = theStock.getExpiredReservationNum();
        	Basket basket = theStock.getReservation(rNum);
        	theStock.removeReservation(rNum); // remove resrvation
        	for(Product pr : basket) {
        		// restore products
        		theStock.addStock(pr.getProductNum(), pr.getQuantity());
        	}
        	setChanged(); notifyObservers(theAction);
        }                                    // 
        Thread.sleep(2000);                  // idle
        //worker.free();
      } catch ( Exception e )
      {
        DEBUG.error("%s\n%s",                // Eek!
           "BackGroundCheck.run()\n%s",
           e.getMessage() );
      }
    }
  }

  /**
   * Check if the product is in Stock
   * @param productNum The product number
   */
  public void doCheck(String productNum )
  {
    theState  = State.process;                  // State process
    pn  = productNum.trim();                    // Product no.
    int    amount  = 1;                         //  & quantity
    try
    {
      if ( theStock.exists( pn ) )              // Stock Exists?
      {                                         // T
        Product pr = theStock.getDetails(pn);   //  Get details
        if ( pr.getQuantity() >= amount )       //  In stock?
        {                                       //  T
        	String pairNo = theStock.getRecommendedProduct(pn);
        	theAction =                           //   Display 
	            String.format( "%s : %7.2f (%2d)", //
	              pr.getDescription(),              //    description
	              pr.getPrice(),                    //    price
	              pr.getQuantity() );               //    quantity  
        	if(pairNo.isEmpty() == false)
        		theAction += String.format( "  ***Customers who bought %s also bought %s", pn, pairNo);
        	theProduct = pr;                      //   Remember prod.
        	theProduct.setQuantity( amount );     //    & quantity
        	theState = State.checked;             //   OK await BUY 
        } else {                                //  F
          theAction =                           //   Not in Stock
            pr.getDescription() +" not in stock";
        }
      } else {                                  // F Stock exists
        theAction =                             //  Unknown
          "Unknown product number " + pn;       //  product no.
      }
    } catch( StockException e )
    {
      DEBUG.error( "%s\n%s", 
            "CashierModel.doCheck", e.getMessage() );
      theAction = e.getMessage();
    }
    setChanged(); notifyObservers(theAction);
  }

  /**
   * Buy the product
   */
  public void doBuy()
  {
    int    amount  = 1;                         //  & quantity
    try
    {
      if ( theState != State.checked )          // Not checked
      {                                         //  with customer
        theAction = "Check if OK with customer first";
      } else {
        boolean stockBought =                   // Buy
                theStock.buyStock(                    //  however
                        theProduct.getProductNum(),         //  may fail
                        theProduct.getQuantity() );         //
        if ( stockBought )                      // Stock bought
        {                                       // T
          makeBasketIfReq();                    //  new Basket ?
          theBasket.add( theProduct );          //  Add to bought
          theAction = "Purchased " +            //    details
                  theProduct.getDescription();  //
          
          for(Product pr : theBasket) {	// update pairing level
        	  if(theProduct.equals(pr))
        		  continue;
        	  theStock.updateRecommendLevel(theProduct.getProductNum(), pr.getProductNum());
          }
        } else {                                // F
          theAction = "!!! Not in stock";       //  Now no stock
        }
      }
    } catch( StockException e )
    {
      DEBUG.error( "%s\n%s",
              "CashierModel.doBuy", e.getMessage() );
      theAction = e.getMessage();
    }
    theState = State.process;                   // All Done
    setChanged(); notifyObservers(theAction);
  }

  /**
   * Remove the product
   */
  public void doRemove()
  {
    try
    {
      if ( theBasket.isEmpty() )          // check basket
      {                                         //  with customer
        theAction = "No product in basket";
      } else {
        Product pr =  theBasket.remove();                 // remove from basket
        theStock.addStock( pr.getProductNum(), 1); // add stock
        theAction = "Removed " +            //    details
                pr.getDescription();  //
      }
    } catch( StockException e )
    {
      DEBUG.error( "%s\n%s",
              "CashierModel.doBuy", e.getMessage() );
      theAction = e.getMessage();
    }
    theState = State.process;                   // All Done
    setChanged(); notifyObservers(theAction);
  }

  /**
   * Customer pays for the contents of the basket
   */
  public void doBought()
  {
    try
    {
      if ( theBasket != null &&
           theBasket.size() >= 1 )            // items > 1
      {                                       // T
        theOrder.newOrder( theBasket );       //  Process order
        theBasket = null;                     //  reset
      }
      theBasket = null;
      theAction = "Next customer";            // New Customer
      theState = State.process;               // All Done
    } catch( OrderException e )
    {
      DEBUG.error( "%s\n%s", 
            "CashierModel.doCancel", e.getMessage() );
      theAction = e.getMessage();
      theBasket = null;
    }
    setChanged(); notifyObservers(theAction); // Notify
  }
  
  /**
   * Load reserved products
   */
  public void doLoadReservation(int rNum)
  {
	  if(theBasket != null) {
		  theAction = "Please try after buying completely!";
	  }
	  else {
		  try {
			  if(theStock.isInReservations(rNum) != true) {
				  theAction = String.format("Reservation #%d is not exist.", rNum);
			  }
			  else {
				  theBasket = theStock.getReservation(rNum);
				  if(theBasket == null) {
					  theAction = String.format("Reservation #%d is invalid.", rNum);
				  }
				  else {
					  theBasket.setUniqueOrderNum();
				  }
				  theStock.removeReservation(rNum);
			  }
		  } catch (StockException e) {
		      DEBUG.error( "%s\n%s",
		              "CashierModel.doLoadReservation", e.getMessage() );
		      theAction = e.getMessage();
		  }	  
	  }
	  setChanged(); notifyObservers(theAction); // Notify
 }
  
  /**
   * reserve product in basket
   */
  public void doReserve()
  {
	  if(theBasket == null || theBasket.isEmpty()) {
		  theAction = "There is no product to reserve.";
	  }
	  else {
		  try {
			  int rNum = theStock.addReservation("Customer");
			  for(Product pr : theBasket) {
				  theStock.addReservedProduct(rNum, pr.getProductNum(), pr.getQuantity());
			  }
			  theBasket = null;
			  theAction = String.format("Your reservation number is %d.", rNum);
		  } catch (StockException e) {
		      DEBUG.error( "%s\n%s",
		              "CashierModel.doReserve", e.getMessage() );
		      theAction = e.getMessage();
		  }
	  }
	  setChanged(); notifyObservers(theAction); // Notify
}

  /**
   * Set Discount rate
   */
  public void doDiscount(double discountRate)
  {
	  if(theBasket != null) {
		  theBasket.setDiscountRate(discountRate);
		  theAction = "Discount Rate : " + theBasket.getDiscountRate();
	  }
	  else {
		  theAction = "Cannot discount on empty basket!";
	  }
	  
	  setChanged(); notifyObservers(theAction); // Notify
  }
  
  /**
   * ask for update of view called at start of day
   * or after system reset
   */
  public void askForUpdate()
  {
    setChanged(); notifyObservers("Welcome");
  }
  
  /**
   * make a Basket when required
   */
  private void makeBasketIfReq()
  {
    if ( theBasket == null ) {
        theBasket = makeBasket();                //  basket list
		theBasket.setUniqueOrderNum();
    }
  }

  /**
   * return an instance of a new Basket
   * @return an instance of a new Basket
   */
  protected Basket makeBasket()
  {
    return new Basket();
  }
}
  
