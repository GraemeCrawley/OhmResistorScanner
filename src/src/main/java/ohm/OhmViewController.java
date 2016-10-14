package ohm; /**
 * Created by jon on 2016-09-20.
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;

import java.net.URL;
import java.util.ResourceBundle;

import static ohm.Helpers.*;

public class OhmViewController implements Initializable {

    @FXML
    public Button startCameraButton;
    @FXML
    private MatrixLinePicker linePickerImageView;
    @FXML
    private LiveImageView processedImageView;
    @FXML
    private Slider thresholdSlider1;
    @FXML
    private Slider thresholdSlider2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final Image src = new Image("file:resources/10k.jpg");
        final Mat frame = Imgcodecs.imread("resources/10k.jpg");

        linePickerImageView.setImage(frame);
        linePickerImageView.setListener(new MatrixLinePicker.Listener() {
            @Override
            public void onLinePicked(Point p1, Point p2) {
                // When a line is picked we do this math that's been produced
                // by trial and error. This is subject to change, and will have
                // a clearer explanation when it's closer to done.

                double[][] sample = boxSample(frame,p1, p2,(int)dist(p1,p2),20);
                double[][] diff = diff(sample);
                System.out.println("Color Derrivate Magnitude for selected line");
                for (double[] term: rollingAverageFilter(diff,2)){
                    System.out.println(mag(term));
                }
            }
        });

        this.processedImageView.setRenderer(new LiveImageView.Rednderer() {
            @Override
            public Image render() {
                return src;
            }
        });


    }

    @FXML
    public void buttonClicked(ActionEvent actionEvent) {

    }
}
