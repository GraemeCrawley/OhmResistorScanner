package ohm.userinterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import ohm.ValueIdentification.ResistorColour;
import ohm.ImageProcessing.BandReader;
import ohm.Input.ImageInput;
import ohm.Input.Input;
import ohm.ValueIdentification.ValueCalculator;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;

import java.awt.color.ColorSpace;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import static ohm.Helpers.*;

/**
 * @addtogroup UserInterface
 * @{
 */


/**
 * @brief Coordinates the algorithms present in the model with user input through the view.
 */
public class OhmViewController implements Initializable {

    @FXML
    public Button startCameraButton;
    @FXML
    private ResistorAxisPickerView resistorAxisPickerView;
    @FXML
    private LiveImageView processedImageView;
    @FXML
    private Slider thresholdSlider1;
    @FXML
    private Slider thresholdSlider2;

    private List<Point> points = new ArrayList<>();

    @Override
    /**
     * Method used to "glue" together the front and back end of the application.
     */
    public void initialize(URL url, ResourceBundle rb) {
        Input imageInput = new ImageInput("color_data/M8/Screenshot_2016-11-27-15-19-56");
        final Image src = imageInput.getImage();
        final Mat rgbframe = imageInput.getRGB();
        final Mat labframe = imageInput.getLAB();

        resistorAxisPickerView.setImage(rgbframe);
        resistorAxisPickerView.setListener((p1, p2) -> points = BandReader.read(rgbframe,p1,p2));





        this.processedImageView.setRenderer(() -> {
            /*
            Mat processed = rgbframe.clone();
            ArrayList<Integer> values = new ArrayList<Integer>();
            for (int i = 0; i < Math.min(points.size()-1, 7); i = i + 2){
                Point p1 = points.get(i);
                Point p2 = points.get(i+1);
                Point midpoint = new Point((p1.x + p2.x)/2, (p1.y + p2.y)/2);
                Scalar colourAtMidpoint = new Scalar(rgbframe.get((int) midpoint.y, (int) midpoint.x));
                int resistorColourAtMidpoint = ResistorColour
                        .fit(colourAtMidpoint.val[0], colourAtMidpoint.val[1],
                                colourAtMidpoint.val[2], ColorSpace.TYPE_Lab);
                Scalar rgbAtMidpoint = new Scalar(processed.get((int) midpoint.y, (int) midpoint.x));
                Imgproc.circle(processed, p1, 1, new Scalar(0, 0, 255), 2);
                Imgproc.circle(processed, p2, 1, new Scalar(0, 0, 255), 2);
                Imgproc.circle(processed, midpoint, 10, rgbAtMidpoint, 2);
                Imgproc.putText(processed,
                        Integer.toString(resistorColourAtMidpoint),p1,1,1,new Scalar(255,255,255));
                values.add(resistorColourAtMidpoint);


               /* Mat labprocessed = processed.clone();
                Imgproc.cvtColor(processed, labprocessed, Imgproc.COLOR_RGB2Lab);
                for(int j = 0; j < labprocessed.cols(); j ++){
                    for (int k = 0; k < labprocessed.rows(); k++){
                        double[] toPut = labprocessed.get(k,j);
                        toPut[0] = 128;
                        labprocessed.put(k,j,toPut);
                    }
                }
                Imgproc.cvtColor(labprocessed, processed, Imgproc.COLOR_Lab2RGB);
            }
            if (values.size() == 4){
                ValueCalculator vc = new ValueCalculator(values.get(0),values.get(1), values.get(2),values.get(3));
                Imgproc.putText(processed,vc.getValue(), new Point(0,340),1,3,new Scalar(0,0,0));
            } */

            Mat processed = rgbframe.clone();
            //Imgproc.medianBlur(processed,processed,15);
            Mat img_hist_equalized = new Mat();
            Imgproc.cvtColor(processed,img_hist_equalized,Imgproc.COLOR_RGB2Lab);
            List<Mat> matList = new Vector<Mat>();
            Core.split(img_hist_equalized,matList);

            CLAHE clahe = Imgproc.createCLAHE();
            clahe.setClipLimit(1);
            Mat dst = new Mat();
            clahe.apply(matList.get(0),dst);
            dst.copyTo(matList.get(0));

            //Imgproc.equalizeHist(matList.get(0),matList.get(0));

            Core.merge(matList,img_hist_equalized);
            Imgproc.cvtColor(img_hist_equalized,processed,Imgproc.COLOR_Lab2RGB);

            /*
            Mat kernel = Mat.ones(40,1, CvType.CV_32FC1);
            Core.multiply( kernel, new Scalar(1.0/40), kernel );
            Imgproc.filter2D(processed, processed, -1, kernel,new Point(-1,-1), 0);*/

            //Imgproc.boxFilter(processed,processed,-1,new Size(3,15));


            Point left = new Point(processed.width() / 3, processed.height() / 2);
            Point right = new Point(processed.width() * 2 / 3, processed.height() / 2);
            Mat temp =new Mat();
            //Imgproc.medianBlur(processed,processed,9);

            Imgproc.cvtColor(processed,temp, Imgproc.COLOR_RGB2BGR);
            Imgcodecs.imwrite("resources/" + "output.jpg",temp);


            for (int i = 0; i < processed.height(); i++){
                for (int j = 0; j < processed.width(); j++){
                    double[] point = processed.get(i,j);
                    int color = ResistorColour.fit((float) point[0],(float)point[1],(float)point[2]);
                    if (color != 0)
                        processed.put(i,j,getColorFromCode(color));

                }
            }


            ArrayList<ResistorColour> values = new ArrayList<ResistorColour>();

            //Imgproc.line(processed, left, right, new Scalar(255, 0, 0), 1);

            return matToImage(processed);
        });





    }

    private double[] getColorFromCode(int id){
        switch (id){
            case 0:
                return new double[] {10,10,10};
            case 1:
                return new double[] {165,42,42};
            case 2:
                return new double[] {255,0,0};
            case 3:
                return new double[] {255,69,0};
            case 4:
                return new double[] {255,255,0};
            case 5:
                return new double[] {0,255,0};
            case 6:
                return new double[] {0,0,255};
            case 7:
                return new double[] {255,0,255};
            case 8:
                return new double[] {100,0,100};
            case 9:
                return new double[] {255,255,255};
            case 10:
                return new double[] {217,163,108};
            case 11:
                return new double[] {128,128,50};
            default:
                return new double[] {120,120,120};
        }
    }


    @FXML
    public void buttonClicked(ActionEvent actionEvent) {

    }
}

/** @}*/
