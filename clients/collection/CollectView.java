package clients.collection;

import middle.MiddleFactory;
import middle.OrderProcessing;
import javafx.collections.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */

public class CollectView implements Observer
{
    private static final String COLLECT = "Collect";

    private static final int H = 600;       // Height of window pixels
    private static final int W = 800;       // Width  of window pixels

    private final Label      theAction  = new Label();
    private final Label      theInputName  = new Label();
    private final TextField  theInput   = new TextField();
    private final TextArea   theOutput  = new TextArea();
    private final Button     theBtCollect= new Button( COLLECT );

    private OrderProcessing   theOrder = null;
    private CollectController cont     = null;

    /**
    * Construct the view
    * @param stage   Window in which to construct
    * @param mf    Factor to deliver order and stock objects
    * @param x     x-cordinate of position of window on screen
    * @param y     y-cordinate of position of window on screen
    */
    public CollectView(  Stage stage, MiddleFactory mf, int x, int y )
    {
        try                                           //
        {
            theOrder = mf.makeOrderProcessing();        // Process order
        } catch ( Exception e )
        {
            System.out.println("Exception: " + e.getMessage() );
        }

        stage.setWidth( W ); // Set Window Size
        stage.setHeight( H );
        stage.setX( x );  // Set Window Position
        stage.setY( y );

        //    Font f = new Font("Monospaced",Font.PLAIN,12);  // Font f is

        theBtCollect.setPrefSize( 100, 40 );  // Check Button
        theBtCollect.setOnAction(                 // Call back code
            event -> cont.doCollect( theInput.getText()) );

        theAction.setPrefSize( 650, 20 );       // Message area
        theAction.setText( "Welcome!" );                        // Blank

        theInputName.setPrefSize( 100, 40 );         // Input Area
        theInputName.setText("Order Number:");                           // Blank

        theInput.setPrefSize( 540, 40 );         // Input Area
        theInput.setText("");                           // Blank

        theOutput.setPrefSize( 650, 460 );          // In TextArea
        theOutput.setText( "" );                        //  Blank
//        theOutput.setFont( f );                         //  Uses font

        GridPane buttonPane = new GridPane(); // button Pane
        buttonPane.addColumn(0, theBtCollect );
        buttonPane.setVgap(30); // Vertical Spacing

        GridPane iputPane = new GridPane(); // button Pane
        iputPane.addRow(0, theInputName, theInput );
        iputPane.setHgap(10); // Vertical Spacing

        GridPane infoPane = new GridPane();
        infoPane.addColumn(0, theAction, iputPane, theOutput);
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
        String pinkButtonStyle = "-fx-background-radius: 1em; -fx-background-color: pink; -fx-text-fill: white; -fx-font-family: 'Calibri'; -fx-font-weight: bolder; -fx-font-size: 14px";
        String greyButtonStyle = "-fx-background-radius: 1em; -fx-background-color: grey; -fx-text-fill: white; -fx-font-family: 'Calibri'; -fx-font-weight: bolder; -fx-font-size: 14px";
        String inputStyle = "-fx-background-color:lightgreen; -fx-font-family: Calibri; -fx-font-size: 16px";
        String richAreaStyle = "-fx-control-inner-background:lightgreen; -fx-font-family: Calibri; -fx-font-size: 16px";
        String labelStyle = "-fx-font-family: Calibri; -fx-font-size: 14px; -fx-font-weight: bolder;";

        root.setStyle(rootStyle);
        theBtCollect.setStyle(blueButtonStyle);
        theAction.setStyle(labelStyle);
        theInput.setStyle(inputStyle);
        theInputName.setStyle(labelStyle);
        theOutput.setStyle(richAreaStyle);

        Scene scene = new Scene(root);  // Create the Scene
        stage.setScene(scene); // Add the scene to the Stage

        theInput.requestFocus();                        // Focus is here
    }

    public void setController( CollectController c )
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
        CollectModel model  = (CollectModel) modelC;
        String        message = (String) arg;
        theAction.setText( message );

        theOutput.setText( model.getResponce() );
        theInput.requestFocus();               // Focus is here
    }

}
