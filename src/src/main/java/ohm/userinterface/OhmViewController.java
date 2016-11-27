package ohm.userinterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import ohm.Input.CameraInput;
import ohm.ValueIdentification.ResistorColour;
import ohm.ImageProcessing.BandReader;
import ohm.Input.ImageInput;
import ohm.Input.Input;
import ohm.ValueIdentification.ValueCalculator;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.awt.color.ColorSpace;
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
        Input CameraInput = new CameraInput();
        final Image src = CameraInput.getImage();
        final Mat rgbframe = CameraInput.getRGB();
        final Mat labframe = CameraInput.getLAB();


        resistorAxisPickerView.setImage(rgbframe);
        resistorAxisPickerView.setListener((p1, p2) -> points = BandReader.read(rgbframe,p1,p2));

        this.processedImageView.setRenderer(() -> {
            Mat processed = rgbframe.clone();
            ArrayList<ResistorColour> values = new ArrayList<ResistorColour>();
            for (int i = 0; i < Math.min(points.size()-1, 7); i = i + 2){
                Point p1 = points.get(i);
                Point p2 = points.get(i+1);
                Point midpoint = new Point((p1.x + p2.x)/2, (p1.y + p2.y)/2);
                Scalar colourAtMidpoint = new Scalar(labframe.get((int) midpoint.y, (int) midpoint.x));
                ResistorColour resistorColourAtMidpoint = ResistorColour
                        .fit(colourAtMidpoint.val[0], colourAtMidpoint.val[1],
                                colourAtMidpoint.val[2], ColorSpace.TYPE_Lab);
                Scalar rgbAtMidpoint = new Scalar(processed.get((int) midpoint.y, (int) midpoint.x));
                Imgproc.circle(processed, p1, 1, new Scalar(0, 0, 255), 2);
                Imgproc.circle(processed, p2, 1, new Scalar(0, 0, 255), 2);
                Imgproc.circle(processed, midpoint, 10, rgbAtMidpoint, 2);
                Imgproc.putText(processed,
                        Integer.toString(resistorColourAtMidpoint.value),p1,1,1,new Scalar(255,255,255));
                values.add(resistorColourAtMidpoint);
            }
            if (values.size() == 4){
                ValueCalculator vc = new ValueCalculator(values.get(0),values.get(1), values.get(2),values.get(3));
                Imgproc.putText(processed,vc.getValue(), new Point(200,340),1,3,new Scalar(0,0,0));
            }
            return matToImage(processed);
        });


    }

    @FXML
    public void buttonClicked(ActionEvent actionEvent) {

    }
}

/** @}*/
