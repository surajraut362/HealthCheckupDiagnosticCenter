package ui.pendingReport;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import db.DbHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReportGenerate implements Initializable {

    @FXML
    private BorderPane borderGenerate;
    @FXML
    private Label label;
    @FXML
    private VBox vbox;
    private String testname;
    private String sex;
    private float age;

    @FXML
    private VBox vbox1;

    public void setTest_Patient(String testname,String sex,float age)
    {
        this.testname=testname;
        label.setText(testname);
        this.sex=sex;
        this.age=age;
        setParameter();

    }
    public VBox getVbox1()
    {
        return  vbox1;
    }

    Connection con= DbHandler.getConnection();
    PreparedStatement ps;
    ResultSet rs;
    int parametercode=-1;
    public ObservableList rangelist= FXCollections.observableArrayList();
    public ObservableList unitlist= FXCollections.observableArrayList();
    public ObservableList resultlist=FXCollections.observableArrayList();

    public void  setParameter()
    {
        try {
            ps=con.prepareStatement("Select * from test t1 inner join parameter p1 on p1.testcode=t1.testcode and t1.testname=?");
            ps.setString(1,testname);
            rs=ps.executeQuery();
            while (rs.next())
            {
                JFXComboBox<String> paramName=new JFXComboBox();
//                paramName.setDisable(true);
                JFXComboBox<String> range=new JFXComboBox();
                range.setPromptText("Range");
                rangelist.add(range);
                range.setLabelFloat(true);
                JFXTextField result=new JFXTextField();
                resultlist.add(result);
                result.setPromptText("Result");
                result.setLabelFloat(true);
                JFXComboBox unit=new JFXComboBox();
                unitlist.add(unit);
                unit.setPromptText("Unit");
                unit.setLabelFloat(true);
                HBox hBox=new HBox(paramName,range,result,unit);
                hBox.setSpacing(15);
                vbox.getChildren().add(hBox);
                vbox.setSpacing(20);
                parametercode= rs.getInt("parametercode");
                paramName.getItems().add(( rs.getString("parametername")));
                ps=con.prepareStatement("Select fromage,toage,rangefrom,rangeto,unit from parameter p1 inner join normalvalue n1 on n1.parametercode=p1.parametercode and p1.parametercode=? and n1.sex=?;");
                ps.setInt(1,parametercode);
                ps.setString(2,sex);
                ResultSet rs1=ps.executeQuery();
                range.getItems().clear();
                System.out.println("onsetparameter"+parametercode+sex);
                while (rs1.next()) {
                    System.out.println("onrange");
                    float s_age=rs1.getFloat("fromage");
                    System.out.println(s_age);
                    double l_age=rs1.getDouble(2);

                    System.out.println(l_age);
                    if (( s_age<= age) && ( l_age>=age)) {
                        System.out.println(rs1.getFloat("rangefrom")+"-"+rs1.getFloat("rangeto"));
                            range.getItems().add(rs1.getFloat("rangefrom")+"-"+rs1.getFloat("rangeto"));

                    }
                    unit.getItems().add(rs1.getString("unit"));
                    }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


        public BorderPane getBorderPane()
        {
            return borderGenerate;
        }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

