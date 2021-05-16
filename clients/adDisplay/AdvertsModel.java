package clients.adDisplay;

import catalogue.Basket;
import catalogue.Product;
import debug.DEBUG;
import javafx.scene.image.Image;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockException;
import middle.StockReader;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Implements the Model of the Adverts client
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */
public class AdvertsModel extends Observable
{
  private StockReader     theStock     = null;

  private List<Product> theProductList = new ArrayList<>();          // product list
  private List<Image>     thePicList   = new ArrayList<>();

  /*
   * Construct the model of the Customer
   * @param mf The factory to create the connection objects
   */
  public AdvertsModel(MiddleFactory mf)
  {
    try                                          // 
    {  
      theStock = mf.makeStockReader();           // Database access
    } catch ( Exception e )
    {
      DEBUG.error("AdvertsModel.constructor\n" +
                  "Database not created?\n%s\n", e.getMessage() );
    }
  }
  
  /**
   * find top seller products
   * @param count count of top seller products
   */
  public void findTopSellers(int count )
  {
    theProductList.clear();
    thePicList.clear();
    List<String> productNoList = new ArrayList<>();
    try
    {
      productNoList = theStock.getTopProducts( count );
      for ( String pn:productNoList ) {
        Product pr = theStock.getDetails( pn );
        theProductList.add(pr);
        Image image = theStock.getImage(pn);
        thePicList.add(image);
      }
    } catch( StockException e )
    {
      DEBUG.error("CustomerClient.doCheck()\n%s",
      e.getMessage() );
    }
//    setChanged(); notifyObservers(theAction);
  }


  /**
   * Return a picture of the product
   * @param idx The product index in product list
   * @return An instance of an Image
   */
  public Image getPicture( int idx )
  {
    return thePicList.get( idx );
  }

  /**
   * Return a description of the product
   * @param idx The product index in product list
   * @return product description string
   */
  public String getDescription( int idx )
  {
    Product pr = theProductList.get(idx);
    return "No: " + pr.getProductNum()
            + " Desc: " + pr.getDescription();
  }

  /**
   * ask for update of view callled at start
   */
  private void askForUpdate()
  {
    setChanged(); notifyObservers("START only"); // Notify
  }
}

