package ui;

import db.DbHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainF extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/ui/login/login.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
    @Override
    public void init() throws Exception
    {
        try {
            DbHandler.excAction("use healthcenter");
            DbHandler.createAdminTable();
            DbHandler.createAssistantTable();
            DbHandler.createPatientTable();
            DbHandler.createDoctorTable();
            DbHandler.createDepartmentTable();

            DbHandler.createTestTable();
            DbHandler.createParameterTable();
            DbHandler.createNormalValueTable();
            DbHandler.createBillingTable();
            DbHandler.createSampleTable();
            DbHandler.createReportTable();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}

