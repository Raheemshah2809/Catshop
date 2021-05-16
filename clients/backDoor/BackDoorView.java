package clients.backDoor;

import javafx.collections.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import middle.MiddleFactory;
import middle.StockReadWriter;
import middle.StockReader;

import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */

public class BackDoorView implements Observer
{
  private static final String RESTOCK  = "Add";
  private static final String CLEAR    = "Clear";
  private static final String QUERY    = "Query";
 
  private static final int H = 600;       // Height of window pixels
  private static final int W = 800;       // Width  of window pixels

  private final Label      theAction  = new Label();
  private final Label      theInputName  = new Label();
  private final Label      theAmountName  = new Label();
  private final TextField  theInput   = new TextField();
  private final TextField  theInputAmount = new TextField();
  private final TextArea   theOutput  = new TextArea();
  private final Button     theBtClear = new Button( CLEAR );
  private final Button     theBtRStock = new Button( RESTOCK );
  private final Button     theBtQuery = new Button( QUERY );
  
  private StockReadWriter theStock     = null;
  private BackDoorController cont= null;

  /**
   * Construct the view
   * @param stage   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-cordinate of position of window on screen 
   * @param y     y-cordinate of position of window on screen  
   */
  public BackDoorView(  Stage stage, MiddleFactory mf, int x, int y )
  {
    try                                             // 
    {      
      theStock = mf.makeStockReadWriter();          // Database access
    } catch ( Exception e )
    {
      System.out.println("Exception: " + e.getMessage() );
    }
    stage.setWidth( W ); // Set Window Size
    stage.setHeight( H );
    stage.setX( x );  // Set Window Position
    stage.setY( y );
    
//    Font f = new Font("Monospaced",Font.PLAIN,12);  // Font f is

    theBtQuery.setPrefSize( 100, 40 ); // Buy button
    theBtQuery.setOnAction(event->cont.doQuery( theInput.getText() ) ); // Call back code

    theBtRStock.setPrefSize( 100, 40 ); // Check Button
    theBtRStock.setOnAction(event->cont.doRStock( theInput.getText(),
            theInputAmount.getText() ) ); // Call back code

    theBtClear.setPrefSize( 100, 40 ); // Clear button
    theBtClear.setOnAction(event->cont.doClear() ); // Call back code


    theAction.setPrefSize( 650, 20 ); // Message area
    theAction.setText( "Welcome!" );                        // Blank

    theInputName.setPrefSize( 100, 40 ); // Input Area
    theInputName.setText("Product No:");                           // Blank

    theInput.setPrefSize( 210, 40 ); // Input Area
    theInput.setText("");                           // Blank

    theAmountName.setPrefSize( 110, 40 ); // Input Area
    theAmountName.setText("Product Amount:");                           // Blank

    theInputAmount.setPrefSize( 200, 40 ); // Input Area
    theInputAmount.setText("0");                        // 0

    theOutput.setPrefSize( 650, 460 ); // Output text area
    theOutput.setText( "" );                        //  Blank

    GridPane buttonPane = new GridPane(); // button Pane
    buttonPane.addColumn(0, theBtQuery, theBtRStock, theBtClear);
    buttonPane.setVgap(30); // Vertical Spacing

    GridPane inputPane = new GridPane();
    inputPane.addRow(0, theInputName, theInput, theAmountName, theInputAmount);
    inputPane.setHgap(10);

    GridPane infoPane = new GridPane();
    infoPane.addColumn(0, theAction, inputPane, theOutput);
    infoPane.setVgap(10);

    HBox root = new HBox();
    root.setSpacing(10);   //Setting the space between the nodes of a root pane

    ObservableList rootList = root.getChildren(); // retrieving the observable list of the root pane
    rootList.addAll(buttonPane, infoPane); // Adding all the nodes to the observable list


    // Set the Size of the GridPane
    root.setMinSize(800, 600);
    // Set style
    String rootStyle = "-fx-padding: 10;-fx-border-style: solid inside; -fx-border-width: 1; -fx-border-insets: 5;" +
            "-fx-border-radius: 5; -fx-border-color: purple; -fx-background-color: #b19cd9;";
    String redButtonStyle = "-fx-background-radius: 1em; -fx-background-color: red; -fx-text-fill: white; -fx-font-family: 'Calibri'; -fx-font-weight: bolder; -fx-font-size: 14px";
    String blueButtonStyle = "-fx-background-radius: 1em; -fx-background-color: blue; -fx-text-fill: white; -fx-font-family: 'Calibri'; -fx-font-weight: bolder; -fx-font-size: 14px";
    String brownButtonStyle = "-fx-background-radius: 1em; -fx-background-color: brown; -fx-text-fill: white; -fx-font-family: 'Calibri'; -fx-font-weight: bolder; -fx-font-size: 14px";
    String inputStyle = "-fx-background-color:lightgreen; -fx-font-family: Calibri; -fx-font-size: 16px";
    String richAreaStyle = "-fx-control-inner-background:lightgreen; -fx-font-family: Calibri; -fx-font-size: 16px";
    String labelStyle = "-fx-font-family: Calibri; -fx-font-size: 14px; -fx-font-weight: bolder;";

    root.setStyle(rootStyle);
    theBtQuery.setStyle(redButtonStyle);
    theBtRStock.setStyle(blueButtonStyle);
    theBtClear.setStyle(brownButtonStyle);
    theAction.setStyle(labelStyle);
    theOutput.setStyle(richAreaStyle);
    theInput.setStyle(inputStyle);
    theInputAmount.setStyle(inputStyle);
    theInputName.setStyle(labelStyle);
    theAmountName.setStyle(labelStyle);
    
    Scene scene = new Scene(root);  // Create the Scene
    stage.setScene(scene); // Add the scene to the Stage

    theInput.requestFocus();                        // Focus is here
  }
  
  public void setController( BackDoorController c )
  {
    cont = c;
  }

  /**
   * Update the view
   * @param modelC   The observed model
   * @param arg      Specific args 
   */
  @Override
  public void update( Observable modelC, Object arg )
  {
    BackDoorModel model  = (BackDoorModel) modelC;
    String        message = (String) arg;
    theAction.setText( message );
    
    theOutput.setText( model.getBasket().getDetails() );
    theInput.requestFocus();
  }

}