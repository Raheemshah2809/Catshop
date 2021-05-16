package clients.shopDisplay;

import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * The standalone shop Display Client.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public class DisplayClient  extends Application
{
	  public static RemoteMiddleFactory mrf;
   public static void main (String args[])
   {
     String stockURL = args.length < 1     // URL of stock RW
                     ? Names.STOCK_RW      //  default  location
                     : args[0];            //  supplied location
     String orderURL = args.length < 2     // URL of order
                     ? Names.ORDER         //  default  location
                     : args[1];            //  supplied location
     
    mrf = new RemoteMiddleFactory();
    mrf.setStockRWInfo( stockURL );
    mrf.setOrderInfo  ( orderURL );        //
    launch(args);                       // Create GUI
  }
  
	@Override
	public void start(Stage primaryStage) throws Exception {
	    primaryStage.setTitle("Shop Display Client (MVC RMI)");
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});

	    DisplayModel model = new DisplayModel(mrf);
	    DisplayView  view  = new DisplayView( primaryStage, mrf, 0, 0 );
	    DisplayController cont  = new DisplayController( model, view );
	    view.setController( cont );

	    model.addObserver( view );       // Add observer to the model
	    primaryStage.show();
	}
}
