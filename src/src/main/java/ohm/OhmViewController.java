package ohm; /**
 * Created by jon on 2016-09-20.
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.util.Pair;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
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
        final Image src = new Image("file:resources/1k.jpg");
        final Mat frame = Imgcodecs.imread("resources/1k.jpg");

        linePickerImageView.setImage(frame);
        linePickerImageView.setListener(new MatrixLinePicker.Listener() {
            @Override
            public void onLinePicked(Point p1, Point p2) {
                // When a line is picked we do this math that's been produced
                // by trial and error. This is subject to change, and will have
                // a clearer explanation when it's closer to done.

                double[][] sample = boxSample(frame,p1, p2,(int)dist(p1,p2),40);
                double[][] diff = diff(sample);
                System.out.println("Color Derrivate Magnitude for selected line");
                double[] terms = Arrays.stream(rollingAverageFilter(diff,2))
                        .mapToDouble(d -> Math.log(1+Helpers.mag(d)))
                        .toArray();
                double[] groupedTerms = Helpers.groupTerms(terms,3);
                Arrays.stream(groupedTerms).forEach(d -> System.out.println(d));

                List<Pair<Integer, Double>> localMaxima = findLocalMaxima(groupedTerms);
                points = localMaxima.stream().map(pair -> onLine(p1,p2,pair.getKey()*1.0/groupedTerms.length)).collect(Collectors.toList());
            }
        });

        this.processedImageView.setRenderer(new LiveImageView.Renderer() {
            @Override
            public Image render() {
                Mat processed = frame.clone();
                int i = 0;
                double m;
                double n;
                Point temp = null;
                Point a;
                for (Point p:points) {
                    System.out.println("Point value: " + p.x + " " + p.y);
                    Imgproc.circle(processed, p, 1, new Scalar(255,0,0,255), 2);
                    System.out.println("I: " + i);
                    if (i % 2 != 0) {
                        //Getting average between two points
                        m = (temp.x + p.x)/2;
                        n = (temp.y + p.y)/2;
                        a = new Point(m,n);
                        System.out.println("Point a value: " + a.x + " " + a.y);
                        System.out.println("COLOR: " + processed.get((int)m,(int)n));
                        //Centerpoint
                        Imgproc.circle(processed, a, 1, new Scalar(0,0,0,0), 6);

                        //Color
                        Imgproc.circle(processed, a, 5, new Scalar(processed.get((int)m,(int)n)), 3);
                    }
                    else{
                        temp = p;
                    }
                    i++;
                }
                return matToImage(processed);
            }
        });


    }

    @FXML
    public void buttonClicked(ActionEvent actionEvent) {

    }
}
