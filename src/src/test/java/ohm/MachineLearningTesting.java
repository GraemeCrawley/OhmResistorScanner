package ohm;

import au.com.bytecode.opencsv.CSVReader;
import org.opencv.core.*;
import org.opencv.ml.ANN_MLP;
import org.opencv.ml.KNearest;
import org.opencv.ml.Ml;
import org.opencv.ml.NormalBayesClassifier;

import java.io.FileReader;
import java.util.List;

/**
 * Class used test the accuracy of various machine learning algorithms. Not part of a module used by the application.
 */
public class MachineLearningTesting {

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        try{
            CSVReader reader = new CSVReader(new FileReader("resources/color_data/train.csv"));
            List values = reader.readAll();
            List trainValues = values.subList(1,values.size());
            String[] row = (String []) trainValues.get(0);
            Mat trainSamples = new Mat(trainValues.size(), row.length-1, CvType.CV_32FC1);
            Mat trainResponses = new Mat(trainValues.size(), 1, CvType.CV_32SC1);
            createSamples(trainValues,trainSamples);
            createResults(trainValues,trainResponses);
            KNearest ml = KNearest.create();
            ml.train(trainSamples,0,trainResponses);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void mainOld(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Hello world!");

        try{
            CSVReader reader = new CSVReader(new FileReader("resources/color_data/train.csv"));
            List values = reader.readAll();
            List trainValues = values.subList(1,values.size());
            reader = new CSVReader(new FileReader("resources/color_data/test.csv"));
            values = reader.readAll();
            List testValues = values.subList(1,values.size());
            String[] row = (String []) testValues.get(0);
            Mat trainSamples = new Mat(trainValues.size(), row.length-1, CvType.CV_32FC1);
            Mat trainResponses = new Mat(trainValues.size(), 1, CvType.CV_32SC1);
            Mat testSamples = new Mat(testValues.size(), row.length-1, CvType.CV_32FC1);
            Mat testAnswers = new Mat(testValues.size(), 1, CvType.CV_32SC1);
            Mat testResponses = new Mat(testValues.size(), 1, CvType.CV_32SC1);
            createSamples(trainValues,trainSamples);
            createResults(trainValues,trainResponses);
            createSamples(testValues,testSamples);
            createResults(testValues,testAnswers);


            for(int i = 0; i < trainSamples.rows(); i++){
                for (int j = 0; j < trainSamples.cols(); j ++){
                    System.out.print(trainSamples.get(i,j)[0] + "   ");
                }
                for (int j = 0; j < trainResponses.cols(); j ++){
                    System.out.print(trainResponses.get(i,j)[0] + "   ");
                }
                System.out.println();
            }

            KNearest ml = KNearest.create();
            System.out.println("train samples has " + trainSamples.rows() + " rows. train responses has " + trainResponses.rows());
            System.out.println("Trained!");
            ml.train(trainSamples,0,trainResponses);
            ml.findNearest(testSamples,3,testResponses);
            System.out.println("Predicted");
            //ml.
            double numCorrect = 0;
            double totalNum = 0;
            for(int i = 0; i < testResponses.rows(); i++){
                double answer = testAnswers.get(i,0)[0];
                double response = testResponses.get(i,0)[0];
                System.out.println(answer + " : " + response);

                if (answer == response) numCorrect++;
                totalNum++;
            }
            System.out.println(numCorrect/totalNum);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void createSamples(List values, Mat samples){
        int i = 0;
        for(Object value: values){
            String[] array = (String[]) value;
            for(int j = 0; j < array.length-1; j++){
                samples.put(i,j,new float[] {Float.parseFloat(array[j])});
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

    private static void createResultsForNN(List values, Mat response){
        for(Object value: values){
            String[] array = (String[]) value;
            response.push_back(new MatOfFloat(getFloatForNN(array[array.length-1])).t());
        }
    }

    private static void createResultsFloat(List values, Mat results){
        int i = 0;
        for(Object value: values){
            String[] array = (String[]) value;
            results.put(i,0, new float[] {(float) getIntegerRepresentation(array[array.length-1])});
            i++;
        }
    }

    @SuppressWarnings("Duplicates")
    private static Integer getIntegerRepresentation(String color){
        switch (color){
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

    private static float[] getFloatForNN(String color){
        float[] retVal = new float[13];
        switch (color){
            case "BROWN":
                retVal[1] = 1f;
                break;
            case "RED":
                retVal[2] = 1f;
                break;
            case "ORANGE":
                retVal[3] = 1f;
                break;
            case "YELLOW":
                retVal[4] = 1f;
                break;
            case "GREEN":
                retVal[5] = 1f;
                break;
            case "BLUE":
                retVal[6] = 1f;
                break;
            case "VIOLET":
                retVal[7] = 1f;
                break;
            case "GREY":
                retVal[8] = 1f;
                break;
            case "WHITE":
                retVal[9] = 1f;
                break;
            case "BLACK":
                retVal[0] = 1f;
                break;
            case "BASE":
                retVal[10] = 1f;
                break;
            case "GOLD":
                retVal[11] = 1f;
                break;
            case "SILVER":
                retVal[12] = 1f;
                break;
            default:
                break;
        }
        return retVal;
    }
}
