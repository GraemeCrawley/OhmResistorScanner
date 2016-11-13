package ohm.Input;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * Created by jon on 2016-11-13.
 */
public interface Input {


    public Mat getMat();
    public Image getImage();



}
