package ohm; /**
 * Created by jon on 2016-09-20.
 */

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class OhmViewController implements Initializable {

    @FXML
    public Button startCameraButton;

    @FXML
    private ImageView mainImageView;



    private VideoCapture capture = new VideoCapture();
    private boolean cameraOn = false;
    private int videodevice = 0;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.mainImageView.setImage(new Image("file:resources/Resistor.jpg"));
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
                                Image imageToShow =  matToImage(frame);
                                mainImageView.setImage(imageToShow);
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
                        try {
                            capture.release();
                        } catch (Exception e) {
                        }
                    }
                });
                processFrame.start();
            }
        }
        else {
            startCameraButton.setText("Stop");
            cameraOn = false;
        }
    }

    private Image matToImage(Mat frame) throws Exception{
        MatOfByte bytemat = new MatOfByte();
        Imgcodecs.imencode(".jpg", frame, bytemat);
        byte[] bytes = bytemat.toArray();
        InputStream in = new ByteArrayInputStream(bytes);
        BufferedImage img = ImageIO.read(in);
        return SwingFXUtils.toFXImage(img, null);
    }


}
