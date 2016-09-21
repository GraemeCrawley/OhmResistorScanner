package ohm; /**
 * Created by jon on 2016-09-20.
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
//import org.opencv.core.Core;

public class OhmViewController{

    @FXML
    public void buttonClicked(ActionEvent actionEvent) {
        System.out.println("Button was clicked");
    }
}
