package remote;

import catalogue.Basket;
import catalogue.Product;
import dbAccess.StockRW;
import javafx.scene.image.Image;
import middle.StockException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

// There can only be 1 ResultSet opened per statement
// so no simultaneous use of the statement object
// hence the synchronized methods

/**
 * Implements Read/Write access to the stock list,
 * the stock list is held in a relational DataBase.
 * @author  Mike Smith University of Brighton
 * @version 2.1
 */

public class      R_StockRW
       extends    java.rmi.server.UnicastRemoteObject
       implements RemoteStockRW_I
{
  private static final long serialVersionUID = 1;
  private StockRW aStockRW = null;

  /**
   * All transactions are done via StockRW to ensure
   * that a single connection to the database is used for all transactions
   * @param url of remote object
   * @throws java.rmi.RemoteException if issue
   * @throws middle.StockException if issue
   */
  public R_StockRW(String url)
         throws RemoteException, StockException
  {
    aStockRW = new StockRW();
  }
  
  /**
   * Returns true if product exists
   * @param pNum The product number
   * @return true if product exists else false
   * @throws middle.StockException if underlying error
   */
  public synchronized boolean exists( String pNum )
         throws StockException
  {
    return aStockRW.exists( pNum );
  }

  /**
   * Returns details about the product in the stock list
   * @param pNum The product number
   * @return StockNumber, Description, Price, Quantity
   * @throws middle.StockException if underlying error
   */
  public synchronized Product getDetails( String pNum )
         throws StockException
  {
    return aStockRW.getDetails( pNum );
  }

  /**
   * Returns an image of the product in the stock list
   * @param pNum The product number
   * @return image
   * @throws middle.StockException if underlying error
   */
  public synchronized Image getImage(String pNum )
         throws StockException
  {
    return aStockRW.getImage( pNum );
  }


  /**
   * Buys stock and hence decrements number in the stock list
   * @param pNum product number
   * @param amount amount required
   * @return StockNumber, Description, Price, Quantity
   * @throws middle.StockException if underlying error
   */
  // Need to Fix
  //  What happens if can not commit data
  //
  public synchronized boolean buyStock( String pNum, int amount )
         throws StockException
  {
    return aStockRW.buyStock( pNum, amount );
  }

  /**
   * Adds (Restocks) stock to the product list
   * @param pNum The product number
   * @param amount Quantity
   * @throws middle.StockException if underlying error
   */
  public synchronized void addStock( String pNum, int amount )
         throws StockException
  {
    aStockRW.addStock( pNum, amount );
  }


  	/**
  	 * Modifies Stock details for a given product number.
  	 * Information modified: Description, Price
  	 * @param product The product to be modified
  	 * @throws middle.StockException if underlying error
  	 */
  	public synchronized void modifyStock( Product product )
              throws StockException
  	{
  		aStockRW.modifyStock( product );
  	}

  /**
   * Returns an Product numbers of the Top seller products
   * @param count products count
   * @return List of Product numbers
   */
  public synchronized List<String> getTopProducts(int count)
          throws StockException
  {
    return aStockRW.getTopProducts(count);
  }

  /**
   * Returns products found using name
   * @param pName Product name
   * @return list of products
   * @throws StockException if issue
   */
  public synchronized ArrayList<Product> findProducts(String pName)
          throws StockException
  {
    return aStockRW.findProducts(pName);
  }

  public synchronized boolean isInStock(String pNum, int amount) 
		throws StockException 
  {
	return aStockRW.isInStock(pNum, amount);
  }

	public synchronized int addReservation(String name) 
			throws StockException {
		return aStockRW.addReservation(name);
	}
	
	public synchronized void addReservedProduct(int rNum, String pNum, int amount)
			throws StockException 
	{
		aStockRW.addReservedProduct(rNum, pNum, amount);
	}

	public synchronized Basket getReservation(int rNum) 
			throws StockException 
	{
		return aStockRW.getReservation(rNum);
	}

	public synchronized boolean isInReservations(int rNum) 
			throws RemoteException, StockException 
	{
		return aStockRW.isInReservations(rNum);
	}

	public synchronized int getExpiredReservationNum() 
			throws RemoteException, StockException 
	{
		return aStockRW.getExpiredReservationNum();
	}
	
	public synchronized void removeReservation(int rNum) 
			throws RemoteException, StockException 
	{
		aStockRW.removeReservation(rNum);
	}
	
	public synchronized String getReview(String pNum)
			throws RemoteException, StockException
	{
		return aStockRW.getReview(pNum);
	}

	public synchronized double getRating(String pNum)
			throws RemoteException, StockException
	{
		return aStockRW.getRating(pNum);
	}

	public synchronized void addReviewAndRating(String pNum, String review, double rating)
			throws RemoteException, StockException {
		aStockRW.addReviewAndRating(pNum, review, rating);
	}
	
	public synchronized String getRecommendedProduct(String pNum)
			throws RemoteException, StockException {
		return aStockRW.getRecommendedProduct(pNum);
	}
	
	public synchronized void updateRecommendLevel(String pNum1, String pNum2)
			throws RemoteException, StockException {
		aStockRW.updateRecommendLevel(pNum1, pNum2);
	}
}
