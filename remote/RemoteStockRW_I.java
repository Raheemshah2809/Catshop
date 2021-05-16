/**
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */

package remote;

import catalogue.Product;
import middle.StockException;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Defines the RMI interface for read/write access to the stock object.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */

public interface RemoteStockRW_I
       extends   RemoteStockR_I, Remote
{
  boolean buyStock(String number, int amount)
          throws RemoteException, StockException;
  void    addStock(String number, int amount)
          throws RemoteException, StockException;
  void    modifyStock(Product detail)
          throws RemoteException, StockException;
  int addReservation(String name) 
		  throws RemoteException, StockException;
  void addReservedProduct(int rNum, String pNum, int amount) 
		  throws RemoteException, StockException;
  void removeReservation(int rNum)
		  throws RemoteException, StockException;
  void addReviewAndRating(String pNum, String review, double rating) 
		  throws RemoteException, StockException;
  void updateRecommendLevel(String pNum1, String pNum2) 
		  throws RemoteException, StockException;
}

