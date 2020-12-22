package ui.AssistantDashboard;


import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.collections.FXCollections;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.patient.Patient;
import ui.support.CloseStage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashController extends Application implements Initializable {

    @FXML
    private JFXButton btnback;


    Parent previousPane;

    Parent children= null;

    @FXML
    private AnchorPane paneanchormain;
    AnchorPane pane;
    int assistant;
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
//    @FXML
//    void billing(ActionEvent event) {
//        BorderPane borderPane = null;
//        loadingBackPane(event);
//        try {
//            borderPane = (BorderPane) FXMLLoader.load(getClass().getResource("/ui/paymentBill/payment.fxml"));
//            paneanchormain.getChildren().setAll(borderPane);
//            setAnchorConstraint(borderPane);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
    BorderPane borderpatient=null;
    Patient controlpatient=null;
    BorderPane borderreport=null;
    ObservableList<Parent> dash = FXCollections.observableArrayList();
    public void loadpane(int assistant)
    {
        try {
        this.assistant=assistant;
        dash.add( (Parent) paneanchormain.getChildren().get(0));
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/ui/patient/patient2.fxml"));
        borderpatient=(BorderPane) loader.load();
        controlpatient=loader.getController();
        controlpatient.loadpane(assistant);

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
        loadingBackPane(event);

        setAnchorConstraint(borderpatient);
        paneanchormain.getChildren().setAll(borderpatient);
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
    @FXML
    void logout(ActionEvent event) throws Exception {
        CloseStage.closeStage(event);
        Stage login=new Stage();
        start(login);


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        previousPane = null;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/ui/login/login.fxml"))));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }
}
