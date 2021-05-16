package remote;

import catalogue.Basket;
import catalogue.Product;
import javafx.scene.image.Image;
import middle.StockException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines the RMI interface for read access to the stock object.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */

public interface RemoteStockR_I
       extends Remote
{
  boolean   exists(String number)
            throws RemoteException, StockException;
  Product   getDetails(String number)
            throws RemoteException, StockException;
  Image getImage(String number)
            throws RemoteException, StockException;
  List<String> getTopProducts(int count)
            throws RemoteException, StockException;

  ArrayList<Product> findProducts(String pName) 
		  throws RemoteException, StockException;
  boolean isInStock(String pNum, int amount) 
		  throws RemoteException, StockException;
  
  Basket getReservation(int rNum) 
		  throws RemoteException, StockException;
  boolean isInReservations(int rNum) 
		  throws RemoteException, StockException;
  int getExpiredReservationNum() 
		  throws RemoteException, StockException;
  String getReview(String pNum) 
		  throws RemoteException, StockException;
  double getRating(String pNum) 
		  throws RemoteException, StockException;
  String getRecommendedProduct(String pNum) 
		  throws RemoteException, StockException;
}

