package clients.review;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import org.controlsfx.control.Rating;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import middle.MiddleFactory;
import middle.StockReader;
import clients.review.ReviewController;
import clients.review.ReviewModel;
import clients.review.ReviewModel.State;

public class ReviewView implements Observer
{
  class Name                              // Names of buttons
  {
    public static final String CHECK  = "Check";
    public static final String SUBMIT  = "Submit";
  }

  private static final int H = 600;       // Height of window pixels
  private static final int W = 800;       // Width  of window pixels

  private final Label theAction  = new Label();
  
  private final Label theLabelPNo = new Label();
  private final TextField  theInputPNo   = new TextField();
  private final Button     theBtCheck = new Button( Name.CHECK );
  
  private final ImageView thePicture = new ImageView();
  private final BorderPane theBorderPane = new BorderPane();
  private final Label theLabelDesc  = new Label();
  
  private final Label   theLabelRating  = new Label();
  private final TextArea   theOutput  = new TextArea();
  
  private final Label theLabelReview = new Label();
  private final TextArea   theAreaReview  = new TextArea();
  private final Button     theBtSubmit = new Button( Name.SUBMIT );
  private final Label theLabelMyRating = new Label();
  private TextField theRating = new TextField();
  private StockReader theStock   = null;
  private ReviewController cont= null;


  /**
   * Construct the view
   * @param stage   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-cordinate of position of window on screen 
   * @param y     y-cordinate of position of window on screen  
   */
  
  public ReviewView(Stage stage, MiddleFactory mf, int x, int y )
  {
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

    theAction.setPrefSize( 650, 20 );
    theAction.setText( "Welcome!" );                        //  Blank

    theLabelPNo.setPrefSize(100, 40);
    theLabelPNo.setText("Product No:");
    
    theInputPNo.setPrefSize( 540, 40 );
    theInputPNo.setText("");                           // Blank

    theBtCheck.setPrefSize( 100, 40 ); // Check Button Size
    theBtCheck.setOnAction(event -> cont.doCheck(theInputPNo.getText()));

    thePicture.setStyle("-fx-background-color: WHITE");
    thePicture.setFitWidth( 370 );   // Picture area
    thePicture.setFitHeight( 350 );
    thePicture.setPreserveRatio(false);
    thePicture.setSmooth(true);
    thePicture.setCache(true);
    theBorderPane.setStyle("-fx-background-color: WHITE");
    theBorderPane.setCenter(thePicture);
    
    theLabelDesc.setMaxWidth(Double.MAX_VALUE);
    theLabelDesc.setPrefSize( 370, 40 );
    AnchorPane.setLeftAnchor(theLabelDesc, 0.0);
    AnchorPane.setRightAnchor(theLabelDesc, 0.0);
    theLabelDesc.setAlignment(Pos.CENTER);
    theLabelDesc.setText( "" ); 
    
    theLabelRating.setPrefSize(370, 40);
    theOutput.setPrefSize( 370, 350 );
    theOutput.setText( "" ); 
    
    //  Blank
    theLabelReview.setPrefSize(100,  40);
    theLabelReview.setText("My Reivew:");
    theAreaReview.setPrefSize(350, 40);
    theAreaReview.setText("");
    theRating.setPrefSize(100, 40);
    theLabelMyRating.setPrefSize(60, 40);//    theRating.setMax(5);
    theLabelMyRating.setText("Rating: ");//    theRating.setMax(5);
    theBtSubmit.setPrefSize( 100, 40 ); // Submit Button Size
    theBtSubmit.setOnAction(event -> cont.doReview(theAreaReview.getText(), theRating.getText()));

    // TODO change layout and add rating control

    GridPane prNumPane = new GridPane();
    prNumPane.addRow(0, theLabelPNo, theInputPNo, theBtCheck);
    prNumPane.setHgap(10); // Set the horizontal spacing to 10px
    
    GridPane imagePane = new GridPane();
    imagePane.addColumn(0, theBorderPane, theLabelDesc);
    imagePane.setVgap(10);
    
    GridPane reviewPane = new GridPane();
    reviewPane.addColumn(0, theOutput, theLabelRating);
    reviewPane.setVgap(10);
    
    GridPane irPane = new GridPane();
    irPane.addRow(0, imagePane, reviewPane);
    irPane.setHgap(10);
    
    GridPane pane1 = new GridPane();
    pane1.addRow(0, theLabelReview, theAreaReview);
    pane1.setHgap(10);

    GridPane pane2 = new GridPane();
    pane2.addRow(0, theLabelMyRating, theRating, theBtSubmit);
    pane2.setHgap(10);

    GridPane pane = new GridPane();
    pane.addRow(0, pane1, pane2);
    pane.setHgap(10);

    VBox root = new VBox();
    root.setSpacing(10);   //Setting the space between the nodes of a root pane

    ObservableList rootList = root.getChildren(); //retrieving the observable list of the root pane
    rootList.addAll(theAction, prNumPane, irPane, pane); //Adding all the nodes to the observable list

    root.setMinSize(800, 600);

    String rootStyle = "-fx-padding: 10;-fx-border-style: solid inside; -fx-border-width: 1; -fx-border-insets: 5;" +
            "-fx-border-radius: 5; -fx-border-color: purple; -fx-background-color: #b19cd9;";
    String redButtonStyle = "-fx-background-radius: 1em; -fx-background-color: red; -fx-text-fill: white; -fx-font-family: 'Calibri'; -fx-font-weight: bolder; -fx-font-size: 14px";
    String blueButtonStyle = "-fx-background-radius: 1em; -fx-background-color: blue; -fx-text-fill: white; -fx-font-family: 'Calibri'; -fx-font-weight: bolder; -fx-font-size: 14px";
    String brownButtonStyle = "-fx-background-radius: 1em; -fx-background-color: brown; -fx-text-fill: white; -fx-font-family: 'Calibri'; -fx-font-weight: bolder; -fx-font-size: 14px";
    String pinkButtonStyle = "-fx-background-radius: 1em; -fx-background-color: pink; -fx-text-fill: white; -fx-font-family: 'Calibri'; -fx-font-weight: bolder; -fx-font-size: 14px";
    String greyButtonStyle = "-fx-background-radius: 1em; -fx-background-color: grey; -fx-text-fill: white; -fx-font-family: 'Calibri'; -fx-font-weight: bolder; -fx-font-size: 14px";
    String inputStyle = "-fx-background-color:lightgreen; -fx-font-family: Calibri; -fx-font-size: 16px";
    String richAreaStyle = "-fx-control-inner-background:lightgreen; -fx-font-family: Calibri; -fx-font-size: 16px";
    String labelStyle = "-fx-font-family: Calibri; -fx-font-size: 14px; -fx-font-weight: bolder;";
    
    root.setStyle(rootStyle);
    theBtCheck.setStyle(redButtonStyle);
    theBtSubmit.setStyle(blueButtonStyle);
    theInputPNo.setStyle(inputStyle);
    theOutput.setStyle(richAreaStyle);
    theAreaReview.setStyle(richAreaStyle);
    theAction.setStyle(labelStyle);
    theLabelPNo.setStyle(labelStyle);
    theLabelReview.setStyle(labelStyle);
    theLabelRating.setStyle(labelStyle);
    theLabelDesc.setStyle(labelStyle);
    theRating.setStyle(inputStyle);
    theLabelMyRating.setStyle(labelStyle);

    Scene scene = new Scene(root);  // Create the Scene
    stage.setScene(scene); // Add the scene to the Stage

    theInputPNo.requestFocus();  // Focus is here
  }

   /**
   * The controller object, used so that an interaction can be passed to the controller
   * @param c   The controller
   */

  public void setController( ReviewController c )
  {
    cont = c;
  }

  /**
   * Update the view
   * @param modelC   The observed model
   * @param arg      Specific args 
   */
   
  public void update( Observable modelC, Object arg )
  {
	ReviewModel model  = (ReviewModel) modelC;
    String        message = (String) arg;
    theAction.setText( message );

    Image image = model.getPicture();  // Image of product
    if ( image == null )
    {
      thePicture.setImage(null);                 // Clear picture
    } else {
      thePicture.setImage(image);             // Display picture
    }
   	theOutput.setText( model.getReivew() );
   	theLabelDesc.setText(model.getDescription());
   	theLabelRating.setText( String.format("Rating of the Product : %.2f", model.getRating()) );
   	if(model.getState() == State.process) {
   		theRating.setText("0");
   		theAreaReview.setText("");
   	}
  }
}