package sceneTesting;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class Scene1 {
    @FXML
    private BorderPane border;
    @FXML
    private AnchorPane anchorpri;

    @FXML
    private JFXButton btn;

    @FXML
    void next(ActionEvent event) {
        BorderPane borderpane=null;
        try {
            borderpane= FXMLLoader.load(getClass().getResource("/sceneTesting/scene2.fxml"));
            border.getChildren().setAll(borderpane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
