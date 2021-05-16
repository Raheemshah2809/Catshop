package clients.customer;

import clients.customer.CustomerController;
import clients.customer.CustomerModel;
import clients.customer.CustomerView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

import javax.swing.*;

/**
 * The standalone Customer Client
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public class CustomerClient extends Application
{
  public static RemoteMiddleFactory mrf;

  public static void main (String args[])
  {
    String stockURL = args.length < 1         // URL of stock R
                    ? Names.STOCK_RW           //  default  location
                    : args[0];                //  supplied location
    
    String stockURL2 = args.length < 2         // URL of stock R
            ? Names.STOCK_RW2           //  default  location
            : args[0];                //  supplied location

    mrf = new RemoteMiddleFactory();
    mrf.setStockRWInfo(stockURL);
    mrf.setStockRWInfo2(stockURL2);
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("Customer Client (MVC RMI)");
    primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
      @Override
      public void handle(WindowEvent t) {
        Platform.exit();
        System.exit(0);
      }
    });

    CustomerModel model = new CustomerModel(mrf);
    CustomerView  view  = new CustomerView( primaryStage, mrf, 0, 0 );
    CustomerController cont  = new CustomerController( model, view );
    view.setController( cont );

    model.addObserver( view );       // Add observer to the model
    primaryStage.show();
  }
}
