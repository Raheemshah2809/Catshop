package clients.customer;

import catalogue.Basket;
import catalogue.BetterBasket;
import catalogue.Product;
import debug.DEBUG;
import javafx.scene.image.Image;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockException;
import middle.StockReadWriter;
import middle.StockReader;

import javax.swing.*;

import java.util.Iterator;
import java.util.Observable;

/**
 * Implements the Model of the customer client
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */
public class CustomerModel extends Observable
{
  private enum State { process, checked }
  private State       theState   = State.process;   // Current state
  private Product     theProduct = null;          // Current product
  private Basket      theBasket  = null;          // Bought items

  private String      pn = "";                    // Product being processed

  private StockReadWriter     theStock     = null;
  private StockReadWriter     theStock2     = null;
  private Image           thePic       = null;
  /*
   * Construct the model of the Customer
   * @param mf The factory to create the connection objects
   */
  public CustomerModel(MiddleFactory mf)
  {
    try                                          //
    {  
      theStock = mf.makeStockReadWriter();           // Database access
      theStock2 = mf.makeStockReadWriter2(); // store 2
    } catch ( Exception e )
    {
      DEBUG.error("CustomerModel.constructor\n" +
                  "Database not created?\n%s\n", e.getMessage() );
    }
    theBasket = makeBasket();                    // Initial Basket
    theState = State.process;
  }
  
  /**
   * return the Basket of products
   * @return the basket of products
   */
  public Basket getBasket()
  {
    return theBasket;
  }

  /**
   * Check if the product is in Stock
   * @param productNum The product number
   */
  public void doCheck(String productNum )
  {
    String theAction = "";
    pn  = productNum.trim();                    // Product no.
    int    amount  = 1;                         //  & quantity
    try
    {
      if ( theStock.exists( pn ) )              // Stock Exists?
      {                                         // T
        Product pr = theStock.getDetails( pn ); //  Product
        if ( pr.getQuantity() >= amount )       //  In stock?
        { 
          theAction =                           //   Display 
            String.format( "Stock 1 : %s : %7.2f (%2d) ", //
              pr.getDescription(),              //    description
              pr.getPrice(),                    //    price
              pr.getQuantity() );               //    quantity
          pr.setQuantity( amount );             //   Require 1
          // TODO java RMI not support image transfer
          //thePic = theStock.getImage( pn );     //    product
          theState = State.checked;
        } else {                                //  F
          theAction =                           //   Inform
            pr.getDescription() +               //    product not
            " not in Stock 1" ;                   //    in stock
        }
      } else {                                  // F
        theAction =                             //  Inform Unknown
          "Stock 1 : Unknown product number " + pn;       //  product number
      }
      if ( theStock2 != null && theStock2.exists( pn ) )              // Stock Exists?
      {                                         // T
        Product pr = theStock2.getDetails( pn ); //  Product
        if ( pr.getQuantity() >= amount )       //  In stock?
        { 
          theAction +=                           //   Display 
            String.format( " Stock 2 : %s : %7.2f (%2d) ", //
              pr.getDescription(),              //    description
              pr.getPrice(),                    //    price
              pr.getQuantity() );               //    quantity
          pr.setQuantity( amount );             //   Require 1
          // TODO java RMI not support image transfer
          //thePic = theStock.getImage( pn );     //    product
        } else {                                //  F
          theAction +=                           
            (" " + pr.getDescription() +               
            " not in Stock 2" );                  
        }
      } else {                                  // F
        theAction += (" Stock 2 : Unknown product number " + pn);       //  product number
      }
    } catch( StockException e )
    {
      DEBUG.error("CustomerClient.doCheck()\n%s",
      e.getMessage() );
    }
    setChanged(); notifyObservers(theAction);
  }

  /**
   * reserve product
   * @param productNum The product number
   */
  public void doReserve(String productNum )
  {
    String theAction = "";
	if(theState != State.checked) {
		theAction = "Please Confirm Product to Reserve!";
	}
	else {
		pn  = productNum.trim();                    // Product no.
	    int amount  = 1;                         //  & quantity
	    try
	    {
	        Product pr = theStock.getDetails( pn ); //  Product
	        pr.setQuantity(amount);
	        boolean stockBought =                   // Buy
	                theStock.buyStock(                    //  however
	                		pr.getProductNum(),         //  may fail
	                		pr.getQuantity() );         // reserve one product at a time
	        if ( stockBought )       //  success to buy
	        { 
	          theAction =                           //   Display 
	            String.format( "Product #%s is reserved for you!", pn);
	          pr.setQuantity( amount );             //   Require 1
	          theBasket.add(pr);
	        } else {                                //  F
	          theAction =                           //   Inform
	            pr.getDescription() +               //    product not
	            " not in stock" ;                   //    in stock
	        }
	        theState = State.process;
	    } catch( StockException e )
	    {
	      DEBUG.error("CustomerClient.doReserve()\n%s",
	      e.getMessage() );
	    }
	 }

    setChanged(); notifyObservers(theAction);
  }

  /**
   * submit reservation
   */
  public void doSubmitReservation()
  {
    String theAction = "";
    String customerName = "Customer";
    if(theBasket.isEmpty()) {
		theAction = "Please add Product to Reserve!";
	}
	else {
	    try
	    {
	    	int reservationNum = theStock.addReservation(customerName);
	        for(Product pr:theBasket) {
	        	theStock.addReservedProduct(reservationNum, pr.getProductNum(), pr.getQuantity());
	        }
	        theAction = String.format("Your reservation number is %d.", reservationNum);                          //   Inform
	        theState = State.process;
	        theBasket.clear();
	    } catch( StockException e )
	    {
	      DEBUG.error("CustomerClient.doSubmitReservation()\n%s",
	      e.getMessage() );
	    }
	 }
     setChanged(); notifyObservers(theAction);
  }

  /**
   * Remove last reserved product
   */
  public void doRemove()
  {
	  String theAction = "";
	  if(theBasket.isEmpty()) {
		  theAction = "No Product to remove!";
	  }
	  else {
		  try {
			  Product pr = theBasket.remove();
			  theStock.addStock(pr.getProductNum(), pr.getQuantity());
			  theAction = String.format("Product #%s has been removed.", pr.getProductNum());
		  } catch (StockException e) {
		      DEBUG.error("CustomerClient.doRemove()\n%s",
		    	      e.getMessage() );
		  }
	  }
	  theState = State.process;
	  setChanged(); notifyObservers(theAction);
  }
  
  /**
   * Clear the products from the basket
   */
  public void doClear()
  {
    String theAction = "";
    try {
    	if(theBasket != null && theBasket.isEmpty() != true) {
	        // restore reserved products
    		for (Iterator<Product> iterator = theBasket.iterator(); iterator.hasNext();) {
    		    Product pr = iterator.next();
    		    theStock.addStock(pr.getProductNum(), pr.getQuantity());
    		    iterator.remove();
    		}
	        theBasket.clear();                        // Clear s. list
    	}
        theAction = "Enter Product Number";       // Set display
        thePic = null;                            // No picture
    }  catch( StockException e )
    {
		DEBUG.error("CustomerClient.doClear()\n%s",
	 	e.getMessage() );
	}
    theState = State.process;
    setChanged(); notifyObservers(theAction);
  }
  
  /**
   * Return a picture of the product
   * @return An instance of an ImageIcon
   */ 
  public Image getPicture()
  {
    return thePic;
  }
  
  /**
   * ask for update of view callled at start
   */
  private void askForUpdate()
  {
    setChanged(); notifyObservers("START only"); // Notify
  }

  /**
   * Make a new Basket
   * @return an instance of a new Basket
   */
  protected Basket makeBasket()
  {
    return new BetterBasket();
  }

}

