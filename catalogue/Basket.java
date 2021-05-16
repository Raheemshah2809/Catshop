package catalogue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Formatter;
import java.util.Locale;

import middle.OrderException;

/**
 * A collection of products from the CatShop.
 *  used to record the products that are to be/
 *   wished to be purchased.
 * @author  Mike Smith University of Brighton
 * @version 2.2
 *
 */
public class Basket extends ArrayList<Product> implements Serializable
{
  private static final long serialVersionUID = 1;
  private int    theOrderNum = 0;          // Order number
  private double theDiscountRate = 10.0; // discount rate
 
  private static int theNextNumber = 1;          // Start at order 1

  /**
   * Constructor for a basket which is
   *  used to represent a customer order/ wish list
   */
  public Basket()
  {
    theOrderNum  = theNextNumber;
  }
  
  /**
   * Set the customers unique order number
   */
  public synchronized void setUniqueOrderNum( )
  {
    theOrderNum = theNextNumber;
    theNextNumber += 1;
  }

  /**
   * Returns the customers unique order number
   * @return the customers order number
   */
  public int getOrderNum()
  {
    return theOrderNum;
  }
  
  /**
   * Returns total cost of products in basket
   * @return total cost of products in basket
   */
  public double getTotalCost()
  {
      double total = 0.0;
	  for ( Product pr: this )
      {
        total += pr.getPrice() * pr.getQuantity();
      }    
	  return total;
  }

  /**
   * Returns total cost with discount of products in basket
   * @return total cost with discount of products in basket
   */
  public double getTotalCostWithDiscount()
  {
	  return getTotalCost() * (1 - theDiscountRate / 100) ;
  }

  /**
   * get discount rate
   * @return discount rate of products in basket
   */
  public double getDiscountRate()
  {
    return theDiscountRate;
  }

  /**
   * Set discount rate
   */
  public void setDiscountRate(double discountRate)
  {
    theDiscountRate = discountRate;
  }

  /**
   * Add a product to the Basket.
   * Product is appended to the end of the existing products
   * in the basket.
   * @param pr A product to be added to the basket
   * @return true if successfully adds the product
   */
  // Will be in the Java doc for Basket
  @Override
  public boolean add( Product pr )
  {          
    return super.add( pr );     // Call add in ArrayList
  }

  /**
   * Remove a last product from the Basket.
   * @return Removed product
   */
  public Product remove()
  {
    int idx = super.size() - 1;
    Product pr = super.remove(idx);     // Call remove in ArrayList
    return pr;
  }

  /**
   * Returns a description of the products in the basket suitable for printing.
   * @return a string description of the basket products
   */
  public String getDetails()
  {
    Locale uk = Locale.UK;
    StringBuilder sb = new StringBuilder(256);
    Formatter     fr = new Formatter(sb, uk);
    String csign = (Currency.getInstance( uk )).getSymbol();
    if ( theOrderNum != 0 )
      fr.format( "Order number: %03d\n", theOrderNum );
      
    if ( this.size() > 0 )
    {
      for ( Product pr: this )
      {
        int number = pr.getQuantity();
        fr.format("%-7s",       pr.getProductNum() );
        fr.format("%-14.14s ",  pr.getDescription() );
        fr.format("(%3d) ",     number );
        fr.format("%s%7.2f",    csign, pr.getPrice() * number );
        fr.format("\n");
      }
      fr.format("----------------------------\n");
      fr.format("Discount Rate:  %7.2f\n", theDiscountRate);
      fr.format("Total                       ");
      fr.format("%s%7.2f\n",    csign, getTotalCost());
      fr.format("Total with discount         ");
      fr.format("%s%7.2f\n",    csign, getTotalCostWithDiscount());
      fr.close();
    }
    return sb.toString();
  }
}
