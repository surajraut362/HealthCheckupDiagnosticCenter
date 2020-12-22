package ui.Admin.Test;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
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
import java.sql.*;
import java.util.ResourceBundle;

public class parameter implements Initializable {

    @FXML
    private AnchorPane anchorcenter;

    @FXML
    private JFXComboBox<String> cbdept;

    @FXML
    private JFXComboBox<String> cbtest;

    @FXML
    private JFXTextField txtparameter;

    @FXML
    private JFXButton btncancel;

    @FXML
    private JFXButton btnaddparameter;

    @FXML
    private JFXButton btnupdateparameter;

    @FXML
    private JFXButton btnremoveparameter;

    @FXML
    private JFXTextField txtsearch;

    @FXML
    private JFXComboBox<?> cbsearchcriteria;

    @FXML
    private TableView<param> tblparameter;

    @FXML
    private TableColumn<?, ?> coldept;

    @FXML
    private TableColumn<?, ?> coltest;

    @FXML
    private TableColumn<?, ?> colparameter;
    int deptcode=-1,testcode=-1;
    @FXML
    void addparameter(ActionEvent event) {
        if(cbdept.getSelectionModel().isEmpty()|| cbtest.getSelectionModel().isEmpty()||txtparameter.getText().isEmpty())
        {
            new AlertBox("Please fill the required fields! ",event);
        }
        else {
            try {
                ps = con.prepareStatement("select count(*) from parameter where parametername=?;");
                ps.setString(1, txtparameter.getText().toUpperCase());
                rs = ps.executeQuery();
                rs.next();
                if (rs.getInt(1) != 1) {
                        ps = con.prepareStatement("insert into parameter(parametername,dateofadd,testcode) values(?,?,?);");
                        ps.setString(1, txtparameter.getText().toUpperCase());
                       ps.setDate(2, Date.valueOf(DbHandler.date()));
                       ps.setInt(3,testcode);
                        ps.execute();
                        cancel(event);

                } else {
                    new AlertBox("Parameter already exists", event);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    int admin;
    void loadpane(int admin)
    {
        this.admin=admin;
    }

    @FXML
    void cancel(ActionEvent event) {
        cbdept.getSelectionModel().clearSelection();
        cbdept.setDisable(false);
        cbtest.getSelectionModel().clearSelection();
        cbtest.setDisable(false);
        txtparameter.clear();
        txtsearch.clear();
        cbsearchcriteria.getSelectionModel().clearSelection();
        tblparameter.getItems().clear();
        btnaddparameter.setVisible(true);
        btncancel.setVisible(false);
        btnremoveparameter.setVisible(false);
        btnupdateparameter.setVisible(false);
    }
    param p1;
    @FXML
    void loadparameterdata(MouseEvent event) {
p1=tblparameter.getSelectionModel().getSelectedItem();
if(p1!=null)
{
    cbdept.setValue(p1.getDept());
    cbtest.setValue(p1.getTest());
    cbdept.setDisable(true);
    cbtest.setDisable(true);
    txtparameter.setText(p1.getParameter());
    btnaddparameter.setVisible(false);
    btnupdateparameter.setVisible(true);
    btnremoveparameter.setVisible(true);
    btncancel.setVisible(true);
}
    }

    @FXML
    void print(ActionEvent event) {

    }
int parametercode=-1;
    @FXML
    void removeparameter(ActionEvent event) {
        if(txtparameter.getText().equals(p1.getParameter())) {
            try {

                ps = con.prepareStatement("select count(*) from  parameter p1 inner join normalvalue n1 on p1.parametercode=n1.parametercode and p1.parametername=?;");
                ps.setString(1, p1.getParameter());
                rs = ps.executeQuery();
                rs.next();
                if (rs.getInt(1) >= 1) {
                    ps=con.prepareStatement("select parametercode from parameter where parametername=? ");
                    ps.setString(1,p1.getParameter());
                    rs=ps.executeQuery();
                    while (rs.next())
                    {
                        parametercode=rs.getInt("parametercode");
                    }
                    ps=con.prepareStatement("delete from normalvalue where parametercode=?;");
                    ps.setInt(1,parametercode);
                    ps.execute();

                }

                ps = con.prepareStatement("delete from parameter where parametername=?;");
                ps.setString(1, p1.getParameter());
                ps.execute();
                cancel(event);

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
                    paramlist.clear();
                    switch (cbsearchcriteria.getSelectionModel().getSelectedIndex()) {
                        case 0:ps=con.prepareStatement(" select deptname,testname,parametername from department d1 inner join test t1 inner join parameter p1 on t1.deptcode=d1.deptcode and t1.testcode=p1.testcode and d1.deptname=?;");
                            ps.setString(1,txtsearch.getText().toUpperCase());
                            rs=ps.executeQuery();
                            while (rs.next())
                            {
                                paramlist.add(new param(rs.getString("deptname"),rs.getString("testname"),rs.getString("parametername")));
                            }
                            tblparameter.getItems().setAll(paramlist);
                            break;

                        case 1:ps=con.prepareStatement(" select deptname,testname,parametername from department d1 inner join test t1 inner join parameter p1 on t1.deptcode=d1.deptcode and t1.testcode=p1.testcode and t1.testname=?;");
                            ps.setString(1,txtsearch.getText().toUpperCase());
                            rs=ps.executeQuery();
                            while (rs.next())
                            {
                                paramlist.add(new param(rs.getString("deptname"),rs.getString("testname"),rs.getString("parametername")));
                            }
                            tblparameter.getItems().setAll(paramlist);
                            break;
                        case 2:ps=con.prepareStatement(" select deptname,testname,parametername from department d1 inner join test t1 inner join parameter p1 on t1.deptcode=d1.deptcode and t1.testcode=p1.testcode and p1.parametrname=?;");
                            ps.setString(1,txtsearch.getText().toUpperCase());
                            rs=ps.executeQuery();
                            while (rs.next())
                            {
                                paramlist.add(new param(rs.getString("deptname"),rs.getString("testname"),rs.getString("parametername")));
                            }
                            tblparameter.getItems().setAll(paramlist);
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
    ObservableList criterialist=FXCollections.observableArrayList();
        ObservableList paramlist=FXCollections.observableArrayList();
    @FXML
    void showall(ActionEvent event) {
        try {
            paramlist.clear();
            ps=con.prepareStatement("select deptname,testname,parametername from department d1 inner join test t1 inner join parameter p1 on t1.deptcode=d1.deptcode and t1.testcode=p1.testcode;");
            rs=ps.executeQuery();
            while (rs.next())
            {
                paramlist.add(new param(rs.getString("deptname"),rs.getString("testname"),rs.getString("parametername")));
            }
            tblparameter.getItems().setAll(paramlist);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void updateparameter(ActionEvent event) {
        if(!(txtparameter.getText().equals(p1.getParameter()))){
            try {
                    ps = con.prepareStatement("update test set parametername=? where parametername=?;");
                    ps.setString(1, p1.getParameter());
                    ps.execute();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else
        {
            new AlertBox("! No changes reuired.",event);
        }
    }
    int flag=1;
ObservableList testlist=FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initdept();
        deptlist.clear();
        initcolumns();
        criterialist.setAll("Department","Test","Parameter");
        cbsearchcriteria.getItems().setAll(criterialist);
        btnupdateparameter.setVisible(false);
        btnremoveparameter.setVisible(false);
        btncancel.setVisible(false);
        cbdept.setOnAction(event ->
        {
            try {
                testlist.clear();
                ps=con.prepareStatement("SELECT deptcode from department where deptname=?;");
                ps.setString(1, String.valueOf(cbdept.getSelectionModel().getSelectedItem()));
                rs=ps.executeQuery();
                while (rs.next())
                {
                    deptcode=rs.getInt("deptcode");
                }
                ps=con.prepareStatement("select  testname from test where deptcode=?;");
                ps.setInt(1,deptcode);
                rs=ps.executeQuery();
                while (rs.next())
                {
                    testlist.add(rs.getString("testname"));
                }
                cbtest.getItems().setAll(testlist);
                if(flag==-1)
                {
                     cbtest.setValue(null);
                     txtparameter.clear();
                    flag=1;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        cbtest.setOnAction(event ->
        {
            if(cbtest.getSelectionModel().getSelectedItem()!=null) {
                try {
                    ps = con.prepareStatement("select testcode from test where testname=?;");
                    flag=-1;
                    ps.setString(1, String.valueOf(cbtest.getSelectionModel().getSelectedItem()));
                    rs=ps.executeQuery();
                    while (rs.next())
                    {
                        testcode=rs.getInt("testcode");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        });
    }
    Connection con= DbHandler.getConnection();
    PreparedStatement ps;
    ResultSet rs;
    ObservableList deptlist= FXCollections.observableArrayList();
    public void initdept()
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
    public  void initcolumns()
    {
        coldept.setCellValueFactory(new PropertyValueFactory<>("dept"));
        coltest.setCellValueFactory(new PropertyValueFactory<>("test"));
        colparameter.setCellValueFactory(new PropertyValueFactory<>("parameter"));

    }
    public class param
    {
        String dept,test,parameter;
        public param(String dept,String test,String parameter)
        {
            this.dept=dept;
            this.parameter=parameter;
            this.test=test;
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

        public String getParameter() {
            return parameter;
        }

        public void setParameter(String parameter) {
            this.parameter = parameter;
        }
    }
}
