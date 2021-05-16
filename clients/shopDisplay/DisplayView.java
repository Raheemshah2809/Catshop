package clients.shopDisplay;

import middle.MiddleFactory;
import middle.OrderException;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * The visual display seen by customers (Change to graphical version)
 * Change to a graphical display
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */
public class DisplayView implements Observer
{
  private static final long serialVersionUID = 1L;
  private int H = 600;         // Height of window 
  private int W = 800;         // Width  of window 
  private String textToDisplay = "";
  private DisplayController cont= null;
  private Canvas theCanvas = new Canvas(W, H);
  private Pane theRoot;
  /**
   * Construct the view
   * @param rpc   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-coordinate of position of window on screen 
   * @param y     y-coordinate of position of window on screen  
   */
  
  public DisplayView(  Stage stage, MiddleFactory mf, int x, int y )
  {
	  stage.setWidth( W ); // Set Window Size
	  stage.setHeight( H );
	  stage.setX( x );  // Set Window Position
	  stage.setY( y );
	  
	  GraphicsContext gc = theCanvas.getGraphicsContext2D();
	  // Set line width
	  gc.setLineWidth(5.0);
	  // Create the Pane
	  theRoot = new Pane();
	  // Set the Style-properties of the Pane
	  String rootStyle = "-fx-padding: 10;-fx-border-style: solid inside; -fx-border-width: 1; -fx-border-insets: 5;" +
			  "-fx-border-radius: 5; -fx-border-color: purple; -fx-background-color: #b19cd9;";

	  theRoot.setStyle(rootStyle);
	  // Add the Canvas to the Pane
	  theRoot.getChildren().add(theCanvas);
	  // Create the Scene
	  Scene scene = new Scene(theRoot);
	  // Add the Scene to the Stage
	  stage.setScene(scene);
	  // Display the Stage
	  stage.show();
  }
  
  
  public void setController( DisplayController c )
  {
    cont = c;
  }
  
  /**
   * Called to update the display in the shop
   */
  @Override
  public void update( Observable aModelOfDisplay, Object arg )
  {
    // Code to update the graphical display with the current
    //  state of the system
    //  Orders awaiting processing
    //  Orders being picked in the 'warehouse. 
    //  Orders awaiting collection
    
    try
    {
      Map<String, List<Integer> > res =
      ( (DisplayModel) aModelOfDisplay ).getOrderState();

      textToDisplay = 
           "Orders in system" + "\n" +
           "Waiting        : " + listOfOrders( res, "Waiting" ) + 
           "\n"  + 
           "Being picked   : " + listOfOrders( res, "BeingPicked" ) + 
           "\n"  + 
           "To Be Collected: " + listOfOrders( res, "ToBeCollected" );
    }
    catch ( OrderException err )
    {
      textToDisplay = "\n" + "** Communication Failure **";
    }
    
    // Draw state of system on display
    String lines[] = textToDisplay.split("\n");

    GraphicsContext gc = theCanvas.getGraphicsContext2D();
	// Set fill color
	gc.setFill(new Color(0.69, 0.611, 0.847, 1));
    gc.fillRect(10, 10, W-40, H-60);
    
    gc.setFill(Color.BLACK);
    gc.setFont(new Font(60));
    for ( int i=0; i<lines.length; i++ )
    {
    	gc.fillText( lines[i], 50, 100 + 120*i, 1000 );
    }
  }

  /**
   * Return a string of order numbers
   * @param map Contains the current state of the system
   * @param key The key of the list requested
   * @return As a string a list of order numbers.
   */
  private String listOfOrders( Map<String, List<Integer> > map, String key )
  {
    String res = "";
    if ( map.containsKey( key ))
    {
      List<Integer> orders = map.get(key);
      for ( Integer i : orders )
      {
        res += " " + i;
      }
    } else {
      res = "-No key-";
    }
    return res;
  }
}
