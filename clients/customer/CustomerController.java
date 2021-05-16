package clients.customer;

/**
 * The Customer Controller
 * @author M A Smith (c) June 2014
 */

public class CustomerController
{
  private CustomerModel model = null;
  private CustomerView  view  = null;

  /**
   * Constructor
   * @param model The model 
   * @param view  The view from which the interaction came
   */
  public CustomerController( CustomerModel model, CustomerView view )
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
   * reserve product
   * @param pn The product number to be reserved
   */
  public void doReserve( String pn )
  {
    model.doReserve(pn);
  }
  
  /**
   * remove last reserved product
   */
  public void doRemove()
  {
    model.doRemove();
  }
  
  /**
   * submit reservation
   */
  public void doSubmitReservation()
  {
    model.doSubmitReservation();
  }

  /**
   * Clear interaction from view
   */
  public void doClear()
  {
    model.doClear();
  }

  
}

