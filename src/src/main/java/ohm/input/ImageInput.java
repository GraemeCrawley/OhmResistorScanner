package ohm.input;

import javafx.scene.image.Image;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;

import java.util.List;
import java.util.Vector;

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
    Mat rgb;
    Mat lab;

    /**
     * Default constructor creates an instance using a default image.
     */
    public ImageInput(){
        img = new Image("file:resources/resistor-sample.jpg");
        rgb = Imgcodecs.imread("resources/resistor-sample.jpg");
        Imgproc.resize(rgb, rgb,new Size(576,360));
        Imgproc.cvtColor(rgb,rgb,Imgproc.COLOR_BGR2RGB);
        lab = rgb.clone();
        Imgproc.cvtColor(rgb,lab,Imgproc.COLOR_RGB2Lab);
    }


    /**
     * Constructor featuring a name parameter.
     * @param name The name of the input image. Do not include an extension.
     */
    public ImageInput(String name){
        img = new Image("file:resources/" + name + ".jpg");
        rgb = Imgcodecs.imread("resources/" + name + ".jpg");
        Imgproc.resize(rgb, rgb,new Size(576,360));
        Mat temp = rgb.clone();
        Imgproc.medianBlur(rgb,temp,15);
        Imgcodecs.imwrite("resources/" + name + "-blurred.jpg",temp);
        Imgproc.cvtColor(rgb,rgb,Imgproc.COLOR_BGR2RGB);
        lab = rgb.clone();
        Imgproc.cvtColor(rgb,lab,Imgproc.COLOR_RGB2Lab);
        Mat img_hist_equalized = new Mat();
        Imgproc.cvtColor(rgb,img_hist_equalized,Imgproc.COLOR_RGB2Lab);
        List<Mat> matList = new Vector<Mat>();
        Core.split(img_hist_equalized,matList);

        CLAHE clahe = Imgproc.createCLAHE();
        clahe.setClipLimit(1);
        Mat dst = new Mat();
        clahe.apply(matList.get(0),dst);
        dst.copyTo(matList.get(0));
        Core.merge(matList,img_hist_equalized);
        Imgproc.cvtColor(img_hist_equalized,rgb,Imgproc.COLOR_Lab2RGB);
    }

    /**
     * Method returns a OpenCV Matrix of the loaded image.
     * @return OpenCV Matrix representation of the image.
     */
    @Override
    public Mat getRGB() {
        return rgb;
    }


    /**
     * Used to retrieve a image representation of the input (used by JavaFX).
     * @return Image representation of input.
     */
    @Override
    public Image getImage() {
        return img;
    }

    @Override
    public Mat getLAB() { return lab; }
}

/** @} */