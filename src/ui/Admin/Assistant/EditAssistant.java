package ui.Admin.Assistant;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class EditAssistant {

    @FXML
    private JFXComboBox<?> comboassname;

    @FXML
    private BorderPane paneborderadd;

    @FXML
    private ImageView imageprofile;

    @FXML
    private JFXButton btnedita;

    @FXML
    private JFXButton btnsave;

    @FXML
    private JFXTextArea txtadd;

    @FXML
    private JFXDatePicker assistantdob;

    @FXML
    private JFXTextField txtagender;

    @FXML
    private JFXTextField assistantname;

    @FXML
    private JFXTextField email;

    @FXML
    private JFXTextField mobno;

    @FXML
    private JFXButton clearAll;

    @FXML
    private JFXButton delete;
    public  void  hide(Boolean bool)
    {
        btnedita.setVisible(bool);
        delete.setVisible(bool);
        btnsave.setVisible(!bool);
        clearAll.setVisible(!bool);
        comboassname.setVisible(bool);
    }
    @FXML
    void Clear(ActionEvent event) {

    }

    @FXML
    void chooseProfilePhoto(ActionEvent event) {

    }

    @FXML
    void editadditional(ActionEvent event) {

    }

    @FXML
    void onback(ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();
        AnchorPane anchorPane = (AnchorPane) scene.lookup("#anchorcenter");

        anchorPane.toFront();

    }

    @FXML
    void ondelete(ActionEvent event) {

    }

    @FXML
    void saveadditional(ActionEvent event) {

    }

}
