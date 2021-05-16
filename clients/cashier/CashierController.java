package clients.cashier;


/**
 * The Cashier Controller
 * @author M A Smith (c) June 2014
 */

public class CashierController
{
  private CashierModel model = null;
  private CashierView  view  = null;

  /**
   * Constructor
   * @param model The model 
   * @param view  The view from which the interaction came
   */
  public CashierController( CashierModel model, CashierView view )
  {
    this.view  = view;
    this.model = model;
  }

  /**
   * Check interaction from view
   * @param pn The product number to be checked
   */
  public void doCheck( String pn )
  {
    model.doCheck(pn);
  }

  /**
   * Buy interaction from view
   */
  public void doBuy()
  {
    model.doBuy();
  }

  /**
   * Discount products in the basket
   */
  public void doDiscount( String rateString)
  {
	  double rate = Double.parseDouble(rateString);
	  model.doDiscount(rate);
  }

  /**
   * remove interaction from view
   */
  public void doRemove()
  {
    model.doRemove();
  }

  /**
   * load reservation
   */
  public void doLoadReservation(String sNum)
  {
	  int rNum = Integer.parseInt(sNum);
	  model.doLoadReservation(rNum);
  }

  /**
   * reserve product
   */
  public void doReserve()
  {
	  model.doReserve();
  }
  
  /**
   * Bought interaction from view
   */
  public void doBought()
  {
    model.doBought();
  }
}
