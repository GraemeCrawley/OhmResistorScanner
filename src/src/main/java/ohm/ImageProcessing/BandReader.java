package ohm.ImageProcessing;

import javafx.util.Pair;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @defgroup BandLocation
 * Module used to analyze the line of pixels selected by the user through the UI.
 * It uses high values of the differential of the RGB colours to detect edges of bands.
 * @addtogroup BandLocation
 * @{
 */

/**
 * @author Ryan Marks & Jonathan Brown
 * @brief Module used to analyze the line of pixels selected by the user through the UI.
 * It uses high values of the differential of the RGB colours to detect edges of bands.
 */
public class BandReader {

    /**
     * @param frame Image to Sample.
     * @param p1 Starting point of the sampling line.
     * @param p2 Ending point of the sampling line.
     * @return List of points along the line that are likely band edges.
     */
    public static List<Point> read(Mat frame, Point p1, Point p2){
        Mat img = frame.clone();
        Imgproc.medianBlur(img,img,13);
        double[][] sample = boxSample(img,p1, p2,(int)dist(p1,p2),20);
        double[][] diff = diff(sample);
        double[] terms = Arrays.stream(rollingAverageFilter(diff,1))
                .mapToDouble(d -> Math.log(1+mag(d)))
                .toArray();
        double[] groupedTerms = groupTerms(terms,3);
        Arrays.stream(groupedTerms).forEach(d -> System.out.println(d));

        List<Pair<Integer, Double>> localMaxima = findLocalMaxima(groupedTerms);

        return localMaxima.stream().map(pair -> onLine(p1,p2,pair.getKey()*1.0/groupedTerms.length))
                .collect(Collectors.toList());
    }

    /**
     * This method takes something Ryan made up called a box sample.
     * At nSamples points along the line from start to end,
     * a line of samples are take along a line of length boxWidth,
     * normal to and centred on the sampling line
     * @param mat The matrix being sampled
     * @param start The start of the sampling line
     * @param end The end of the sampling line
     * @param nSamples The number or linear samples to take along the sampling line
     * @param boxWidth the length of the normal sampling line.
     * @return An array containing the average rgb values (represented as double[]) in the sampling window.
     */
    public static double[][] boxSample(Mat mat, Point start, Point end, int nSamples, double boxWidth){
        // Both points must be in the bounds of the matrix
        assert (mat.height() > start.y);
        assert (mat.height() > end.y);
        assert (mat.width() > start.x);
        assert (mat.width() > end.x);

        double dx = end.x - start.x;
        double dy = end.y - start.y;
        double len = Math.sqrt(dx*dx+dy*dy);

        double normX = (boxWidth / 2)  * (-dy / len);
        double normY = (boxWidth / 2) * ( dx / len);


        double[][] samples = new double[nSamples][];

        for (int n = 0; n < nSamples; n++){
            int centerX = (int) Math.floor(n * dx / nSamples + start.x);
            int centerY = (int) Math.floor(n * dy / nSamples + start.y);
            Point samplingStart = new Point(centerX+normX,centerY+normY);
            Point samplingEnd = new Point(centerX-normX,centerY-normY);

            samples[n] = componentwiseMean(sample(mat,samplingStart,samplingEnd,(int) boxWidth));
        }

        return samples;
    }

    /**
     * This method is used to apply a rolling average filter to vector data.
     * @param sample An array of ( arrays of doubles representing an individual vector sample)
     * @param windowRadius The number of samples on either side of a given sample to incorporate into the average
     * @return An array containing the average RGB value sampled along the line.
     */
    public static double[][] rollingAverageFilter(double[][] sample, int windowRadius){
        int windowWidth = 1 + windowRadius;
        double[][] filtered = new double[sample.length][];
        for (int n = 0; n < sample.length; n++){
            double[] windowSample = new double[sample[n].length];
            for (int i = 0; i < sample[n].length; i++){
                int value = 0;
                for (int s = -windowRadius; s <= windowRadius; s++){
                    int samplingPosition = n + s;
                    if (samplingPosition < 0 ){
                        samplingPosition = 0;
                    }else if(samplingPosition >= sample.length){
                        samplingPosition = sample.length - 1;
                    }
                    value +=  sample[samplingPosition][i] / windowWidth;
                }
                windowSample[i] = value;
            }
            filtered[n] = windowSample;
        }
        return filtered;
    }


    /**
     * Calculate the mean of an array of vectors.
     * @param samples Array of RGB vectors represented as double[]
     * @return The average of all vectors within the input array (represented as a double[])
     */
    public static double[] componentwiseMean(double[][] samples){
        double[] mean = new double[samples[0].length];

        for (int i = 0; i < mean.length; i++){
            mean[i] = 0;
        }

        for (double[] sample:samples) {
            for (int i = 0; i < mean.length; i++){
                mean[i] += sample[i]/samples.length;
            }
        }

        return mean;
    }

    /**
     * Take a linear sampling from a matrix at a given number of points along a line
     * @param mat The matrix to be sampled
     * @param start The start of the sampling line
     * @param end The end of the sampling line
     * @param nSamples The number of samples to take along the line.
     * @return The samples taken
     */
    public static double[][] sample(Mat mat, Point start, Point end, int nSamples){
        // Both points must be in the bounds of the matrix
        assert (mat.height() > start.y);
        assert (mat.height() > end.y);
        assert (mat.width() > start.x);
        assert (mat.width() > end.x);

        double dx = end.x - start.x;
        double dy = end.y - start.y;


        double[][] samples = new double[nSamples][];

        for (int n = 0; n < nSamples; n++){
            int sampleX = (int) Math.floor(n * dx / nSamples + start.x);
            int sampleY = (int) Math.floor(n * dy / nSamples + start.y);
            samples[n] = mat.get(sampleY,sampleX);
        }

        return samples;
    }

    /**
     * Take the discrete derivative of a series of vectors.
     * @param y A series of vectors
     * @return y', the derivative of the input represented as a array of vectors (double[]).
     */
    public static double[][] diff (double[][] y){
        double[][] result = new double[y.length - 1][];

        for (int n = 0; n < result.length; n++){
            double[] difference = new double[y[n].length];
            for (int i = 0; i < difference.length; i++) {
                difference[i] = y[n+1][i] - y[n][i];
            }
            result[n] = difference;
        }

        return result;
    }

    /**
     * Calculate the magnitude of a given vector
     * @param vect Input vector
     * @return The magnitude of the input vector
     */
    public static double abmag(double[] vect){
        double sumComponentsSquared = 0;
        for (int i = 1; i < 3; i ++) {
            double component = vect[i];
            sumComponentsSquared += component * component;
        }
        return Math.sqrt(sumComponentsSquared);
    }

    /**
     * Calculate the magnitude of a given vector
     * @param vect Input vector
     * @return The magnitude of the input vector
     */
    public static double mag(double[] vect){
        double sumComponentsSquared = 0;
        for (double component:vect) {
            sumComponentsSquared += component * component;
        }
        return Math.sqrt(sumComponentsSquared);
    }

    /**
     * Calcualte the distance between two points.
     * @param p1 Point 1.
     * @param p2 Point 2.
     * @return Distance between p1 and p2.
     */
    public static double dist(Point p1,Point p2){
        double deltaX = p1.x - p2.x;
        double deltaY = p1.y - p2.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public static double[] groupTerms(double[] terms, int binSize){
        double[] groupedTerms = new double[(terms.length+binSize-1)/binSize];
        for (int i = 0; i < terms.length; i++){
            int n = i / binSize;
            if (i % binSize == 0){
                groupedTerms[n] = terms[i];
            }else{
                groupedTerms[n] += terms[i];
            }
        }
        return groupedTerms;
    }

    public static List<Pair<Integer,Double>> findLocalMaxima(double[] values){
        List<Pair<Integer,Double>> indexValuePairs= new ArrayList<>(10);
        for(int i = 1; i < values.length - 1; i++){
            if (values[i] > values[i-1] && values[i] > values[i+1]){
                indexValuePairs.add(new Pair<>(i,values[i]));
            }
        }
        return indexValuePairs;
    }

    public static List<Pair<Integer,Double>> findGlobalMaxima (double[] values, int nMaxima){
        List<Pair<Integer,Double>> indexValuePairs= new ArrayList<>(values.length);
        for (int i = 0; i < values.length; i++){
            indexValuePairs.add(new Pair<>(i,values[i]));
        }
        List<Pair<Integer,Double>> sortedPairs = indexValuePairs.stream()
                .sorted((o1, o2) -> o1.getValue().compareTo(o2.getValue()))
                .collect(Collectors.toList())
                .subList(values.length-nMaxima,values.length);
        return sortedPairs;
    }

    public static Point onLine(Point start, Point end, double fraction){
        return new Point(start.x+(end.x-start.x)*fraction,start.y+(end.y-start.y)*fraction);
    }
}

/** @} */