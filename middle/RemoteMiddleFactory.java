/**
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
package middle;

/**
  * Provide access to middle tier components.
  */

public class RemoteMiddleFactory implements MiddleFactory
{
  private String theStockR_URL   = "";
  private String theStockRW_URL  = "";
  private String theOrder_URL    = "";
  
  private String theStockR_URL2   = "";
  private String theStockRW_URL2  = "";
  private String theOrder_URL2    = "";
  
  public void setStockRInfo( String url )
  {
    theStockR_URL = url;
  }
  
  public void setStockRInfo2( String url )
  {
    theStockR_URL2 = url;
  }
  
  public void setStockRWInfo( String url )
  {
    theStockRW_URL = url;
  }
  
  public void setStockRWInfo2( String url )
  {
    theStockRW_URL2 = url;
  }
  
  public void setOrderInfo( String url )
  {
    theOrder_URL = url;
  }
 
  public void setOrderInfo2( String url )
  {
    theOrder_URL2 = url;
  }
 
  /**
   * Return an object to access the database for read only access.
   * Access is via RMI
   */
  
  public StockReader makeStockReader() throws StockException
  {
    return new F_StockR( theStockR_URL );
  }

  /**
   * Return an object to access the database for read/write access.
   * Access is via RMI
   */
  public StockReadWriter makeStockReadWriter() throws StockException
  {
    return new F_StockRW( theStockRW_URL );
  }
  
  /**
   * Return an object to access the order processing system.
   * Access is via RMI
   */
  public OrderProcessing makeOrderProcessing() throws OrderException
  {
    return new F_Order( theOrder_URL );
  }

  public StockReader makeStockReader2() throws StockException {
	return new F_StockR( theStockR_URL2 );
  }

  public StockReadWriter makeStockReadWriter2() throws StockException {
	return new F_StockRW( theStockRW_URL2 );
  }

  public OrderProcessing makeOrderProcessing2() throws OrderException {
	return new F_Order( theOrder_URL2 );
  }
}

