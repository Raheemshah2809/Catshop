package clients.catalogueDisplay;

import clients.customer.CustomerController;
import clients.customer.CustomerModel;
import clients.customer.CustomerView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import middle.Names;
import middle.RemoteMiddleFactory;

public class CatalogueClient extends Application {
    public static RemoteMiddleFactory mrf;

    public static void main(String args[]) {
        String stockURL = args.length < 1         // URL of stock R
                ? Names.STOCK_R           //  default  location
                : args[0];                //  supplied location

        mrf = new RemoteMiddleFactory();
        mrf.setStockRInfo(stockURL);

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Catalogue Client (MVC RMI)");
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        CatalogueModel model = new CatalogueModel(mrf);
        CatalogueView view = new CatalogueView(primaryStage, mrf, 0, 0);
        CatalogueController cont = new CatalogueController(model, view);
        view.setController(cont);

        model.addObserver(view);       // Add observer to the model
        primaryStage.show();
    }
}