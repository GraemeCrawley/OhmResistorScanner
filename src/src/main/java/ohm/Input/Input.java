package ohm.Input;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * @author Jonathan Brown
 * */
public interface Input {

    /**
     * Used to retrieve a matrix representation of the input (used by Opencv libraries).
     * @return Matrix representation of input.
     */
    public Mat getMat();

    /**
     * Used to retrieve a image representation of the input (used by JavaFX).
     * @return Image representation of input.
     */
    public Image getImage();
}

/** @} */