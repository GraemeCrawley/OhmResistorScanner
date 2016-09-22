package ohm;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by jon on 2016-09-21.
 */
public class Helpers {

    public static Image matToImage(Mat frame) throws Exception{
        MatOfByte bytemat = new MatOfByte();
        Imgcodecs.imencode(".jpg", frame, bytemat);
        byte[] bytes = bytemat.toArray();
        InputStream in = new ByteArrayInputStream(bytes);
        BufferedImage img = ImageIO.read(in);
        return SwingFXUtils.toFXImage(img, null);
    }


}
