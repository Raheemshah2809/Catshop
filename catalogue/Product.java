package catalogue;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Used to hold the following information about
 * a product: Product number, Description, Price and
 * Stock level.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */

public class Product implements Serializable, Comparator<Product>
{
  private static final long serialVersionUID = 20092506;
  private String theProductNum;       // Product number
  private String theDescription;      // Description of product
  private double thePrice;            // Price of product
  private int    theQuantity;  // Quantity involved

  /**
   * Construct a product details
   * @param aProductNum Product number
   * @param aDescription Description of product
   * @param aPrice The price of the product
   * @param aQuantity The Quantity of the product involved
   */
  public Product( String aProductNum, String aDescription,
                  double aPrice, int aQuantity )
  {
    theProductNum  = aProductNum;     // Product number
    theDescription = aDescription;    // Description of product
    thePrice       = aPrice;          // Price of product
    theQuantity    = aQuantity;       // Quantity involved
  }
  
  public String getProductNum()  { return theProductNum; }
  public String getDescription() { return theDescription; }
  public double getPrice()       { return thePrice; }
  public int    getQuantity()    { return theQuantity; }
  
  public void setProductNum( String aProductNum )
  { 
    theProductNum = aProductNum;
  }
  
  public void setDescription( String aDescription )
  { 
    theDescription = aDescription;
  }
  
  public void setPrice( double aPrice )
  { 
    thePrice = aPrice;
  }
  
  public void setQuantity( int aQuantity )
  { 
    theQuantity = aQuantity;
  }
  
  public void increaseQuantity( )
  {
	theQuantity += 1;
  }
  
  public boolean decreaseQuantity()
  {
	  theQuantity -= 1;
	  if(theQuantity < 1)
		  return false;
	  else
		  return true;
  }
  
  // check if the product is equal to the other
  @Override
  public boolean equals(Object other)
  {
	  if(theProductNum.compareTo(((Product)other).getProductNum()) != 0)
		  return false;
	  else
		  return true;
  }
  
  /**
   *  compare two product's product number
   *  return 0 : numbers are equal, 
   *  		1: arg0's product number is greater than arg1's
   *  		-1: arg0's product number is less than arg1
   */
	@Override
	public int compare(Product pr1, Product pr2) {
		return pr1.getProductNum().compareTo(pr2.getProductNum());
	}

}
