package ohm;

import org.opencv.core.*;
import org.opencv.imgcodecs.*;

/**
 * Created by Ryan on 19/09/2016.
 * This is a main class to verify that our build system works
 */
public class Main {
    public static void main(String... args){
        System.out.println("Hello, world");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        displayImage();
    }

    public static void displayImage(){
        Mat image = Imgcodecs.imread("/Resources/Resistor.jpg");
        System.out.println(image.toString());
    }
}
