package dbAccess;

/**
 * Implements Read access to the stock list
 * The stock list is held in a relational DataBase
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */

import catalogue.Basket;
import catalogue.BetterBasket;
import catalogue.Product;
import debug.DEBUG;
import javafx.scene.image.Image;
import middle.StockException;
import middle.StockReader;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// There can only be 1 ResultSet opened per statement
// so no simultaneous use of the statement object
// hence the synchronized methods

// mySQL
//    no spaces after SQL statement ;

/**
  * Implements read only access to the stock database.
  */
public class StockR implements StockReader
{
  private Connection theCon    = null;      // Connection to database
  private Statement  theStmt   = null;      // Statement object

  /**
   * Connects to database
   * Uses a factory method to help setup the connection
   * @throws StockException if problem
   */
  public StockR()
         throws StockException
  {
    try
    {
      DBAccess dbDriver = (new DBAccessFactory()).getNewDBAccess();
      dbDriver.loadDriver();
    
      theCon  = DriverManager.getConnection
                  ( dbDriver.urlOfDatabase(), 
                    dbDriver.username(), 
                    dbDriver.password() );

      theStmt = theCon.createStatement();
      theCon.setAutoCommit( true );
    }
    catch ( SQLException e )
    {
      throw new StockException( "SQL problem:" + e.getMessage() );
    }
    catch ( Exception e )
    {
      throw new StockException("Can not load database driver.");
    }
  }


  /**
   * Returns a statement object that is used to process SQL statements
   * @return A statement object used to access the database
   */
  
  protected Statement getStatementObject()
  {
    return theStmt;
  }

  /**
   * Returns a connection object that is used to process
   * requests to the DataBase
   * @return a connection object
   */

  protected Connection getConnectionObject()
  {
    return theCon;
  }

  /**
   * Checks if the product exits in the stock list
   * @param pNum The product number
   * @return true if exists otherwise false
   */
  public synchronized boolean exists( String pNum )
         throws StockException
  {
    
    try
    {
      ResultSet rs   = getStatementObject().executeQuery(
        "select price from ProductTable " +
        "  where  ProductTable.productNo = '" + pNum + "'"
      );
      boolean res = rs.next();
      DEBUG.trace( "DB StockR: exists(%s) -> %s", 
                    pNum, ( res ? "T" : "F" ) );
      rs.close();
      return res;
    } catch ( SQLException e )
    {
      throw new StockException( "SQL exists: " + e.getMessage() );
    }
  }

  /**
   * Returns details about the product in the stock list.
   *  Assumed to exist in database.
   * @param pNum The product number
   * @return Details in an instance of a Product
   */
  public synchronized Product getDetails( String pNum )
         throws StockException
  {
    try
    {
      Product   dt = new Product( "0", "", 0.00, 0 );
      ResultSet rs = getStatementObject().executeQuery(
        "select description, price, stockLevel " +
        "  from ProductTable, StockTable " +
        "  where  ProductTable.productNo = '" + pNum + "' " +
        "  and    StockTable.productNo   = '" + pNum + "'"
      );
      if ( rs.next() )
      {
        dt.setProductNum( pNum );
        dt.setDescription(rs.getString( "description" ) );
        dt.setPrice( rs.getDouble( "price" ) );
        dt.setQuantity( rs.getInt( "stockLevel" ) );
      }
      rs.close();
      return dt;
    } catch ( SQLException e )
    {
      throw new StockException( "SQL getDetails: " + e.getMessage() );
    }
  }

  /**
   * Returns products found using name
   * @param pName Product name
   * @return list of products
   * @throws StockException if issue
   */
  public synchronized ArrayList<Product> findProducts(String pName) throws StockException {
    ArrayList<Product> productList = new ArrayList<>();
    try
    {
      ResultSet rs = getStatementObject().executeQuery(
              "select ProductTable.productNo as productNo, description, price, stockLevel " +
                      "  from ProductTable inner join StockTable " +
                      "  on  ProductTable.productNo = StockTable.productNo " +
                      "  where ProductTable.description  like '%" + pName + "%'"
      );
      boolean res = rs.next();
      while( res )
      {
        Product   dt = new Product( "0", "", 0.00, 0 );
        dt.setProductNum(rs.getString( "productNo" ) );
        dt.setDescription(rs.getString( "description" ) );
        dt.setPrice( rs.getDouble( "price" ) );
        dt.setQuantity( rs.getInt( "stockLevel" ) );

        productList.add(dt);
        res = rs.next();
      }
      rs.close();
      return productList;
    } catch ( SQLException e )
    {
      throw new StockException( "SQL getDetails: " + e.getMessage() );
    }
  }

  /**
   * Returns 'image' of the product
   * @param pNum The product number
   *  Assumed to exist in database.
   * @return ImageIcon representing the image
   */
  public synchronized Image getImage(String pNum )
         throws StockException
  {
    String filename = "default.jpg";
    try
    {
      ResultSet rs   = getStatementObject().executeQuery(
        "select picture from ProductTable " +
        "  where  ProductTable.productNo = '" + pNum + "'"
      );
      
      boolean res = rs.next();
      if ( res )
        filename = rs.getString( "picture" );
      rs.close();
    } catch ( SQLException e )
    {
      DEBUG.error( "getImage()\n%s\n", e.getMessage() );
      throw new StockException( "SQL getImage: " + e.getMessage() );
    }
    
    //DEBUG.trace( "DB StockR: getImage -> %s", filename );
    return new Image( filename );
  }

  /**
   * Returns product numbers of the Top Seller Products
   * @param count The products count
   * @return List of Product numbers of Top Seller Products
   */
  public synchronized List<String> getTopProducts(int count )
          throws StockException
  {
    List<String> topProducts = new ArrayList<>();
    String productNo;
    try
    {
      ResultSet rs   = getStatementObject().executeQuery(
              "select productNo from ProductTable " +
                      "  order by sales DESC limit " + count
      );

      boolean res = rs.next();
      while( res )
      {
        productNo = rs.getString("productNo");
        if(!productNo.isEmpty())
          topProducts.add(productNo);
        res = rs.next();
      }
      rs.close();
    } catch ( SQLException e )
    {
      DEBUG.error( "getTopProducts()\n%s\n", e.getMessage() );
      throw new StockException( "SQL getTopProducts: " + e.getMessage() );
    }

    //DEBUG.trace( "DB StockR: getTopProducts -> %s", filename );
    return topProducts;
  }

  /**
   * Checks if the product amount available in the stock
   * @param pNum Product number
   * @param reqAmount required amount
   * @return true if available otherwise false
   * @throws StockException if issue
   */
  public synchronized boolean isInStock(String pNum, int reqAmount) 
		  throws StockException 
  {
    int availAmount = 0;
    try
    {
      ResultSet rs   = getStatementObject().executeQuery(
              "select stocklevel from stocktable" +
                      "   where productNo = '" + pNum + "'"
      );

      if(rs.next()) {
        availAmount = rs.getInt("stocklevel");
      }
      rs.close();
    } catch ( SQLException e )
    {
      DEBUG.error( "available()\n%s\n", e.getMessage() );
      throw new StockException( "SQL available : " + e.getMessage() );
    }
    if(availAmount > reqAmount)
    	return true;
    else
    	return false;
  }


	public synchronized Basket getReservation(int rNum) throws StockException {
	    Basket basket = new BetterBasket();
	    try
	    {
	      ResultSet rs = getStatementObject().executeQuery(
	              "SELECT reservationdetailtable.productNo AS productNo, description, price, reservedAmount"
	              + " FROM reservationdetailtable, producttable"
	              + " WHERE reservationdetailtable.productNo = producttable.productNo"
	              + " AND reservationNo = " + rNum
	      );
	      boolean res = rs.next();
	      while( res )
	      {
	        Product pr = new Product(
	        		rs.getString("productNo"), 
	        		rs.getString("description"), 
	        		rs.getDouble("price"), 
	        		rs.getInt("reservedAmount")
	        );
	        basket.add(pr);
	        
	        res = rs.next();
	      }
	      rs.close();
	      return basket;
	    } catch ( SQLException e )
	    {
	      throw new StockException( "SQL getReservation: " + e.getMessage() );
	    }
	}

	public synchronized boolean isInReservations(int rNum) 
			throws StockException 
	{
	    try
	    {
	    	Timestamp validTimestamp = Timestamp.valueOf(LocalDateTime.now().minusHours(24));
	    	PreparedStatement statement = 
	    		getConnectionObject().prepareStatement("select * from ReservationTable" +
					" where  reservationNo = ? and reserveTime > ?");
	    	statement.setInt(1, rNum);
	    	statement.setTimestamp(2, validTimestamp);
    		ResultSet rs   = statement.executeQuery();
    		boolean res = rs.next();
    		rs.close();
    		DEBUG.trace( "DB StockR: isInReservations(%d) -> %d", 
    				rNum, ( res ? "T" : "F" ) );
    		return res;
	    } catch ( SQLException e )
	    {
	    	throw new StockException( "SQL isInReservations: " + e.getMessage() );
	    }
	}

	public synchronized int getExpiredReservationNum() 
			throws StockException 
	{
	    try
	    {
	    	Timestamp validTimestamp = Timestamp.valueOf(LocalDateTime.now().minusHours(24));
	    	PreparedStatement statement = 
	    		getConnectionObject().prepareStatement("select * from ReservationTable" +
					" where reserveTime < ?");
	    	statement.setTimestamp(1, validTimestamp);
    		ResultSet rs   = statement.executeQuery();
    		boolean res = rs.next();
    		int rNum = -1;
    		if(res)
    			rNum = rs.getInt("reservationNo");
    		DEBUG.trace( "DB StockR: getExpiredReservationNum()" );
    		rs.close();
    		return rNum;
	    } catch ( SQLException e )
	    {
	    	throw new StockException( "SQL getExpiredReservationNum: " + e.getMessage() );
	    }
	}
	
	public synchronized String getReview(String pNum)
		throws StockException
	{
		String review = "";
	    try
	    {
		      ResultSet rs = getStatementObject().executeQuery(
		              "SELECT comment FROM reviewtable WHERE productNo='" 
		            		  + pNum + "'"
		      );
		      boolean res = rs.next();
		      while( res )
		      {
		    	  review += rs.getString("comment");
		    	  review += "\n";
		    	  res = rs.next();
		      }
		      rs.close();
		      return review;
	    } catch ( SQLException e )
	    {
	    	throw new StockException( "SQL getExpiredReservationNum: " + e.getMessage() );
	    }	
	}
	
	public synchronized double getRating(String pNum)
			throws StockException
	{
	    try
	    {
			double rating = 0;
			int cnt = 0;
			ResultSet rs = getStatementObject().executeQuery(
					"SELECT rating FROM reviewtable WHERE productNo='" + pNum + "'"
			);
			boolean res = rs.next();
			while( res )
			{
		    	  rating += rs.getDouble("rating");
		    	  cnt++;
		    	  res = rs.next();
		      }
		      rs.close();
		      return (rating / cnt);
	    } catch ( SQLException e )
	    {
	    	throw new StockException( "SQL getRating: " + e.getMessage() );
	    }	
	}

	public synchronized String getRecommendedProduct(String pNum)
			throws StockException
	{
	    try
	    {
			String pairNo = "";
			ResultSet rs = getStatementObject().executeQuery(
					"SELECT * FROM productpairtable WHERE ( productNo='" + pNum + "' OR pairNo='" + pNum + "') "
					+ "ORDER BY level DESC LIMIT 1"
			);
			boolean res = rs.next();
			
			if( res ) {
				String sNo1 = rs.getString("productNo");
				String sNo2 = rs.getString("pairNo");
				if(pNum.compareTo(sNo1) == 0)
					pairNo = sNo2;
				else
					pairNo = sNo1;
			}
			rs.close();
			
		    return pairNo;
	    } catch ( SQLException e )
	    {
	    	throw new StockException( "SQL getRating: " + e.getMessage() );
	    }	
	}
}
