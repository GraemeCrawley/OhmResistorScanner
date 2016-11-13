package ohm;

import javafx.scene.image.Image;
import javafx.util.Pair;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jon on 2016-09-21.
 */
public class Helpers {

    public static Image matToImage(Mat mat) {
        try {
            MatOfByte byteMat = new MatOfByte();
            Imgcodecs.imencode(".bmp", mat, byteMat);
            return new Image(new ByteArrayInputStream(byteMat.toArray()));
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}
