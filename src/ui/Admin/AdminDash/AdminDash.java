package ui.Admin.AdminDash;

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.Admin.Assistant.Assistant;
import ui.Admin.Test.alltest;
import ui.support.CloseStage;
import ui.support.CreateStage;
import ui.support.setAnchorConstraint;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminDash extends Application implements Initializable {

    @FXML
    private JFXButton btnback;

    @FXML
    private AnchorPane panenav;

    @FXML
    private JFXButton btnhamburger;


    @FXML
    private AnchorPane paneanchormain;
    BorderPane assistantp=null;
    BorderPane adminp=null;
    AnchorPane doctorp=null;
    BorderPane addtestp=null;
    BorderPane edittestp=null;
    BorderPane editnormp=null;
    BorderPane ptestp=null;
    StackPane switchp=null;

    ObservableList<Parent> dash = FXCollections.observableArrayList();
    @FXML
    private BorderPane mainBorder;
    AnchorPane pane ;
    @FXML
    void onAssistantClick(ActionEvent event) {
        if(panenav.getPrefWidth()!=50.00)
            toggleNav(event);
        paneanchormain.getChildren().setAll(assistantp);
        new setAnchorConstraint(assistantp);

    }



    @FXML
    void onDailyCollection(ActionEvent event) {

    }

    @FXML
    void onDashboardClick(ActionEvent event) {
        if(panenav.getPrefWidth()!=50.00)
            toggleNav(event);
        paneanchormain.getChildren().setAll(dash.get(0));
//        loadPane();
//        paneanchormain.getChildren().setAll(dash.get(0));

    }

    @FXML
    void onDoctorClick(ActionEvent event) {
        if(panenav.getPrefWidth()!=50.00)
            toggleNav(event);
        paneanchormain.getChildren().setAll(doctorp);
        new setAnchorConstraint(doctorp);

//        AnchorPane.setTopAnchor(doctorp,0.0);
//        AnchorPane.setBottomAnchor(doctorp,0.0);
//        AnchorPane.setLeftAnchor(doctorp,0.0);
//        AnchorPane.setRightAnchor(doctorp,0.0);

    }

    @FXML
    void onSwitchClick(ActionEvent event)
    {
        CloseStage.closeStage(event);
       new CreateStage().createStage("Welcome To System ", "/ui/Dashboard/dash.fxml",null,0,false,true,true,true);
        CloseStage.closeStage(event);
    }

    @FXML
    void onTestClick(ActionEvent event) {
        if(panenav.getPrefWidth()!=50.00)
            toggleNav(event);
        paneanchormain.getChildren().setAll(ptestp);
        new setAnchorConstraint(ptestp);

    }
    @FXML
    void logout(ActionEvent event) throws Exception {
        CloseStage.closeStage(event);
       Stage login=new Stage();
        start(login);
    }
    FXMLLoader loader;
    alltest alltestcontrol=null;
    Assistant assistantcontrol=null;
    public void loadPane(int admin)
    {

        dash.add( (Parent) paneanchormain.getChildren().get(0));

        try {
           loader=new FXMLLoader(getClass().getResource("/ui/Admin/Test/alltest.fxml"));
           ptestp=(BorderPane) loader.load();
           alltestcontrol=loader.getController();
           alltestcontrol.loadpane(admin);
            loader=new FXMLLoader(getClass().getResource("/ui/Admin/Assistant/assistant.fxml"));
            assistantp=(BorderPane) loader.load();
            assistantcontrol=loader.getController();
            assistantcontrol.loadpane(admin);
            doctorp=(AnchorPane)FXMLLoader.load(getClass().getResource("/ui/Admin/doctor/Doctor.fxml"));
            switchp=(StackPane)FXMLLoader.load(getClass().getResource("/ui/AssistantDashboard/dash.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean flag = false;
    @FXML
    void toggleNav(ActionEvent event) {

        if(flag){
            panenav.setPrefWidth(50);
            paneanchormain.setEffect(null);
//            paneanchormain.setPrefWidth(paneanchormain.getPrefWidth()+130.00);
            flag = false;
        }
        else
        {
            panenav.setPrefWidth(200);
            paneanchormain.setEffect(new BoxBlur());
//            paneanchormain.setPrefWidth(paneanchormain.getPrefWidth()-130);
            flag = true;
        }


        System.out.println("WOrking");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/ui/login/login.fxml"))));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }
}
