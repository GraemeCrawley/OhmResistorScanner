package ohm.Input;

import javafx.scene.image.Image;
import org.opencv.core.Mat;

/**
 * @addtogroup CameraInput
 * @{
 */

/**
 *
 * @brief Instances of this class are to be used to receive input from device camera. Currently unimplemented.
 * */
public class CameraInput implements Input {

    @Override
    public Mat getRGB() {
        return null;
    }

    @Override
    public Image getImage() {
        return null;
    }

    @Override
    public Mat getLAB() {
        return null;
    }
}

/** @} */