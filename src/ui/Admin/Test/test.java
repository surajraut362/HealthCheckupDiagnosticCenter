package ui.Admin.Test;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.sun.org.apache.bcel.internal.generic.ALOAD;
import db.DbHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import ui.support.AlertBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class test implements Initializable {

    @FXML
    private AnchorPane anchorcenter;

    @FXML
    private JFXComboBox<String> cbdept;

    @FXML
    private JFXTextField txttest;

    @FXML
    private JFXButton btncancel;

    @FXML
    private JFXButton btnaddtest;

    @FXML
    private JFXButton btnupdatetest;

    @FXML
    private JFXButton btnremovetest;

    @FXML
    private JFXTextField txtcost;

    @FXML
    private JFXTextField txtprice;

    @FXML
    private JFXTextField txtsearch;

    @FXML
    private JFXComboBox<String> cbsearchcriteria;

    @FXML
    private TableView<Test> tbltest;

    @FXML
    private TableColumn<?, ?> coldept;

    @FXML
    private TableColumn<?, ?> coltest;

    @FXML
    private TableColumn<?, ?> colcost;

    @FXML
    private TableColumn<?, ?> colprice;
    Connection con= DbHandler.getConnection();
    PreparedStatement ps=null;
    ResultSet rs=null;
    int admin;
   int deptcode=-1;
    public void loadpane(int admin)
    {
        this.admin=admin;
    }
    @FXML
    void addtest(ActionEvent event) {

        if(cbdept.getValue().isEmpty()|| txttest.getText().isEmpty()||txtprice.getText().isEmpty()||txtcost.getText().isEmpty())
        {
         new AlertBox("Please fill the required fields! ",event);
        }
        else {
            try {
                ps = con.prepareStatement("select count(*) from test where testname=?;");
                ps.setString(1, txttest.getText().toUpperCase());
                rs = ps.executeQuery();
                rs.next();
                if (rs.getInt(1) != 1) {
                    if (Float.valueOf(txtcost.getText()) <= Float.valueOf(txtprice.getText())) {
                        ps = con.prepareStatement("insert into test(testname,cost,price,deptcode) values(?,?,?,?);");
                        ps.setString(1, txttest.getText().toUpperCase());
                        ps.setFloat(2, Float.parseFloat(txtcost.getText()));
                        ps.setFloat(3, Float.parseFloat(txtprice.getText()));
                        ps.setInt(4,deptcode);
                        ps.execute();
                        cancel(event);
                    } else {
                        new AlertBox("Price cant be smaller than Cost", event);
                    }

                } else {
                    new AlertBox("Test already exists", event);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        cbdept.setDisable(false);
        cbdept.getSelectionModel().clearSelection();
        cbsearchcriteria.getSelectionModel().clearSelection();
        tbltest.getItems().clear();
        txttest.clear();
        txtcost.clear();
        txtprice.clear();
        txtsearch.clear();
        btnaddtest.setVisible(true);
        btncancel.setVisible(false);
        btnupdatetest.setVisible(false);
        btnremovetest.setVisible(false);

    }
        Test t1;
    @FXML
    void loadtestdata(MouseEvent event) {
    t1=tbltest.getSelectionModel().getSelectedItem();
    if(t1!=null)
    {
        cbdept.setValue(t1.getDept());
        cbdept.setDisable(true);
        txttest.setText(t1.getTest());
        txtcost.setText(String.valueOf(t1.getCost()));
        txtprice.setText(String.valueOf(t1.getPrice()));
        btnupdatetest.setVisible(true);
        btnaddtest.setVisible(false);
        btnremovetest.setVisible(true);
        btncancel.setVisible(true);
    }
    }

    @FXML
    void print(ActionEvent event) {

    }

    @FXML
    void removetest(ActionEvent event) {
        if(t1.getTest().equals(txttest.getText())&&Float.valueOf(txtcost.getText()).equals(t1.getCost()) &&Float.valueOf(txtprice.getText()).equals(t1.getPrice())) {
            try {

                ps=con.prepareStatement("select count(*) from  test t1 inner join parameter p1 on t1.testcode=p1.testcode and testname=?;");
                ps.setString(1,t1.getTest());
                rs=ps.executeQuery();
                rs.next();
                if(!(rs.getInt(1)>=1)) {
                    ps = con.prepareStatement("delete from test where testname=?;");
                    ps.setString(1, txttest.getText().toUpperCase());
                    ps.execute();
                    cancel(event);

                }
                else {
                    new AlertBox("Parameter already exist with Test first delete parameter ",event);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else
        {
            new AlertBox("You have made changes reselect the item",event);
        }

    }

    @FXML
    void search(ActionEvent event) {
        try {
            if (!txtsearch.getText().isEmpty()) {
                if (!cbsearchcriteria.getSelectionModel().isEmpty()) {
                    testlist.clear();
                    switch (cbsearchcriteria.getSelectionModel().getSelectedIndex()) {
                        case 0:ps=con.prepareStatement("select deptname,testname,cost,price from department d1 inner join test t1 on t1.deptcode=d1.deptcode and d1.deptname=?;");
                                ps.setString(1,txtsearch.getText().toUpperCase());
                                rs=ps.executeQuery();
                                while (rs.next())
                                {
                                    testlist.add(new Test(rs.getString("deptname"),rs.getString("testname"),rs.getFloat("cost"),rs.getFloat("price")));
                                }
                                tbltest.getItems().setAll(testlist);
                                break;

                        case 1:ps=con.prepareStatement("select deptname,testname,cost,price from department d1 inner join test t1 on t1.deptcode=d1.deptcode and t1.testname=?;");
                            ps.setString(1,txtsearch.getText().toUpperCase());
                            rs=ps.executeQuery();
                            while (rs.next())
                            {
                                testlist.add(new Test(rs.getString("deptname"),rs.getString("testname"),rs.getFloat("cost"),rs.getFloat("price")));
                            }
                            tbltest.getItems().setAll(testlist);
                            break;


                    }
                } else {
                    new AlertBox("Criteria is not selected", event);
                }
            } else {
                new AlertBox("Search field is empty", event);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }
    ObservableList testlist= FXCollections.observableArrayList();
    @FXML
    void showall(ActionEvent event) {
        try {
            testlist.clear();
            tbltest.getItems().clear();
            ps=con.prepareStatement("select deptname,testname,cost,price from department d1 inner join test t1 on t1.deptcode=d1.deptcode;");
            rs=ps.executeQuery();
            while (rs.next())
            {
                testlist.add(new Test(rs.getString("deptname"),rs.getString("testname"),rs.getFloat("cost"),rs.getFloat("price")));
            }
            tbltest.getItems().setAll(testlist);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void updatetest(ActionEvent event) {
        if(!(t1.getTest().equals(txttest.getText())&&t1.getCost()==Float.valueOf(txtcost.getText()) &&t1.getPrice()==Float.valueOf(txtprice.getText()))){
            try {
                if(Float.valueOf(txtprice.getText())>=Float.valueOf(txtcost.getText())) {
                    ps = con.prepareStatement("update test set testname=?,cost=?,price=? where testname=?;");
                    ps.setString(1, txttest.getText().toUpperCase());
                    ps.setFloat(2, Float.parseFloat(txtcost.getText()));
                    ps.setFloat(3, Float.parseFloat(txtprice.getText()));
                    ps.setString(4, t1.getTest());
                    ps.execute();
                    cancel(event);
                }
                else
                {
                    new AlertBox("Cost price cant be greater than the price",event);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else
        {
            new AlertBox("! No changes reuired.",event);
        }
    }
    void initdept()
    {
        deptlist.clear();
        try {
            ps=con.prepareStatement("Select * from department;");
            rs=ps.executeQuery();
            while (rs.next())
            {
                deptlist.add(rs.getString("deptname"));
            }
            cbdept.getItems().setAll(deptlist);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    ObservableList deptlist=FXCollections.observableArrayList();
    ObservableList criterialist=FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        criterialist.setAll("Department","Test");
        cbsearchcriteria.getItems().setAll(criterialist);
        coldept.setCellValueFactory(new PropertyValueFactory<>("dept"));
        coltest.setCellValueFactory(new PropertyValueFactory<>("test"));
        colcost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colprice.setCellValueFactory(new PropertyValueFactory<>("price"));
        btnupdatetest.setVisible(false);
        btnremovetest.setVisible(false);
        btncancel.setVisible(false);
        initdept();
        cbdept.setOnAction(event ->
        {
            try {
                ps=con.prepareStatement("Select deptcode from department where deptname=?");
                ps.setString(1,cbdept.getSelectionModel().getSelectedItem());
                rs=ps.executeQuery();
                while (rs.next())
                {
                    deptcode= rs.getInt("deptcode");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    public class Test
    {
        String dept,test;
        float cost,price;
        public Test(String dept,String test,float cost,float price)
        {
            this.dept=dept;
            this.test=test;
            this.cost=cost;
            this.price=price;
        }

        public String getDept() {
            return dept;
        }

        public void setDept(String dept) {
            this.dept = dept;
        }

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }

        public float getCost() {
            return cost;
        }

        public void setCost(float cost) {
            this.cost = cost;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }
    }
}
