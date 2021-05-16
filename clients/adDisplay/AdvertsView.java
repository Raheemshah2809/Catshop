package clients.adDisplay;

import catalogue.Basket;
import catalogue.BetterBasket;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;
import middle.MiddleFactory;
import middle.StockReader;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Adverts view.
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */

public class AdvertsView implements Observer
{
  private static final int H = 600;       // Height of window pixels
  private static final int W = 800;       // Width  of window pixels
  private static final int UPDATEINTERVAL = 100; // Ads updating interval
  private static final int ADSCOUNT = 3; // Ads count

  private AudioClip theAudioClip;

  private final Label theDescription  = new Label();
  private ImageView thePicture = new ImageView();
  private BorderPane theBorderPane = new BorderPane();
//  private AnchorPane theAngchorPan = new AnchorPane();
  private StockReader theStock   = null;
  private AdvertsController cont= null;


  /**
   * Construct the view
   * @param stage   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-cordinate of position of window on screen 
   * @param y     y-cordinate of position of window on screen  
   */
  
  public AdvertsView(Stage stage, MiddleFactory mf, int x, int y )
  {
    // Create an AudioClip, which loads the audio data synchronously
    final URL resource = getClass().getResource("/audio/welcome.mp3");
    theAudioClip = new AudioClip(resource.toExternalForm());

    try                                             //
    {      
      theStock  = mf.makeStockReader();             // Database Access
    } catch ( Exception e )
    {
      System.out.println("Exception: " + e.getMessage() );
    }

    stage.setWidth( W ); // Set Window Size
    stage.setHeight( H );
    stage.setX( x );  // Set Window Position
    stage.setY( y );
    
    thePicture.setStyle("-fx-background-color: WHITE");
    thePicture.setFitWidth( 750 );   // Picture area
    thePicture.setFitHeight( 480 );
    thePicture.setPreserveRatio(true);
    thePicture.setSmooth(true);
    thePicture.setCache(true);
    theBorderPane.setStyle("-fx-background-color: WHITE");
    theBorderPane.setCenter(thePicture);
    
    theDescription.setMaxWidth(Double.MAX_VALUE);
    theDescription.setPrefSize( 750, 40 );
    AnchorPane.setLeftAnchor(theDescription, 0.0);
    AnchorPane.setRightAnchor(theDescription, 0.0);
    theDescription.setAlignment(Pos.CENTER);
    theDescription.setText( "" );                        //  Blank

    VBox root = new VBox();
    root.setSpacing(10);   //Setting the space between the nodes of a root pane

    ObservableList rootList = root.getChildren(); //retrieving the observable list of the root pane
    rootList.addAll(theBorderPane, theDescription); //Adding all the nodes to the observable list


    // Set the Size of the GridPane
    root.setMinSize(800, 600);

    String rootStyle = "-fx-padding: 10;-fx-border-style: solid inside; -fx-border-width: 1; -fx-border-insets: 5;" +
            "-fx-border-radius: 5; -fx-border-color: purple; -fx-background-color: #b19cd9;";
    String labelStyle = "-fx-font-family: Calibri; -fx-font-size: 16px; -fx-font-weight: bolder;";
    root.setStyle(rootStyle);
    theDescription.setStyle(labelStyle);

    Scene scene = new Scene(root);  // Create the Scene
    stage.setScene(scene); // Add the scene to the Stage
//    theAudioClip.setCycleCount(AudioClip.INDEFINITE);
//    theAudioClip.play();
    Timeline adTimeLine = new Timeline(
            new KeyFrame(Duration.seconds(5),event->showAds()));
    adTimeLine.setCycleCount(Timeline.INDEFINITE);
    adTimeLine.play();
  }

   /**
   * The controller object, used so that an interaction can be passed to the controller
   * @param c   The controller
   */

  public void setController( AdvertsController c )
  {
    cont = c;
  }

  public static int adsIdx = 0;
  public void showAds()
  {
    if(cont == null)
      return;
    // update Ads content in every 500S
    if(adsIdx % UPDATEINTERVAL == 0)
      cont.findTopSellers(ADSCOUNT);

    // show Ads
    String description = cont.getDescription(adsIdx % ADSCOUNT);
    Image image = cont.getPicture(adsIdx % ADSCOUNT);

    thePicture.setImage(image);
//    centerImage();
    theDescription.setText(description);

    adsIdx++;
  }
  
  private void centerImage() {
      Image img = thePicture.getImage();
      if (img != null) {
          double w = 0;
          double h = 0;

          double ratioX = thePicture.getFitWidth() / img.getWidth();
          double ratioY = thePicture.getFitHeight() / img.getHeight();

          double reducCoeff = 0;
          if(ratioX >= ratioY) {
              reducCoeff = ratioY;
          } else {
              reducCoeff = ratioX;
          }

          w = img.getWidth() * reducCoeff;
          h = img.getHeight() * reducCoeff;

          thePicture.setX((thePicture.getFitWidth() - w) / 2);
          thePicture.setY((thePicture.getFitHeight() - h) / 2);
      }
  }
  /**
   * Update the view
   * @param modelC   The observed model
   * @param arg      Specific args 
   */
   
  public void update( Observable modelC, Object arg )
  {
//    AdvertsModel model  = (AdvertsModel) modelC;
//    String message = (String) arg;
//    theAction.setText( message );
//    Image image = model.getPicture();  // Image of product
//    if ( image == null )
//    {
//      thePicture.setImage(null);                  // Clear picture
//    } else {
//      thePicture.setImage( image );             // Display picture
//    }
//    theOutput.setText( model.getBasket().getDetails() );
//    theInput.requestFocus();               // Focus is here
  }
}
