package ohm;/**
 * Created by jon on 2016-09-20.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.Core;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Ohm extends Application {

    public static void main(String[] args) throws Exception{
        System.out.println("Hello, world");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println(getClass().toString());
        Parent root = FXMLLoader.load(getClass().getResource("OhmView.fxml"));
        primaryStage.setTitle("Ohm");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
