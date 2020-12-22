package sceneTesting;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class sceneTesting implements Initializable {
    @FXML
    private AnchorPane anchorpri;

    @FXML
    private AnchorPane anchorsec;
    @FXML
    private JFXTextField txt1;

    @FXML
    private JFXButton btn1;

    private void setAnchorConstraint(BorderPane borderPane) {

        Double v = 0.0;
        AnchorPane.setTopAnchor(borderPane,v);
        AnchorPane.setBottomAnchor(borderPane,v);
        AnchorPane.setLeftAnchor(borderPane,v);
        AnchorPane.setRightAnchor(borderPane,v);

    }
    @FXML
    void next(ActionEvent event) throws IOException {
        BorderPane borderPane=null;
        borderPane=FXMLLoader.load(getClass().getResource("/sceneTesting/scene1.fxml"));
        anchorsec.getChildren().setAll(borderPane);
        setAnchorConstraint(borderPane);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader.load(getClass().getResource("/sceneTesting/scene1.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
