package ui.login;

import com.jfoenix.controls.JFXButton;
import db.DbHandler;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import ui.Admin.AdminDash.AdminDash;
import ui.AssistantDashboard.DashController;
import ui.support.AlertBox;
import ui.support.CloseStage;
import ui.support.CreateStage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static ui.security.encryptpassword.encryptpassword;

public class Login implements Initializable {


    @FXML
    private AnchorPane login;

    @FXML
    private AnchorPane layer1;

    @FXML
    private Label lblassistant;

    @FXML
    private Label lbladmin;

    @FXML
    private JFXButton btnsignin;

    @FXML
    private TextField txtuser;

    @FXML
    private Label lblForgot;

    @FXML
    private PasswordField txtpass;

    @FXML
    private AnchorPane layer2;

    @FXML
    private Label lblwelc;

    @FXML
    private Label lblcomp;

    @FXML
    private Label lblanth;

    @FXML
    private JFXButton btnassistant;

    @FXML
    private JFXButton btnadmin;

    public void visibility() {
        btnsignin.setVisible(true);
        txtuser.setVisible(true);
        txtpass.setVisible(true);
        lblForgot.setVisible(true);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtuser.setText("root");
        txtpass.setText("root");
        btnadmin.setVisible(true);
        btnassistant.setVisible(false);
        lblassistant.setVisible(true);
        lbladmin.setVisible(false);
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
        btnadmin.setVisible(false);
        btnassistant.setVisible(true);
        lblassistant.setVisible(false);
        lbladmin.setVisible(true);

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
        btnadmin.setVisible(true);
        btnassistant.setVisible(false);
        lblassistant.setVisible(true);
        lbladmin.setVisible(false);
    }

   @FXML
    void login(ActionEvent event) throws IOException {

//           if(lbladmin.isVisible()==true)
//           {
//
//               new CreateStage().createStage("Welcome To System ","/ui/Admin/AdminDash/adminDash.fxml", null, 0, false, false, true);
//               CloseStage.closeStage(event);
//           }
//           else {
//               new CreateStage().createStage("Welcome To System ", "/ui/dashboard/dash.fxml", null, 0, false, false, true);
//               CloseStage.closeStage(event);
//           }
            verify(event);
               }



    int loginid;
    public void verify(ActionEvent event)
    {
        try{

            if (!(txtuser.getText().equals("") || txtpass.getText().equals(""))){

                PreparedStatement ps = DbHandler.getConnection().prepareStatement("select count(*)from admin where username =? ");
                ps.setString(1,txtuser.getText());
                ResultSet rs = ps.executeQuery();
                rs.next();
                if (rs.getInt(1)==1){

                    if (lbladmin.isVisible()==true) {
                        rs=null;
                        ps = DbHandler.getConnection().prepareStatement("select adminid from admin where username =?  and password= ? ");
                        ps.setString(1, txtuser.getText());
                        ps.setString(2, encryptpassword(txtpass.getText()));
                        rs = ps.executeQuery();
                            if (rs!=null) {
                                rs.next();
                                FXMLLoader loader;
                                    loader = new CreateStage().createStage("Welcome To System ", "/ui/Admin/AdminDash/adminDash.fxml", null, 0, false, true, true, true);
                                AdminDash controller = loader.getController();
                                controller.loadPane(rs.getInt("adminid"));
                                CloseStage.closeStage((ActionEvent) event);
                            } else
                                new AlertBox("Error","Wrong Credential");

                    }
                    else
                    {
                        ps = DbHandler.getConnection().prepareStatement("select count(*)from assistant where username =? or email=? and password= ?");
                        ps.setString(1,txtuser.getText());
                        ps.setString(2,txtuser.getText());
                        ps.setString(3,encryptpassword(txtpass.getText()));
                        rs = ps.executeQuery();
                        rs.next();
                        if (rs.getInt(1)==1){

                            ps = DbHandler.getConnection().prepareStatement("select assistantid from assistant where username =? or email=?  and password= ? ");
                            ps.setString(1, txtuser.getText());
                            ps.setString(2,txtuser.getText());
                            ps.setString(3, encryptpassword(txtpass.getText()));
                            rs = ps.executeQuery();
                            rs.next();
                            FXMLLoader loader;
                            loader = new CreateStage().createStage("Welcome To System ", "/ui/AssistantDashboard/dash.fxml", null, 0, false, false, true, true);
                             DashController controller = loader.getController();
                            controller.loadpane(rs.getInt("assistantid"));
                            CloseStage.closeStage(event);
                        }
                        else
                            new AlertBox("Error","Wrong Credential");
                    }
                }
                else
                {
                    new AlertBox(" Login Alert","Please enter correct Username and Password ");
                }
            }
            else
            {
                new AlertBox(" Login Alert","Please enter login credential ... ");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        }
    }