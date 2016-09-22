package ohm; /**
 * Created by jon on 2016-09-20.
 */

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import java.net.URL;
import java.util.ResourceBundle;
import org.opencv.imgproc.*;

import static ohm.Helpers.matToImage;
import static org.opencv.imgproc.Imgproc.*;

public class OhmViewController implements Initializable {

    @FXML
    public Button startCameraButton;

    @FXML
    private ImageView unprocessedImageView;

    @FXML
    private ImageView processedImageView;



    private VideoCapture capture = new VideoCapture();
    private boolean cameraOn = false;
    private int videodevice = 0;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.unprocessedImageView.setImage(new Image("file:resources/Resistor.jpg"));
        this.processedImageView.setImage(new Image("file:resources/Resistor.jpg"));
    }

    @FXML
    public void buttonClicked(ActionEvent actionEvent) {
        System.out.println("Button was clicked");
        if(!cameraOn){
            this.capture.open(videodevice);
            if (capture.isOpened()){
                cameraOn = true;
                startCameraButton.setText("Stop");

                Thread processFrame = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (cameraOn) {
                            try {
                                Mat frame = new Mat();
                                capture.read(frame);
                                Mat cannyEdge = new Mat();
                                blur(frame,cannyEdge,new Size(2,2));
                                cvtColor(cannyEdge,cannyEdge,COLOR_BGR2GRAY);
                                Canny(cannyEdge,cannyEdge,10, 100);
                                Image processedToShow = matToImage(cannyEdge);
                                Image imageToShow =  matToImage(frame);
                                unprocessedImageView.setImage(imageToShow);
                                processedImageView.setImage(processedToShow);
                            } catch (Exception e1) {
                                System.out.println("Error on Update " + e1);
                            }
                            try {
                             Thread.sleep(10);
                            } catch (InterruptedException e) {
                            e.printStackTrace();
                            }

                        }
                        System.out.println("Thread processFrame closed");
                        capture.release();
                    }
                });
                processFrame.start();
            }
        }
        else {
            startCameraButton.setText("Start");
            cameraOn = false;
        }
    }

    public void stop(){
        cameraOn = false;
    }
}
