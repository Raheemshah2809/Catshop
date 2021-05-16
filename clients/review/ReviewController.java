package clients.review;

public class ReviewController {
  private ReviewModel model = null;
  private ReviewView  view  = null;

  /**
   * Constructor
   * @param model The model 
   * @param view  The view from which the interaction came
   */
  public ReviewController( ReviewModel model, ReviewView view )
  {
    this.view  = view;
    this.model = model;
  }

  /**
   * Check interaction from view
   * @param pn The product number to be checked
   */
  public void doCheck( String pn )
  {
    model.doCheck(pn);
  }

  /**
   * reivew product
   * @param rating The rating string to be reviewed
   */
  public void doReview( String review, String rating )
  {
	double fRating = 0.0;
	try {
      fRating = Double.parseDouble(rating);
    } catch(Exception e) {
	  fRating = 0.0;
    }
    model.doReview(review, fRating);
  }
}

