package ohm;/**
 * Created by jon on 2016-09-20.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ohm.userinterface.OhmViewController;
import org.opencv.core.Core;

import java.net.URL;

public class Ohm extends Application {

    private FXMLLoader fxmlLoader;

    //Loads opencv and hands control over to OhmViewController
    public static void main(String[] args) throws Exception{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Required for creation of view and controller.
        URL location = getClass().getResource("userinterface/OhmView.fxml");
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = fxmlLoader.load(location.openStream());
        primaryStage.setTitle("Ohm");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /*
    //Method responsible for safely shutting down the application
    @Override
    public void stop() {
        ((OhmViewController) fxmlLoader.getController()).stop();
    }*/
}
