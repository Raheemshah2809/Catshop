package clients.catalogueDisplay;

import catalogue.Product;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.StockException;
import middle.StockReader;

import java.util.*;

public class CatalogueModel extends Observable {
    private ArrayList<Product> productList  = new ArrayList<>();          // product list
    private StockReader theStock     = null;

    /*
     * Construct the model of the Catalogue
     * @param mf The factory to create the connection objects
     */
    public CatalogueModel(MiddleFactory mf)
    {
        try                                          //
        {
            theStock = mf.makeStockReader();           // Database access
        } catch ( Exception e )
        {
            DEBUG.error("CatalogueModel.constructor\n" +
                    "Database not created?\n%s\n", e.getMessage() );
        }
    }

    /**
     * return the list of products
     * @return the list of products
     */
    public ArrayList<Product> getProductList()
    {
        return productList;
    }

    /**
     * find product
     * @param productName The product name or part of product name
     */
    public void findProduct(String productName )
    {
        productList.clear();                          // Clear s. list
        String theAction = "";
        String prName  = productName.trim();                    // Product no.
        int    amount  = 1;                         //  & quantity
        try
        {
            productList = theStock.findProducts(prName);
            if ( productList.isEmpty() != true )              // Stock Exists?
            {                                         // T
                amount = productList.size();
                theAction =  String.format( "%2d Products Found. ", amount );
            } else {
                theAction = "Not found product!";
            }
        } catch( StockException e )
        {
            DEBUG.error("CatalogueClient.findProducts()\n%s",
                    e.getMessage() );
        }
        setChanged(); notifyObservers(theAction);
    }

    /**
     * get result string
     */
    public String getFindResult( )
    {
        Locale uk = Locale.UK;
        StringBuilder sb = new StringBuilder(256);
        Formatter fr = new Formatter(sb, uk);
        String csign = (Currency.getInstance( uk )).getSymbol();

        for (Product p: productList )
        {
            int number = p.getQuantity();
            fr.format("%-7s",       p.getProductNum() );
            fr.format("%-14.14s ",  p.getDescription() );
            fr.format("(%3d) ",     number );
            fr.format("\t%s%7.2f",    csign, p.getPrice() );
            fr.format("\n");
        }
        fr.close();
        return sb.toString();
    }
     /**
     * clear findings
     */
    public void doClear( )
    {
        productList.clear();                          // Clear s. list
        String theAction =  String.format( "Welcome!" );
        setChanged(); notifyObservers(theAction);
    }

    /**
     * ask for update of view callled at start
     */
    private void askForUpdate()
    {
        setChanged(); notifyObservers("START only"); // Notify
    }
}
