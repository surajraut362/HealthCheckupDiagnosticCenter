package ui.Admin.Test;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ui.support.setAnchorConstraint;

import java.io.IOException;

public class alltest {

        @FXML
        private AnchorPane panedept;
        @FXML
        private Tab tabnormal;
        @FXML
        private AnchorPane paneparameter;

        @FXML
        private AnchorPane panenormal;
        @FXML
        private Tab tabtest;
        @FXML
        private Tab tabparameter;

        @FXML
        private  AnchorPane panetest;
        int admin;
        dept deptcontrol;
        test testcontrol;
        NormalValue normalcontrol;
        parameter parametercontrol;
    FXMLLoader loader;
    BorderPane borderdept;
    BorderPane bordertest,borderparameter,bordernormal ;
        public void loadpane(int admin)
        {
            this.admin=admin;
            try {
        loader =new  FXMLLoader(getClass().getResource("/ui/Admin/Test/dept.fxml"));
        borderdept=(BorderPane)loader.load();
        deptcontrol=loader.getController();
        deptcontrol.loadpane(admin);
        panedept.getChildren().setAll(borderdept);
        new setAnchorConstraint(borderdept);
        loader=new FXMLLoader(getClass().getResource("/ui/Admin/Test/test.fxml"));
        bordertest=(BorderPane) loader.load();
        testcontrol=loader.getController();
        testcontrol.loadpane(admin);
        panetest.getChildren().setAll(bordertest);
        new setAnchorConstraint(bordertest);
        loader=new FXMLLoader(getClass().getResource("/ui/Admin/Test/parameter.fxml"));
        borderparameter=(BorderPane) loader.load();
        parametercontrol=loader.getController();
        parametercontrol.loadpane(admin);
        paneparameter.getChildren().setAll(borderparameter);
        new setAnchorConstraint(borderparameter);
        loader=new FXMLLoader(getClass().getResource("/ui/Admin/Test/NormalValue.fxml"));
        bordernormal=(BorderPane) loader.load();
        normalcontrol=loader.getController();
        normalcontrol.loadpane(admin);
        panenormal.getChildren().setAll(bordernormal);
        new setAnchorConstraint(bordernormal);

        tabtest.selectedProperty().addListener((observable, oldValue, newValue) ->
        {
            testcontrol.initdept();
        });
        tabparameter.selectedProperty().addListener((observable, oldValue, newValue) ->
        {
            parametercontrol.initdept();
        });
        tabnormal.selectedProperty().addListener((observable, oldValue, newValue) ->
        {
            normalcontrol.initdept();
        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



}
