package ui.dashboard;


import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashController implements Initializable {

    @FXML
    private JFXButton btnback;


    Parent previousPane;

    Parent children= null;

    @FXML
    private AnchorPane paneanchormain;
    AnchorPane pane;
    String username;
    public void setUsername(String user)
    {
        username=user;
    }
    @FXML
    void back(ActionEvent event) {

    //AnchorPane a = previousPane;
    paneanchormain.getChildren().setAll(previousPane);
        
    }

    private void setAnchorConstraint(BorderPane borderPane) {

        Double v = 0.0;
        AnchorPane.setTopAnchor(borderPane,v);
        AnchorPane.setBottomAnchor(borderPane,v);
        AnchorPane.setLeftAnchor(borderPane,v);
        AnchorPane.setRightAnchor(borderPane,v);

    }
    @FXML
    void billing(ActionEvent event) {
        BorderPane borderPane = null;



        loadingBackPane(event);


        try {
            borderPane = (BorderPane) FXMLLoader.load(getClass().getResource("/ui/paymentBill/payment.fxml"));
            paneanchormain.getChildren().setAll(borderPane);
            setAnchorConstraint(borderPane);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadingBackPane(ActionEvent event) {
        Node n = (Node) event.getSource();
        Scene sc=n.getScene();
        AnchorPane p = (AnchorPane) sc.lookup("#paneanchormain");
        ObservableList<Parent> observableList = (ObservableList) p.getChildren();
        previousPane = observableList.get(0);
    }

    @FXML
    void patientRegistration(ActionEvent event) {
        BorderPane borderPane = null;
        loadingBackPane(event);

        try {
            borderPane = (BorderPane) FXMLLoader.load(getClass().getResource("/ui/patient/patient2.fxml"));
            setAnchorConstraint(borderPane);
            paneanchormain.getChildren().setAll(borderPane);
            ;
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @FXML
    void receipt(ActionEvent event) {
        BorderPane borderPane = null;
        try {
            borderPane = (BorderPane) FXMLLoader.load(getClass().getResource("/ui/paymentBill/payment.fxml"));
            paneanchormain.getChildren().setAll(borderPane);
            setAnchorConstraint(borderPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void report(ActionEvent event) {
        BorderPane borderPane = null;
        try {
            borderPane = (BorderPane) FXMLLoader.load(getClass().getResource("/ui/pendingReport/pendingReport.fxml"));
            paneanchormain.getChildren().setAll(borderPane);
            setAnchorConstraint(borderPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void sampleCollection(ActionEvent event) {

    }

    @FXML
    void sampleTest(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        previousPane = null;
    }
}
