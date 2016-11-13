package ohm.Input;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * Created by jon on 2016-11-13.
 */
public class ImageInput implements Input {

    Image img;
    Mat mat;

    public ImageInput(){
        img = new Image("file:resources/1k.jpg");
        mat = Imgcodecs.imread("resources/1k.jpg");
    }


    @Override
    public Mat getMat() {
        return mat;
    }

    @Override
    public Image getImage() {
        return img;
    }
}
