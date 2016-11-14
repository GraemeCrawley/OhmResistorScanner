package ohm.userinterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import ohm.ValueIdentification.ResistorColour;
import ohm.ImageProcessing.BandReader;
import ohm.Input.ImageInput;
import ohm.Input.Input;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static ohm.Helpers.*;

/**
 * @addtogroup UserInterface
 * @{
 */


/**
 * @brief Coordinates the algorithms present in the model with user input through the view.
 */
public class OhmViewController implements Initializable {

    @FXML
    public Button startCameraButton;
    @FXML
    private ResistorAxisPickerView resistorAxisPickerView;
    @FXML
    private LiveImageView processedImageView;
    @FXML
    private Slider thresholdSlider1;
    @FXML
    private Slider thresholdSlider2;

    private List<Point> points = new ArrayList<>();

    @Override
    /**
     * Method used to "glue" together the front and back end of the application.
     */
    public void initialize(URL url, ResourceBundle rb) {
        Input imageInput = new ImageInput();
        final Image src = imageInput.getImage();
        final Mat frame = imageInput.getMat();

        resistorAxisPickerView.setImage(frame);
        resistorAxisPickerView.setListener((p1, p2) -> points = BandReader.read(frame,p1,p2));

        this.processedImageView.setRenderer(() -> {
            Mat processed = frame.clone();
            for (int i = 0; i < Math.min(points.size()-1, 7); i = i + 2){
                Point p1 = points.get(i);
                Point p2 = points.get(i+1);
                Point midpoint = new Point((p1.x + p2.x)/2, (p1.y + p2.y)/2);
                Scalar colourAtMidpoint = new Scalar(processed.get((int) midpoint.y, (int) midpoint.x));
                ResistorColour resistorColourAtMidpoint = ResistorColour.fit(colourAtMidpoint.val[2], colourAtMidpoint.val[1], colourAtMidpoint.val[0]);
                Imgproc.circle(processed, p1, 1, new Scalar(255, 0, 0), 2);
                Imgproc.circle(processed, p2, 1, new Scalar(255, 0, 0), 2);
                Imgproc.circle(processed, midpoint, 10, colourAtMidpoint, 2);
                Imgproc.putText(processed, Integer.toString(resistorColourAtMidpoint.value),p1,1,1,new Scalar(255,255,255));

            }
            return matToImage(processed);
        });


    }

    @FXML
    public void buttonClicked(ActionEvent actionEvent) {

    }
}

/** @}*/
