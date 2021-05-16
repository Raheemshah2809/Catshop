package middle;

/**
 * Facade for read access to the stock list.
 * The actual implementation of this is held on the middle tier.
 * The actual stock list is held in a relational DataBase on the 
 * third tier.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */

import catalogue.Basket;
import catalogue.Product;
import debug.DEBUG;
import javafx.scene.image.Image;
import remote.RemoteStockR_I;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Setup connection to the middle tier
 */

public class F_StockR implements StockReader
{
  private RemoteStockR_I aR_StockR   = null;
  private String         theStockURL = null;

  public F_StockR( String url )
  {
    DEBUG.trace("F_StockR: %s", url );
    theStockURL = url;
  }
  
  private void connect() throws StockException
  {
    try                                             // Setup
    {                                               //  connection
      aR_StockR =                                   //  Connect to
        (RemoteStockR_I) Naming.lookup(theStockURL);// Stub returned
    }
    catch ( Exception e )                           // Failure to
    {                                               //  attach to the
      aR_StockR = null;
      throw new StockException( "Com: " + 
                                e.getMessage()  );  //  object
      
    }
  }

  /**
   * Checks if the product exits in the stock list
   * @return true if exists otherwise false
   */
  @Override
  public synchronized boolean exists( String number )
         throws StockException
  {
    DEBUG.trace("F_StockR:exists()" );
    try
    {
      if ( aR_StockR == null ) connect();
      return aR_StockR.exists( number );
    } catch ( RemoteException e )
    {
      aR_StockR = null;
      throw new StockException( "Net: " + e.getMessage() );
    }
  }

  /**
   * Returns details about the product in the stock list
   * @return StockNumber, Description, Price, Quantity
   */

  @Override
  public synchronized Product getDetails( String number )
         throws StockException
  {
    DEBUG.trace("F_StockR:getDetails()" );
    try
    {
      if ( aR_StockR == null ) connect();
      return aR_StockR.getDetails( number );
    } catch ( RemoteException e )
    {
      aR_StockR = null;
      throw new StockException( "Net: " + e.getMessage() );
    }
  }

  @Override
  public synchronized ArrayList<Product> findProducts(String pName) throws StockException {
    DEBUG.trace("F_StockR:findProducts()" );
    try
    {
      if ( aR_StockR == null ) connect();
      return aR_StockR.findProducts(pName);
    }
    catch ( RemoteException e )
    {
      aR_StockR = null;
      throw new StockException( "Net: " + e.getMessage() );
    }
  }


  @Override
  public synchronized Image getImage(String number )
         throws StockException
  {
    DEBUG.trace("F_StockR:getImage()" );
    try
    {
      if ( aR_StockR == null ) connect();
      return aR_StockR.getImage( number );
    }
    catch ( RemoteException e )
    {
      aR_StockR = null;
      throw new StockException( "Net: " + e.getMessage() );
    }
  }

  @Override
  public synchronized List<String> getTopProducts(int count) 
		  throws StockException 
  {
    DEBUG.trace("F_StockR:getTopProducts()" );
    try
    {
      if ( aR_StockR == null ) connect();
      return aR_StockR.getTopProducts( count );
    }
    catch ( RemoteException e )
    {
      aR_StockR = null;
      throw new StockException( "Net: " + e.getMessage() );
    }
  }

  @Override
	public synchronized boolean isInStock(String pNum, int amount) 
			throws StockException 
	{
	    DEBUG.trace("F_StockR:available()" );
	    try
	    {
	      if ( aR_StockR == null ) connect();
	      return aR_StockR.isInStock(pNum, amount);
	    } catch ( RemoteException e )
	    {
	      aR_StockR = null;
	      throw new StockException( "Net: " + e.getMessage() );
	    }
	}

  @Override
	public synchronized Basket getReservation(int rNum) 
			throws StockException 
	{
	    DEBUG.trace("F_StockR:getReservation()" );
	    try
	    {
	      if ( aR_StockR == null ) connect();
	      return aR_StockR.getReservation(rNum);
	    } catch ( RemoteException e )
	    {
	      aR_StockR = null;
	      throw new StockException( "Net: " + e.getMessage() );
	    }
	}

  @Override
	public synchronized boolean isInReservations(int rNum) 
			throws StockException 
	{
	    DEBUG.trace("F_StockR:isInReservations()" );
	    try
	    {
	      if ( aR_StockR == null ) connect();
	      return aR_StockR.isInReservations(rNum);
	    } catch ( RemoteException e )
	    {
	      aR_StockR = null;
	      throw new StockException( "Net: " + e.getMessage() );
	    }
	}
	
  @Override
	public synchronized int getExpiredReservationNum() 
			throws StockException 
	{
	    DEBUG.trace("F_StockR:getReservation()" );
	    try
	    {
	      if ( aR_StockR == null ) connect();
	      return aR_StockR.getExpiredReservationNum();
	    } catch ( RemoteException e )
	    {
	      aR_StockR = null;
	      throw new StockException( "Net: " + e.getMessage() );
	    }
	}
	
  @Override
	public synchronized String getReview(String pNum)
			throws StockException
	{
	    DEBUG.trace("F_StockR:getReservation()" );
	    try
	    {
	      if ( aR_StockR == null ) connect();
	      return aR_StockR.getReview(pNum);
	    } catch ( RemoteException e )
	    {
	      aR_StockR = null;
	      throw new StockException( "Net: " + e.getMessage() );
	    }
	}
	
  @Override
	public synchronized double getRating(String pNum)
			throws StockException
	{
	    DEBUG.trace("F_StockR:getReservation()" );
	    try
	    {
	      if ( aR_StockR == null ) connect();
	      return aR_StockR.getRating(pNum);
	    } catch ( RemoteException e )
	    {
	      aR_StockR = null;
	      throw new StockException( "Net: " + e.getMessage() );
	    }
	}
	
  @Override
	public synchronized String getRecommendedProduct(String pNum)
			throws StockException
	{
	    DEBUG.trace("F_StockR:getRecommendedProduct()" );
	    try
	    {
	      if ( aR_StockR == null ) connect();
	      return aR_StockR.getRecommendedProduct(pNum);
	    } catch ( RemoteException e )
	    {
	      aR_StockR = null;
	      throw new StockException( "Net: " + e.getMessage() );
	    }
	}
}
