package ui.Admin.Test.addTest;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import db.DbHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import ui.support.AlertBox;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class addingTest implements Initializable {

    @FXML
    private JFXComboBox<String> txtdept;

    @FXML
    private JFXComboBox<String> txttestname;

    @FXML
    private JFXComboBox<String> txtparameter;

    @FXML
    private JFXButton btnback;


    int deptcode=-1,testcode=-1,parametercode=-1;
    @FXML
    private JFXButton btncleartest;

    @FXML
    private JFXButton btnproceed;

    @FXML
    private JFXButton btnedittest;

    @FXML
    private JFXCheckBox chknounit;

    @FXML
    private JFXComboBox<String> txtsex;

    @FXML
    private JFXTextField txtunit;

    @FXML
    private JFXCheckBox chkforallages;

    @FXML
    private TextField txtyearfrom;

    @FXML
    private TextField txtyearto;

    @FXML
    private JFXButton btnclearnormal;

    @FXML
    private JFXTextField txtrangefrom;

    @FXML
    private JFXTextField txtrangeto;

    @FXML
    private JFXButton btnaddnormal;

    @FXML
    void addnormal(ActionEvent event) {
        if (txtsex.getSelectionModel().isEmpty()){
            new AlertBox("Gender/Sex Field Criteria is Empty",event);
        } else {
            if (txtyearfrom.getText().isEmpty() || txtyearto.getText().isEmpty()) {
                new AlertBox("Age Years fields are empty", event);
            } else {
                if (txtrangefrom.getText().isEmpty() || txtrangefrom.getText().isEmpty()) {
                    new AlertBox("Range Fields are empty", event);
                } else {
                    try {
                        PreparedStatement ps = con.prepareStatement("insert into normalvalue(sex,fromage,toage,rangefrom,rangeto,unit,parametercode) values(?,?,?,?,?,?,?);");
                        ps.setString(1, txtsex.getSelectionModel().getSelectedItem());
                        ps.setFloat(2, Float.parseFloat(txtyearfrom.getText()));
                        ps.setFloat(3, Float.parseFloat(txtyearto.getText()));
                        ps.setFloat(4, Float.parseFloat(txtrangefrom.getText()));
                        ps.setFloat(5, Float.parseFloat(txtrangeto.getText()));
                        ps.setString(6, txtunit.getText());
                        ps.setInt(7, parametercode);
                        ps.execute();
                        clearNormal(event);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    @FXML
    void btnproceed(ActionEvent event) {
        if( txtdept.getValue()!=null &&txttestname!=null&& txtparameter!=null) {
            testactive(true);
            btnproceed.setVisible(false);
            btnedittest.setVisible(true);
        }
        else
        {
            new AlertBox("All required fields are empty",event);
        }
    }
    @FXML
    void editTest(ActionEvent event)
    {

        btnproceed.setVisible(true);
        btnedittest.setVisible(false);
        clearNormal(event);
        testactive(false);
    }
    @FXML
    void clearNormal(ActionEvent event) {
        txtsex.getSelectionModel().clearSelection();
        chknounit.setSelected(false);
        txtunit.clear();
        chkforallages.setSelected(false);
        txtyearfrom.clear();
        txtyearto.clear();
        txtrangefrom.clear();
        txtrangeto.clear();
    }

    @FXML
    void cleartest(ActionEvent event) {
        txtdept.getSelectionModel().clearSelection();
        txttestname.getItems().clear();
        txtparameter.getItems().clear();
        txtparameter.setValue(null);
        txttestname.getSelectionModel().clearSelection();

    }


    @FXML
    void onback(ActionEvent event) {
        Scene scene = ((Node)event.getSource()).getScene();
        AnchorPane anchorPane = (AnchorPane) scene.lookup("#panetest");
        anchorPane.toFront();
        cleartest(event);
        clearNormal(event);
    }

    void testactive(Boolean val)
    {
        txtdept.setDisable(val);
        txttestname.setDisable(val);
        txtparameter.setDisable(val);
        btncleartest.setDisable(val);
        chkforallages.setDisable(!val);
        chknounit.setDisable(!val);
        txtsex.setDisable(!val);
        txtunit.setDisable(!val);
        txtyearfrom.setDisable(!val);
        txtyearto.setDisable(!val);
        txtrangefrom.setDisable(!val);
        txtrangeto.setDisable(!val);
        btnaddnormal.setDisable(!val);
        btnclearnormal.setDisable(!val);
    }
    ObservableList deptlist= FXCollections.observableArrayList();
    PreparedStatement ps=null;
    ResultSet rs;
    Connection con= DbHandler.getConnection();
    void initdept()
    {
        try {
            deptlist.clear();
            ps = con.prepareStatement("Select * from department;");
            rs=ps.executeQuery();
            ps=null;
            while(rs.next())
            {
                deptlist.add(rs.getString("deptname"));
            }
            txtdept.getItems().setAll(deptlist);
            rs=null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    ObservableList testlist=FXCollections.observableArrayList();
    ObservableList parameterlist=FXCollections.observableArrayList();
    ObservableList genderlist=FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initdept();
        testactive(false);
        btnedittest.setVisible(false);
        genderlist.setAll("Male","Female");
        txtsex.getItems().setAll(genderlist);
        txtdept.setOnAction(event -> {
            try {
                testlist.clear();
                txttestname.getSelectionModel().clearSelection();
                txtparameter.getSelectionModel().clearSelection();
                ps = con.prepareStatement("Select * from department where deptname=?");
                ps.setString(1, txtdept.getSelectionModel().getSelectedItem());
                rs = ps.executeQuery();
                while (rs.next()) {
                    deptcode = rs.getInt("deptcode");
                }

                ps = con.prepareStatement("Select * from test where deptcode='" + deptcode + "';");
                rs = ps.executeQuery();
                while (rs.next()) {
                    testlist.add(rs.getString("testname"));
                }
                txttestname.getItems().setAll(testlist);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        txttestname.setOnAction(event -> {
            try {
                parameterlist.clear();
                 ps = con.prepareStatement("Select testcode from test where testname=?");
                ps.setString(1,  txttestname.getSelectionModel().getSelectedItem());
                rs = ps.executeQuery();
                while (rs.next()) {
                    testcode = rs.getInt("testcode");
                }
                ps=con.prepareStatement("Select parametername from parameter where testcode=?;");
                ps.setInt(1,testcode);
                rs=ps.executeQuery();
                while (rs.next())
                {
                    parameterlist.add(rs.getString("parametername"));
                }
                txtparameter.getItems().setAll(parameterlist);

            } catch (SQLException e) {
                e.printStackTrace();
            }});
        txtparameter.setOnAction(event -> {
            try {

                ps=con.prepareStatement("select parametercode from parameter where parametername=?;");
                ps.setString(1,txtparameter.getSelectionModel().getSelectedItem());
                rs=ps.executeQuery();
                while (rs.next())
                {
                    parametercode=rs.getInt("parametercode");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });

        chknounit.selectedProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue)
            {
                if(chknounit.isSelected()==true)
                    txtunit.setDisable(true);
                else
                    txtunit.setDisable(false);
            }
        });
        chkforallages.selectedProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue)
            {
                if(chkforallages.isSelected()==true)
                {
                    txtyearfrom.setText("0");
                    txtyearto.setText("150");
                    txtyearfrom.setDisable(true);
                    txtyearto.setDisable(true);

                }
                else
                {
                    txtyearfrom.clear();
                    txtyearto.clear();
                    txtyearfrom.setDisable(false);
                    txtyearto.setDisable(false);

                }
            }
        });

    }
}

