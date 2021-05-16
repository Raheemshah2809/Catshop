package clients.catalogueDisplay;

import clients.customer.CustomerModel;
import clients.customer.CustomerView;

public class CatalogueController {
    private CatalogueModel model = null;
    private CatalogueView view  = null;

    /**
     * Constructor
     * @param model The model
     * @param view  The view from which the interaction came
     */
    public CatalogueController( CatalogueModel model, CatalogueView view )
    {
        this.view  = view;
        this.model = model;
    }

    /**
     * find interaction from view
     * @param pName The product name
     */
    public void doFindProduct( String pName )
    {
        model.findProduct(pName);
    }

    /**
     * clear interaction from view
     */
    public void doClear( )
    {
        model.doClear();
    }
}