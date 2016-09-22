package ohm;/**
 * Created by jon on 2016-09-20.
 */

import com.sun.deploy.uitoolkit.impl.fx.ui.FXSSV3Dialog;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.Core;

import java.net.URL;

public class Ohm extends Application {

    private FXMLLoader fxmlLoader;

    public static void main(String[] args) throws Exception{
        System.out.println("Hello, world");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL location = getClass().getResource("OhmView.fxml");
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = fxmlLoader.load(location.openStream());
        primaryStage.setTitle("Ohm");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() {
         ((OhmViewController) fxmlLoader.getController()).stop();
    }
}
