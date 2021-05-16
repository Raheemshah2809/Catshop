package clients.customer;

import catalogue.Basket;
import catalogue.BetterBasket;
import javafx.collections.*;
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
import middle.MiddleFactory;
import middle.StockReader;

import javax.swing.*;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */

public class CustomerView implements Observer
{
  class Name                              // Names of buttons
  {
    public static final String CHECK  = "Check";
    public static final String CLEAR  = "Clear";
    public static final String RESERVE  = "Reserve";
    public static final String SUBMIT  = "Submit";
    public static final String REMOVE  = "Remove";
  }

  private static final int H = 600;       // Height of window pixels
  private static final int W = 800;       // Width  of window pixels

  private AudioClip theAudioClip;

  private final Label theAction  = new Label();
  private final Label theInputName = new Label();
  private final TextField  theInput   = new TextField();
  private final TextArea   theOutput  = new TextArea();
  private final Button     theBtCheck = new Button( Name.CHECK );
  private final Button     theBtClear = new Button( Name.CLEAR );
  private final Button     theBtReserve = new Button( Name.RESERVE );
  private final Button     theBtSubmit = new Button( Name.SUBMIT );
  private final Button     theBtRemove = new Button( Name.REMOVE );

  private ImageView thePicture = new ImageView();

  private StockReader theStock   = null;
  private CustomerController cont= null;


  /**
   * Construct the view
   * @param stage   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-cordinate of position of window on screen 
   * @param y     y-cordinate of position of window on screen  
   */
  
  public CustomerView(Stage stage, MiddleFactory mf, int x, int y )
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

    theBtCheck.setPrefSize( 100, 40 ); // Check Button Size
    theBtCheck.setOnAction(event -> cont.doCheck(theInput.getText()));

    theBtClear.setPrefSize( 100, 40 ); // Clear Button Size
    theBtClear.setOnAction(event -> cont.doClear());

    theBtReserve.setPrefSize( 100, 40 ); // Reserve Button Size
    theBtReserve.setOnAction(event -> cont.doReserve(theInput.getText()));

    theBtSubmit.setPrefSize( 100, 40 ); // Submit Button Size
    theBtSubmit.setOnAction(event -> cont.doSubmitReservation());

    theBtRemove.setPrefSize( 100, 40 ); // Submit Button Size
    theBtRemove.setOnAction(event -> cont.doRemove());

    thePicture.setFitWidth( 100 );   // Picture area
    thePicture.setFitHeight( 100 );

    theAction.setPrefSize( 650, 20 );
    theAction.setText( "Welcome!" );                        //  Blank

    theInputName.setPrefSize(100, 40);
    theInputName.setText("Product No:");
    
    theInput.setPrefSize( 540, 40 );
    theInput.setText("");                           // Blank

    theOutput.setPrefSize( 650, 460 );
    theOutput.setText( "" );                        //  Blank

    GridPane buttonBar = new GridPane();
    buttonBar.addColumn(0, theBtCheck, thePicture, theBtClear, theBtReserve, theBtRemove, theBtSubmit);
    buttonBar.setVgap(30); // Set the horizontal spacing to 10px
    
    GridPane inputBar = new GridPane();
    inputBar.addRow(0, theInputName, theInput);
    inputBar.setHgap(10);
    
    GridPane infoBar = new GridPane();
    infoBar.addColumn(0, theAction, inputBar, theOutput);
    infoBar.setVgap(10);

    HBox root = new HBox();
    root.setSpacing(10);   //Setting the space between the nodes of a root pane

    ObservableList rootList = root.getChildren(); //retrieving the observable list of the root pane
    rootList.addAll(buttonBar, infoBar); //Adding all the nodes to the observable list


    root.setMinSize(800, 600);

    String rootStyle = "-fx-padding: 10;-fx-border-style: solid inside; -fx-border-width: 1; -fx-border-insets: 5;" +
            "-fx-border-radius: 5; -fx-border-color: purple; -fx-background-color: #b19cd9;";
    String redButtonStyle = "-fx-background-radius: 1em; -fx-background-color: red; -fx-text-fill: white; -fx-font-family: 'Calibri'; -fx-font-weight: bolder; -fx-font-size: 14px";
    String blueButtonStyle = "-fx-background-radius: 1em; -fx-background-color: blue; -fx-text-fill: white; -fx-font-family: 'Calibri'; -fx-font-weight: bolder; -fx-font-size: 14px";
    String brownButtonStyle = "-fx-background-radius: 1em; -fx-background-color: brown; -fx-text-fill: white; -fx-font-family: 'Calibri'; -fx-font-weight: bolder; -fx-font-size: 14px";
    String midnightblueButtonStyle = "-fx-background-radius: 1em; -fx-background-color: midnightblue; -fx-text-fill: white; -fx-font-family: 'Calibri'; -fx-font-weight: bolder; -fx-font-size: 14px";
    String greyButtonStyle = "-fx-background-radius: 1em; -fx-background-color: grey; -fx-text-fill: white; -fx-font-family: 'Calibri'; -fx-font-weight: bolder; -fx-font-size: 14px";
    String inputStyle = "-fx-background-color:lightgreen; -fx-font-family: Calibri; -fx-font-size: 16px";
    String richAreaStyle = "-fx-control-inner-background:lightgreen; -fx-font-family: Calibri; -fx-font-size: 16px";
    String labelStyle = "-fx-font-family: Calibri; -fx-font-size: 14px; -fx-font-weight: bolder;";
    
    root.setStyle(rootStyle);
    theBtClear.setStyle(brownButtonStyle);
    theBtCheck.setStyle(redButtonStyle);
    theBtReserve.setStyle(midnightblueButtonStyle);
    theBtSubmit.setStyle(blueButtonStyle);
    theBtRemove.setStyle(greyButtonStyle);
    theInputName.setStyle(labelStyle);
    theInput.setStyle(inputStyle);
    theOutput.setStyle(richAreaStyle);
    theAction.setStyle(labelStyle);

    Scene scene = new Scene(root);  // Create the Scene
    stage.setScene(scene); // Add the scene to the Stage
    theAudioClip.setCycleCount(AudioClip.INDEFINITE);
    theAudioClip.play();


    theInput.requestFocus();  // Focus is here
  }

   /**
   * The controller object, used so that an interaction can be passed to the controller
   * @param c   The controller
   */

  public void setController( CustomerController c )
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
    CustomerModel model  = (CustomerModel) modelC;
    String        message = (String) arg;
    theAction.setText( message );

    Image image = model.getPicture();  // Image of product
    if ( image == null )
    {
      thePicture.setImage(null);                 // Clear picture
    } else {
      thePicture.setImage(image);             // Display picture
    }
   	theOutput.setText( model.getBasket().getDetails() );
    theInput.requestFocus();               // Focus is here
  }
}
