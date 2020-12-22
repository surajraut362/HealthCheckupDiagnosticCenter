package sample;
import com.jfoenix.controls.JFXButton;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private AnchorPane sign_in;

    @FXML
    private AnchorPane layer1;

    @FXML
    private Label a1;

    @FXML
    private Label b1;

    @FXML
    private JFXButton btnsignin;

    @FXML
    private TextField n1;

    @FXML
    private Label c3;

    @FXML
    private PasswordField n2;

    @FXML
    private AnchorPane layer2;

    @FXML
    private Label l1;

    @FXML
    private Label l2;

    @FXML
    private Label l3;

    @FXML
    private JFXButton assistant;

    @FXML
    private JFXButton admin;

    public void visibility()
    {
        btnsignin.setVisible(true);
        n1.setVisible(true);
        n2.setVisible(true);
        c3.setVisible(true);
        l1.setVisible(true);
        l2.setVisible(true);
        l3.setVisible(true);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
    a1.setVisible(true);
    b1.setVisible(false);
    assistant.setVisible(false);
    admin.setVisible(true);
    visibility();
    }



    @FXML
    public void admin_clicked(MouseEvent mouseEvent) {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.7));
        slide.setNode(layer2);
        slide.setToX(491);
        slide.play();
        layer1.setTranslateX(-309);
        visibility();
        a1.setVisible(false);
        b1.setVisible(true);
        assistant.setVisible(true);
        admin.setVisible(false);
    }

    @FXML
    public void assistant_clicked(MouseEvent mouseEvent) {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.7));
        slide.setNode(layer2);
        slide.setToX(0);
        slide.play();
        layer1.setTranslateX(0);
        visibility();
        a1.setVisible(true);
        b1.setVisible(false);
        assistant.setVisible(false);
        admin.setVisible(true);


    }


}

