package dbAccess;

/**
 * Implements Read /Write access to the stock list
 * The stock list is held in a relational DataBase
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */

import catalogue.Product;
import debug.DEBUG;
import middle.StockException;
import middle.StockReadWriter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

// There can only be 1 ResultSet opened per statement
// so no simultaneous use of the statement object
// hence the synchronized methods
// 

/**
  * Implements read/write access to the stock database.
  */
public class StockRW extends StockR implements StockReadWriter 
{
  /*
   * Connects to database
   */
  public StockRW() throws StockException
  {    
    super();        // Connection done in StockR's constructor
  }
  
  /**
   * Customer buys stock, quantity decreased if sucessful.
   * @param pNum Product number
   * @param amount Amount of stock bought
   * @return true if succeeds else false
   */
  public synchronized boolean buyStock( String pNum, int amount )
         throws StockException
  {
    DEBUG.trace("DB StockRW: buyStock(%s,%d)", pNum, amount);
    int updates = 0;
    try
    {
      getStatementObject().executeUpdate(
        "update StockTable set stockLevel = stockLevel-" + amount +
        "       where productNo = '" + pNum + "' and " +
        "             stockLevel >= " + amount + ""
      );
      updates = 1; // getStatementObject().getUpdateCount();
    } catch ( SQLException e )
    {
      throw new StockException( "SQL buyStock: " + e.getMessage() );
    }
    DEBUG.trace( "buyStock() updates -> %n", updates );
    return updates > 0;   // sucess ?
  }

  /**
   * Adds stock (Re-stocks) to the store.
   *  Assumed to exist in database.
   * @param pNum Product number
   * @param amount Amount of stock to add
   */
  public synchronized void addStock( String pNum, int amount )
         throws StockException
  {
    try
    {
      getStatementObject().executeUpdate(
        "update StockTable set stockLevel = stockLevel + " + amount +
        "         where productNo = '" + pNum + "'"
      );
      //getConnectionObject().commit();
      DEBUG.trace( "DB StockRW: addStock(%s,%d)" , pNum, amount );
    } catch ( SQLException e )
    {
      throw new StockException( "SQL addStock: " + e.getMessage() );
    }
  }

  /**
   * Adds reservation to the store.
   * @param name Customer name
   * @return reservation number
   */
  public synchronized int addReservation( String name )
         throws StockException
  {
    try
    {
      getStatementObject().executeUpdate(
        "insert into reservationtable values (0, null, '" + name + "')"
      );
      int reservationNo = -1;
      ResultSet rs = getStatementObject().executeQuery("Select Max(reservationNo) from reservationtable");
      if(rs.next())
    	  reservationNo = rs.getInt("Max(reservationNo)");
      //getConnectionObject().commit();
      DEBUG.trace( "DB StockRW: addReservation(%s)" , name);
      rs.close();
      return reservationNo;
    } catch ( SQLException e )
    {
      throw new StockException( "SQL addStock: " + e.getMessage() );
    }
  }
	
  public synchronized void addReservedProduct(int rNum, String pNum, int amount)
			throws StockException {
    try
    {
      getStatementObject().executeUpdate(
        "insert into reservationdetailtable values (" + rNum + ", " + "'" + pNum + "', " + amount + ")"
      );
      DEBUG.trace( "DB StockRW: addReservedProduct(%d,%s,%d)" , rNum, pNum, amount);
    } catch ( SQLException e )
    {
      throw new StockException( "SQL addReservedProduct: " + e.getMessage() );
    }
  }
  	/**
  	 * remove reservation
  	 */
	public synchronized void removeReservation(int rNum) 
			throws StockException 
	{
	    try
	    {
			// delete from reservation table
			getStatementObject().executeUpdate(
	    		"delete from reservationtable where reservationNo = " + rNum
	    	);
			// delete from reservationdetailtable
			getStatementObject().executeUpdate(
		    	"delete from reservationdetailtable where reservationNo = " + rNum
		    );
  	      	DEBUG.trace( "DB StockR: removeReservation()");
	    } catch ( SQLException e )
	    {
	    	throw new StockException( "SQL removeReservation: " + e.getMessage() );
	    }
	}

	
  /**
   * Modifies Stock details for a given product number.
   *  Assumed to exist in database.
   * Information modified: Description, Price
   * @param detail Product details to change stocklist to
   */
  public synchronized void modifyStock( Product detail )
         throws StockException
  {
    DEBUG.trace( "DB StockRW: modifyStock(%s)", 
                 detail.getProductNum() );
    try
    {
      if ( ! exists( detail.getProductNum() ) )
      {
    	getStatementObject().executeUpdate( 
         "insert into ProductTable values ('" +
            detail.getProductNum() + "', " + 
             "'" + detail.getDescription() + "', " + 
             "'images/Pic" + detail.getProductNum() + ".jpg', " + 
             "'" + detail.getPrice() + "' " + ")"
            );
    	getStatementObject().executeUpdate( 
           "insert into StockTable values ('" + 
           detail.getProductNum() + "', " + 
           "'" + detail.getQuantity() + "' " + ")"
           ); 
      } else {
    	getStatementObject().executeUpdate(
          "update ProductTable " +
          "  set description = '" + detail.getDescription() + "' , " +
          "      price       = " + detail.getPrice() +
          "  where productNo = '" + detail.getProductNum() + "' "
         );
       
    	getStatementObject().executeUpdate(
          "update StockTable set stockLevel = " + detail.getQuantity() +
          "  where productNo = '" + detail.getProductNum() + "'"
        );
      }
      //getConnectionObject().commit();
      
    } catch ( SQLException e )
    {
      throw new StockException( "SQL modifyStock: " + e.getMessage() );
    }
  }

	/**
	 * add review and rating reservation
	 */
	public synchronized void addReviewAndRating(String pNum, String review, double rating) 
			throws StockException 
	{
	    try
	    {
			// delete from reservation table
			getStatementObject().executeUpdate(
	    		"insert into reviewtable values ('" + pNum + "', " + "'" +  review + "', " + rating + ")"
	    	);
	      	DEBUG.trace( "DB StockRW: addReviewAndRating()");
	    } catch ( SQLException e )
	    {
	    	throw new StockException( "SQL addReviewAndRating: " + e.getMessage() );
	    }
	}

	/**
	 * add review and rating reservation
	 */
	public synchronized void updateRecommendLevel(String pNum1, String pNum2) 
			throws StockException 
	{
	    try
	    {
	    	String query = "SELECT * FROM productpairtable "
					+ "WHERE (productNo = '" + pNum1 + "' AND pairNo = '" + pNum2 + "')"
					+ " OR (productNo = '" + pNum2 + "' AND pairNo = '" + pNum1 + "')";
	    	ResultSet rs = getStatementObject().executeQuery(query);
    		boolean res = rs.next();
    		int level = 0;
    		if(res)
    			level = rs.getInt("level");
    		rs.close();
    		
    		level += 1;
    		
    		if(res) {
    			query = "UPDATE productpairtable SET level=" + level 
        				+ " WHERE (productNo = '" + pNum1 + "' AND pairNo = '" + pNum2 + "')"
        				+ " OR (productNo = '" + pNum2 + "' AND pairNo = '" + pNum1 + "')";
    			getStatementObject().executeUpdate(query);
    	    }
    		else {
    			getStatementObject().executeUpdate(
	    			"INSERT into productpairtable values ('" + pNum1 + "', '" + pNum2 + "', " + level + ")");
    		}
    		DEBUG.trace( "DB StockR: updateRecommendLevel()" );
	    } catch ( SQLException e )
	    {
	    	throw new StockException( "SQL updateRecommendLevel: " + e.getMessage() );
	    }
	}
}
