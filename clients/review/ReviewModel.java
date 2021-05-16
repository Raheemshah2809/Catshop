package clients.review;

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



import java.util.Observable;

public class ReviewModel extends Observable
{
  public enum State { process, checked }
  private State       theState   = State.process;   // Current state

  private String      pn = "";                    // Product being processed
  private String theDescription = "";

  private StockReadWriter     theStock     = null;
  private Image           thePic       = null;
  /*
   * Construct the model of the Customer
   * @param mf The factory to create the connection objects
   */
  public ReviewModel(MiddleFactory mf)
  {
    try                                          //
    {  
      theStock = mf.makeStockReadWriter();           // Database access
    } catch ( Exception e )
    {
      DEBUG.error("CustomerModel.constructor\n" +
                  "Database not created?\n%s\n", e.getMessage() );
    }
    theState = State.process;
  }

  /**
   * Check if the product is in Stock
   * @param productNum The product number
   */
  public void doCheck(String productNum )
  {
    String theAction = "";
    pn  = productNum.trim();                    // Product no.
    try
    {
      if ( theStock.exists( pn ) )              // Stock Exists?
      {                                         // T
        Product pr = theStock.getDetails( pn ); //  Product
          theAction =                           //   Display 
            String.format( "%s : %7.2f (%2d) ", //
            		pr.getDescription(),              //    description
            		pr.getPrice(),                    //    price
            		pr.getQuantity() );               //    quantity
          theDescription = theAction;
          thePic = theStock.getImage( pn );     //    product
          theState = State.checked;
      } else {                                  // F
        theAction =                             //  Inform Unknown
          "Unknown product number " + pn;       //  product number
      }
    } catch( StockException e )
    {
      DEBUG.error("ReviewClient.doCheck()\n%s",
      e.getMessage() );
    }
    setChanged(); notifyObservers(theAction);
  }

  /**
   * reserve product
   * @param productNum The product number
   */
  public void doReview(String review, double rating )
  {
    String theAction = "";
	if(theState != State.checked) {
		theAction = "Please Confirm Product to leave reivew!";
	}
	else {
	    try
	    {
	    	String sReview = review.trim();
	    	sReview = sReview.replace("'", "''");
            theStock.addReviewAndRating(pn, sReview, rating);
	        theState = State.process;
	        theAction = "Your review has been added to the product.";
	    } catch( StockException e )
	    {
	      DEBUG.error("CustomerClient.doReivew()\n%s",
	      e.getMessage() );
	    }
	 }

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
   * Return description of the product
   * @return description string
   */ 
  public String getDescription()
  {
	  return theDescription;
  }
  
  /**
   * Return reviews
   */
  public String getReivew()
  {
	String review = "";
    try
    {
    	review = theStock.getReview(pn);
    } catch( StockException e )
    {
      DEBUG.error("ReviewClient.getReivew()\n%s",
      e.getMessage() );
    }
	return review;
  }
  
  /**
   * Return rating
   */
  public double getRating()
  {
	double rating = 0;
    try
    {
    	rating = theStock.getRating(pn);
    } catch( StockException e )
    {
      DEBUG.error("ReviewClient.getReivew()\n%s",
      e.getMessage() );
    }
	return rating;
  }
  
  /**
   * ask for update of view callled at start
   */
  private void askForUpdate()
  {
    setChanged(); notifyObservers("START only"); // Notify
  }
  
  public State getState()
  {
	  return theState;
  }
}

