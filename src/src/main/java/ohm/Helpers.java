package ohm;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayInputStream;

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
     * @return
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
     * @return
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
     * @param samples
     * @return
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
     * @return
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
     * @param vect
     * @return
     */
    public static double mag(double[] vect){
        double sumComponentsSquared = 0;
        for (double component:vect) {
            sumComponentsSquared += component * component;
        }
        return Math.sqrt(sumComponentsSquared);
    }

    public static double dist(Point p1,Point p2){
        double deltaX = p1.x - p2.x;
        double deltaY = p1.y - p2.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }


}
