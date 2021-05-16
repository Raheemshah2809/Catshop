package clients.review;
import clients.review.ReviewController;
import clients.review.ReviewModel;
import clients.review.ReviewView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

public class ReviewClient extends Application {
	public static RemoteMiddleFactory mrf;

  public static void main (String args[])
  {
    String stockURL = args.length < 1         // URL of stock R
        ? Names.STOCK_R           //  default  location
        : args[0];                //  supplied location
    
    mrf = new RemoteMiddleFactory();
    mrf.setStockRInfo( stockURL );

    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("Customer Review (MVC RMI)");
    primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
      @Override
      public void handle(WindowEvent t) {
        Platform.exit();
        System.exit(0);
      }
    });

    ReviewModel model = new ReviewModel(mrf);
    ReviewView  view  = new ReviewView( primaryStage, mrf, 0, 0 );
    ReviewController cont  = new ReviewController( model, view );
    view.setController( cont );

    model.addObserver( view );       // Add observer to the model
    primaryStage.show();
  }
}
