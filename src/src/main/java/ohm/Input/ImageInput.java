package ohm.Input;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * @addtogroup ImageInput
 * @{
 */

/**
 * @author Jonathan Brown
 * @brief A source of input data, uses static images.
 */
public class ImageInput implements Input {

    Image img;
    Mat mat;

    /**
     * Default constructor creates an instance using a default image.
     */
    public ImageInput(){
        img = new Image("file:resources/resistor-sample.jpg");
        mat = Imgcodecs.imread("resources/resistor-sample.jpg");
    }

    /**
     * Method returns a OpenCV Matrix of the loaded image.
     * @return OpenCV Matrix representation of the image.
     */
    @Override
    public Mat getMat() {
        return mat;
    }


    /**
     * Used to retrieve a image representation of the input (used by JavaFX).
     * @return Image representation of input.
     */
    @Override
    public Image getImage() {
        return img;
    }
}

/** @} */