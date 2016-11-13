package ohm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.util.Pair;
import ohm.ImageProcessing.BandReader;
import ohm.Input.ImageInput;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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

    private List<Point> points = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ImageInput imageInput = new ImageInput();
        final Image src = imageInput.getImage();
        final Mat frame = imageInput.getMat();

        linePickerImageView.setImage(frame);
        linePickerImageView.setListener(new MatrixLinePicker.Listener() {
            @Override
            public void onLinePicked(Point p1, Point p2) {points = BandReader.read(frame,p1,p2);
            }
        });

        this.processedImageView.setRenderer(new LiveImageView.Rednderer() {
            @Override
            public Image render() {
                Mat processed = frame.clone();
                for (Point p:points) {
                    Imgproc.circle(processed, p, 1, new Scalar(255, 0, 0, 255), 2);
                }
                return matToImage(processed);
            }
        });


    }

    @FXML
    public void buttonClicked(ActionEvent actionEvent) {

    }
}
