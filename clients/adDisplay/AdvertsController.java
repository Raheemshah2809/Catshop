package clients.adDisplay;

import catalogue.Product;
import debug.DEBUG;
import javafx.scene.image.Image;
import middle.StockException;

import java.util.List;

/**
 * The Adverts Controller
 * @author M A Smith (c) June 2014
 */

public class AdvertsController
{
  private AdvertsModel model = null;
  private AdvertsView  view  = null;

  /**
   * Constructor
   * @param model The model 
   * @param view  The view from which the interaction came
   */
  public AdvertsController( AdvertsModel model, AdvertsView view )
  {
    this.view  = view;
    this.model = model;
  }

  /**
   * find top seller products
   * @param count count of top seller products
   */
  public void findTopSellers(int count )
  {
    model.findTopSellers(count);
  }


  /**
   * Return a picture of the product
   * @param idx The product index in product list
   * @return An instance of an Image
   */
  public Image getPicture(int idx )
  {
    return model.getPicture(idx);
  }

  /**
   * Return a description of the product
   * @param idx The product index in product list
   * @return product description string
   */
  public String getDescription( int idx )
  {
    return model.getDescription(idx);
  }

  
}

