package ohm.Input;

import javafx.scene.image.Image;
import ohm.userinterface.OhmViewController;
import org.opencv.core.Mat;
import javafx.embed.swing.SwingFXUtils;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.opencv.imgproc.Imgproc;

/**
 * @addtogroup CameraInput
 * @{
 */

/**
 *
 * @brief Instances of this class are to be used to receive input from device camera. Currently unimplemented.
 * */
public class CameraInput implements Input {

    private VideoCapture capture = new VideoCapture(0);
    private boolean cameraOn = false;
    private int videodevice = 0;
    ImageInput img = new ImageInput();

    Image imageToShow= img.getImage();
    Mat frame = img.getRGB();


    public CameraInput(){
        System.out.println("Button was clicked");
        if(!cameraOn){
            this.capture.open(videodevice);
            if (capture.isOpened()){
                cameraOn = true;
                System.out.println("STOP INSIDE");
                Thread processFrame = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (cameraOn) {
                            System.out.println("Inside Thread");
                            try {
                                capture.read(frame);
                                System.out.println("read frame");
                                imageToShow =  matToImage(frame);
                                System.out.println("mat to image");
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
            System.out.println("STOP");
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

    @Override
    public Image getImage() {
        System.out.println(imageToShow);
        return imageToShow;
    }

    @Override
    public Mat getRGB() {
        System.out.println(frame);
        return frame;
    }

    @Override
    public Mat getLAB() {
        return null;
    }
}

/** @} */