package ca.ryanmarks.ohm.ValueIdentification;


/**
 * @defgroup ColourMapping
 * @author Jonathan Brown
 *@{
 */

import au.com.bytecode.opencsv.CSVReader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.ml.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;


/**
 * @brief Enum containing all of the possible colours that a resistor can take on. Also features member functions used to map the colours of bands to values used in the calculation process.
 */
public enum ResistorColour {
    BLACK(0),
    BROWN(1),
    RED(2),
    ORANGE(3),
    YELLOW(4),
    GREEN(5),
    BLUE(6),
    VIOLET(7),
    GREY(8),
    WHITE(9),
    GOLD(11),
    //SILVER(),
    BASE(10);
    int value;
    static KNearest KNN;
    static DTrees dt;

    public static void train(Reader csvTrainingSet) {
        try {
            CSVReader reader = new CSVReader(csvTrainingSet);
            List values = reader.readAll();
            List trainValues = values.subList(1,values.size());
            String[] row = (String []) trainValues.get(0);
            Mat trainSamples = new Mat(trainValues.size(), row.length-1, CvType.CV_32FC1);
            Mat trainResponses = new Mat(trainValues.size(), 1, CvType.CV_32SC1);
            createSamples(trainValues,trainSamples);
            createResults(trainValues,trainResponses);
            KNN = KNearest.create();
            KNN.train(trainSamples,0,trainResponses);
            //dt = DTrees.create();
            //dt.train(trainSamples,0,trainResponses);
//            System.out.println(dt.isTrained());
            //          System.out.println(dt.isClassifier());

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ResistorColour fromCode(int i) {
        switch (i) {
            case 0:
                return BLACK;
            case 1:
                return BROWN;
            case 2:
                return RED;
            case 3:
                return ORANGE;
            case 4:
                return YELLOW;
            case 5:
                return GREEN;
            case 6:
                return BLUE;
            case 7:
                return VIOLET;
            case 8:
                return GREY;
            case 9:
                return WHITE;
            case 10:
                return BASE;
            case 11:
                return GOLD;
            default:
                throw new RuntimeException();
        }
    }

    private static Integer getIntegerRepresentation(String color) {
        switch (color) {
            case "BROWN":
                return 1;
            case "RED":
                return 2;
            case "ORANGE":
                return 3;
            case "YELLOW":
                return 4;
            case "GREEN":
                return 5;
            case "BLUE":
                return 6;
            case "VIOLET":
                return 7;
            case "GREY":
                return 8;
            case "WHITE":
                return 9;
            case "BLACK":
                return 0;
            case "BASE":
                return 10;
            case "GOLD":
                return 11;
            case "SILVER":
                return 12;
            default:
                return null;
        }
    }

    private static void createSamples(List values, Mat samples){
        int i = 0;
        for(Object value: values){
            String[] array = (String[]) value;
            float[] lab = new float[3];
            float[] rgb = new float[3];
            for (int j = 0; j < array.length-1; j++){
                rgb[j] = Float.parseFloat(array[j]);
            }
            rgb2lab(rgb[0],rgb[1],rgb[2],lab);
            for(int j = 0; j < array.length-1; j++){
                samples.put(i,j,new float[] {lab[j]});
            }
            i++;
        }
    }

    private static void createResults(List values, Mat results){
        int i = 0;
        for(Object value: values){
            String[] array = (String[]) value;
            results.put(i,0, new int[] {getIntegerRepresentation(array[array.length-1])});
            i++;
        }
    }

    /**
     *
     * @param v The number represented by the colour in the calculation of the resistor's ohmage.
     */
    ResistorColour(int v){
        value = v;
    }


    /**
     * //http://www.brucelindbloom.com
     * @param R
     * @param G
     * @param B
     * @param lab
     */
    public static void rgb2lab(float R, float G, float B, float[] lab) {


        float r, g, b, X, Y, Z, fx, fy, fz, xr, yr, zr;
        float Ls, as, bs;
        float eps = 216.f/24389.f;
        float k = 24389.f/27.f;

        float Xr = 0.964221f;  // reference white D50
        float Yr = 1.0f;
        float Zr = 0.825211f;

        // RGB to XYZ
        r = R/255.f; //R 0..1
        g = G/255.f; //G 0..1
        b = B/255.f; //B 0..1

        // assuming sRGB (D65)
        if (r <= 0.04045)
            r = r/12;
        else
            r = (float) Math.pow((r+0.055)/1.055,2.4);

        if (g <= 0.04045)
            g = g/12;
        else
            g = (float) Math.pow((g+0.055)/1.055,2.4);

        if (b <= 0.04045)
            b = b/12;
        else
            b = (float) Math.pow((b+0.055)/1.055,2.4);


        X =  0.436052025f*r     + 0.385081593f*g + 0.143087414f *b;
        Y =  0.222491598f*r     + 0.71688606f *g + 0.060621486f *b;
        Z =  0.013929122f*r     + 0.097097002f*g + 0.71418547f  *b;

        // XYZ to Lab
        xr = X/Xr;
        yr = Y/Yr;
        zr = Z/Zr;

        if ( xr > eps )
            fx =  (float) Math.pow(xr, 1/3.);
        else
            fx = (float) ((k * xr + 16.) / 116.);

        if ( yr > eps )
            fy =  (float) Math.pow(yr, 1/3.);
        else
            fy = (float) ((k * yr + 16.) / 116.);

        if ( zr > eps )
            fz =  (float) Math.pow(zr, 1/3.);
        else
            fz = (float) ((k * zr + 16.) / 116);

        Ls = ( 116 * fy ) - 16;
        as = 500*(fx-fy);
        bs = 200*(fy-fz);

        lab[0] = (float) (2.55*Ls + .5);
        lab[1] = (float) (as + .5) + 128;
        lab[2] = (float) (bs + .5) + 128;
    }


    public static int fit(float r, float g, float b){
        float[] rgb = new float[] {r,g,b};
        float[] lab = new float[3];
        rgb2lab(r,g,b,lab);
        Mat testSample = new MatOfFloat(lab).t();
        Mat result = new Mat(1,1,CvType.CV_32SC1);
        KNN.findNearest(testSample,1,result);
        //dt.predict(testSample,result,0);
        return (int) result.get(0,0)[0];
    }

    /**
     * Function takes in a sampled colour from the images and attempts to fitOld it to the closest known colour a resistor can possess.
     * @param r The red colour value of the colour to be fitOld.
     * @param g The green colour value of the colour to be fitOld.
     * @param b The blue colour value of the colour to be fitOld.
     * @return The known colour that best represents the sampled colour.
     */
    public static int fit(int r, int g, int b, int colorSpace){
        return fit((float) r, (float) g, (float) b);
    }


    /**
     * Function takes in a sampled colour from the images and attempts to fitOld it to the closest known colour a resistor can possess.
     * @param r The red colour value of the colour to be fitOld.
     * @param g The green colour value of the colour to be fitOld.
     * @param b The blue colour value of the colour to be fitOld.
     * @return The known colour that best represents the sampled colour.
     */
    public static int fit(double r, double g, double b, int colorSpace){
        return fit((float) r, (float) g, (float) b);
    }

    public static void main(String[] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println(fit(243,68,46));
    }


}

/** @} */