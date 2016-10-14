package ohm;

import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 * This class displays an opencv Mat as an image and allows the user to pick
 * two endpoints of a line. Once two valid points, a listener is called
 */
public class MatrixLinePicker extends ImageView implements EventHandler<MouseEvent>{

    private Point p1 = null;
    private Point p2 = null;

    private Mat sourceImage = null;
    private Mat displayImage = null;

    private Listener listener = null;


    @Override
    public void handle(MouseEvent event) {
        // Convert the mouse event's location to pixel values that match the image
        double matX = sourceImage.width() * event.getX() / this.getBoundsInParent().getWidth();
        double matY = sourceImage.height() * event.getY() / this.getBoundsInParent().getHeight();

        Point click = new Point(matX,matY);

        // Decide which point to replace with the clicked point
        if (p1 == null){
            p1 = new Point(matX,matY);
        }else if (p2 == null){
            p2 = new Point(matX,matY);
        }else {
            // If both points exist, replace the closer one
            if (Helpers.dist(p1,click) < Helpers.dist(p2,click)){
                p1 = click;
            }else{
                p2 = click;
            }

        }

        // Render circles over the clicked points
        displayImage = sourceImage.clone();
        if (p1 != null){
            Imgproc.circle(displayImage, p1,3,new Scalar(255, 0, 0, 255),2);
        }
        if (p2 != null) {
            Imgproc.circle(displayImage, p2, 3, new Scalar(255, 0, 0, 255), 2);
        }
        this.setImage(Helpers.matToImage(displayImage));

        // Update the listeners
        if (p1 != null && p2 != null){
            listener.onLinePicked(p1,p2);
        }
    }


    /**
     * Set the matrix to display
     * @param newImage
     */
    public void setImage(Mat newImage){
        sourceImage = newImage;
        displayImage = sourceImage.clone();
        p1 = null;
        p2 = null;
        this.setImage(Helpers.matToImage(displayImage));
    }

    /**
     * Assign a line listener to be updated when new points are selected
     * @param listener
     */
    public void setListener(Listener listener){
        this.setOnMouseClicked(this);
        this.listener = listener;
    }

    interface Listener{
        void onLinePicked(Point p1, Point p2);
    }
}
