package remote;

import catalogue.Basket;
import catalogue.Product;
import dbAccess.StockR;
import javafx.scene.image.Image;
import middle.StockException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

// There can only be 1 ResultSet opened per statement
// so no simultaneous use of the statement object
// hence the synchronized methods

/**
 * Implements Read access to the stock list,
 * the stock list is held in a relational DataBase.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public class      R_StockR
       extends    java.rmi.server.UnicastRemoteObject
       implements RemoteStockR_I
{
  private static final long serialVersionUID = 1;
  private StockR aStockR = null;

  public R_StockR( String url )
         throws RemoteException, StockException
  {
    aStockR = new StockR();
  }

  /**
   * Checks if the product exits in the stock list
   * @param pNum The product number
   * @return true if exists otherwise false
   */
  public synchronized boolean exists( String pNum )
         throws RemoteException, StockException
  {
    return aStockR.exists( pNum );
  }

  /**
   * Returns details about the product in the stock list
   * @param pNum The product number
   * @return StockNumber, Description, Price, Quantity
   */
  public synchronized Product getDetails( String pNum )
         throws RemoteException, StockException
  {
    return aStockR.getDetails( pNum );
  }

  /**
   * Returns an image of the product
   * BUG However this will not work for distributed version
   *     as an instance of an Image is not Serializable
   * @param pNum The product number
   * @return Image
   */
  public synchronized Image getImage(String pNum )
          throws RemoteException, StockException
  {
    return aStockR.getImage( pNum );
  }

  /**
   * Returns an Product numbers of the Top seller products
   * @param count products count
   * @return List of Product numbers
   */
  public synchronized  List<String> getTopProducts(int count)
          throws RemoteException, StockException
  {
    return aStockR.getTopProducts(count);
  }

  /**
   * Returns products found using name
   * @param pName Product name
   * @return list of products
   * @throws StockException if issue
   */
  public synchronized ArrayList<Product> findProducts(String pName)
          throws RemoteException, StockException
  {
    return aStockR.findProducts(pName);
  }

	public synchronized boolean isInStock(String pNum, int amount) 
		 throws RemoteException, StockException
	{
		return aStockR.isInStock(pNum, amount);
	}

	public synchronized Basket getReservation(int rNum) 
			throws StockException {
		return aStockR.getReservation(rNum);
	}

	public synchronized boolean isInReservations(int rNum) 
			throws RemoteException, StockException 
	{
		return aStockR.isInReservations(rNum);
	}
	
	public synchronized int getExpiredReservationNum()
			throws RemoteException, StockException
	{
		return aStockR.getExpiredReservationNum();
	}
	
	public synchronized String getReview(String pNum)
			throws RemoteException, StockException
	{
		return aStockR.getReview(pNum);
	}

	public synchronized double getRating(String pNum)
			throws RemoteException, StockException
	{
		return aStockR.getRating(pNum);
	}
	
	public synchronized String getRecommendedProduct(String pNum)
			throws RemoteException, StockException {
		return aStockR.getRecommendedProduct(pNum);
	}
}
