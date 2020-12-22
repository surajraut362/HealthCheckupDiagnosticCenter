package ui.Admin.Test.addTest;

import com.jfoenix.controls.*;
import db.DbHandler;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import javafx.scene.layout.BorderPane;
import org.omg.CORBA.TIMEOUT;
import ui.support.AlertBox;
import ui.test.TestController;

import javax.xml.soap.Text;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class Addtest implements Initializable {
    Connection con=DbHandler.getConnection();
    @FXML
    private JFXComboBox<?> dept;

    @FXML
    private JFXComboBox<String> testtype;



    @FXML
    private JFXTextField testname;


    @FXML
    private JFXTextField testcost;

    @FXML
    private JFXTextField testprice;

    @FXML
    private JFXCheckBox chknounit;


    @FXML
    private JFXComboBox<String> txtsex;

    @FXML
    private JFXTextField txtunit;

@FXML
private  JFXButton btncleartest;
    @FXML
    private JFXCheckBox chkforallages;

    @FXML
    private TextField txtfyear;

    @FXML
    private TextField txtfmonth;
    @FXML
    private JFXButton btnaddnormal;
    @FXML
    private TextField txttyear;
    ObservableList deptlist = FXCollections.observableArrayList();
    ObservableList typelist = FXCollections.observableArrayList();
    ObservableList listgender = FXCollections.observableArrayList("Male","Female");
    @FXML
    private TextField txttmonth;
    @FXML
    private TextField txtranget;

    @FXML
    private TextField txtrangef;

    @FXML
    private JFXButton btnsave;
    @FXML
    private JFXButton btnnewdept;

    @FXML
    private JFXButton btnnewtype;
    @FXML
    private JFXButton btnclear;
    @FXML
    private JFXButton btnsavetype;
    @FXML
    private JFXButton btnedittest;
    @FXML
    private JFXButton btnsavetest;
    String adminname="root";
    @FXML
    private JFXButton btnsavedept;
    int testcode=-1;
    int deptcode=-1;
    int parametercode=-1;
    int flag=-1;
    @FXML
    void cleartest(ActionEvent event)
    {
        cleartestdetail();
        reinit();

    }
    @FXML
    void savetest(ActionEvent event)
    {

        try {
            System.out.println(testtype.getValue());


            if (testcode != -1) {
                PreparedStatement ps=con.prepareStatement("Select * from parameter;");
                ResultSet rs= ps.executeQuery();
                flag=-1;
                while (rs.next())
                {
                    if(testname.getText()==rs.getString("parametername"))
                        flag=1;
                }
                if(flag!=1) {
                    ps = con.prepareStatement("insert into parameter(parametername,dateofadd,addedby,testcode) values(?,?,?,?) ; ");
                    ps.setString(1, testname.getText());
                    ps.setDate(2, Date.valueOf(DbHandler.date()));
                    ps.setString(3, "root");
                    ps.setInt(4, testcode);
                    ps.execute();
                    testcode = -1;
                    deptcode = -1;
                    ps = con.prepareStatement("Select parametercode from parameter where parametername=?; ");
                    ps.setString(1, testname.getText());
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        parametercode= rs.getInt("parametercode");

                    }
                    testactive(true);
                    btnsavetest.setVisible(false);
                    btncleartest.setVisible(false);
                    btnedittest.setVisible(true);
                }
                else
                {
                    new AlertBox("Error","TestName Already exists");
                    flag=-1;
                }


            }
            else
            {
                new AlertBox("Error","Select proper department and TestTypes");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    void edittest(ActionEvent event)
    {
        testactive(false);
        btnsavetest.setVisible(true);
        btnedittest.setVisible(false);
    }
    @FXML
    void addnormal(ActionEvent event)
    {
//        excAction("create table normalvalue(normalcode int(100) primary key AUTO_INCREMENT,sex varchar(50) not null,
//        fromage float(5,5) not null,toage float(5,5) not null ,rangefrom float(10,5),rangeto float(10,5),unit varchar(20),
//        testcode int(100), foreign key(testcode) references test(testcode));");
        try {
            PreparedStatement ps=con.prepareStatement("insert into normalvalue(sex,fromage,toage,rangefrom,rangeto,unit,parametercode) values(?,?,?,?,?,?,?);");
            ps.setString(1,txtsex.getValue());
            ps.setFloat(2, Float.parseFloat(txtfyear.getText()));
            ps.setFloat(3, Float.parseFloat(txttyear.getText()));
            ps.setFloat(4, Float.parseFloat(txtrangef.getText()));
            ps.setFloat(5, Float.parseFloat(txtranget.getText()));
            ps.setString(6,txtunit.getText());
            ps.setInt(7,parametercode);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void testactive(Boolean val)
    {
        btnsavedept.setDisable(val);
        btnnewdept.setDisable(val);
        btnnewtype.setDisable(val);
        btnsavetype.setDisable(val);
        dept.setDisable(val);
        testtype.setDisable(val);
        testname.setDisable(val);
        testcost.setDisable(val);
        testprice.setDisable(val);
        chkforallages.setDisable(!val);
        chknounit.setDisable(!val);
        txtsex.setDisable(!val);
        txtunit.setDisable(!val);
        txtfyear.setDisable(!val);
        txtfmonth.setDisable(!val);
        txttmonth.setDisable(!val);
        txttyear.setDisable(!val);
        txtrangef.setDisable(!val);
        txtranget.setDisable(!val);
        btnaddnormal.setDisable(!val);


    }
    @FXML
    void clearAll(ActionEvent event) {
        init();

    }

    @FXML
    void newTestType(ActionEvent event) {
        btnnewtype.setVisible(false);
        btnsavetype.setVisible(true);
        testtype.setEditable(true);
        testtype.getEditor().clear();
        testprice.setEditable(true);
        testcost.setEditable(true);



    }

    @FXML
    void newdept(ActionEvent event) {
        testtype.getItems().clear();
        testtype.getEditor().clear();
        dept.setEditable(true);
        dept.getCursor();
        btnsavedept.setVisible(true);
        btnnewdept.setVisible(false);
        btnnewtype.setVisible(true);
        btnsavetype.setVisible(false);



    }

    @FXML
    void saveAll(ActionEvent event) {
//        excAction("create table testtype(typecode int(100) primary key AUTO_INCREMENT,typename varchar(100) unique key not null," +
//                " addedby varchar(20) not null,deptcode int(100) not null ,foreign key(addedby) references admin(username)," +
//                "foreign key(deptcode) references department(deptcode));");


//        (testcode int(100) primary key AUTO_INCREMENT,testname varchar(100) unique key not null,cost float(30,5) not null,
//        price float(30,5) not null,reportday int(10),reporttime time,dateofadd date not null, addedby varchar(20) not null,
//        typecode int(100) not null ,foreign key(addedby) references admin(username),foreign key(typecode) references testtype(typecode));");

//        excAction("create table normalvalue(normalcode int(100) primary key AUTO_INCREMENT,sex varchar(50) not null,
//        fromage float(5,5) not null,toage float(5,5) not null ,rangefrom float(10,5),rangeto float(10,5),unit varchar(20),
//        testcode int(100), foreign key(testcode) references test(testcode));");




    }


    @FXML
    void openTest(ActionEvent event) {

        Scene scene = ((Node)event.getSource()).getScene();
        AnchorPane anchorPane = (AnchorPane) scene.lookup("#panetest");

        anchorPane.toFront();
        System.out.println("in open test");
        clearAll(event);
    }
    void  clearnormaldetail()
    {
        txtsex.setEditable(false);
        txtsex.getItems().setAll(listgender);
        chknounit.setSelected(false);
        txtsex.getEditor().clear();
        txtunit.clear();
        chkforallages.setSelected(false);
        txtfyear.clear();
        txttyear.clear();
        txtfmonth.clear();
        txttmonth.clear();
        txtrangef.clear();
        txtranget.clear();
    }
    void cleartestdetail()
    {
        deptlist.clear();
        dept.getItems().clear();
        dept.setEditable(false);
        testtype.setValue(null);
        testtype.setEditable(false);
        testtype.getItems().clear();
        btnnewdept.setVisible(true);
        btnnewtype.setVisible(true);
        btnsavedept.setVisible(false);
        btnsavetype.setVisible(false);
        testname.clear();
        testcost.clear();
        testprice.clear();
        testcost.setEditable(false);
        testprice.setEditable(false);
    }
    public void init()
    {
        cleartestdetail();
        btncleartest.setVisible(true);
        btnedittest.setVisible(false);
        btnsavetest.setVisible(true);
        clearnormaldetail();
        testactive(false);
       reinit();


    }
    void reinit()
    {
        PreparedStatement ps= null;
        try {
            deptlist.clear();
            ps = con.prepareStatement("Select * from department;");
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                deptlist.add(rs.getString("deptname"));
            }
            dept.getItems().setAll(deptlist);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void savedept(ActionEvent event) {
        System.out.println("In saving dept");
        PreparedStatement ps= null;
        try {
            ps=con.prepareStatement("Select * from department ;");
            ResultSet rs=ps.executeQuery();
            flag =-1;
            String s= dept.getEditor().getText().toUpperCase();
            while (rs.next()) {
                String s1= rs.getString("deptname").toUpperCase();
                if(s==s1)
                {
                    System.out.println("suraj");
                    flag=1;

                }

            }
            if(flag!=1) {
                ps = con.prepareStatement("Insert into department(deptname,addedby) values(?,?);");
                ps.setString(1, dept.getEditor().getText());
                ps.setString(2, adminname);
                ps.execute();
                init();
            }
            else
            {
                new AlertBox("Error"," Department already exists");
                flag=-1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @FXML
    void savetype(ActionEvent event) {

        System.out.println("savetype");
        PreparedStatement ps= null;
        try {
            ps = con.prepareStatement("Select * from test ;");

            ResultSet rs = ps.executeQuery();
            flag=-1;
            while (rs.next()) {
                System.out.println(testtype.getValue());
                String s=rs.getString("testname").toUpperCase();
                String s1= testtype.getValue().toUpperCase();
                if (s== s1)
                {
                    System.out.println("Helloworld");
                    System.out.println(rs.getString("testname"));
                    flag = 1;
                }
            }

            if (flag ==- 1) {
                ps = con.prepareStatement("insert into test(testname,cost,price,addedby,deptcode) values(?,?,?,?,?);");
                ps.setString(1, testtype.getEditor().getText());
                ps.setFloat(2, Float.parseFloat(testcost.getText()));
                ps.setFloat(3, Float.parseFloat(testprice.getText()));
                ps.setString(4, adminname);
                ps.setInt(5,deptcode);
                ps.execute();
                System.out.println("Insertion of testtype");
//                testtype.setEditable(false);
//                btnsavetype.setVisible(false);
//                btnnewtype.setVisible(true);
                init();


            } else
            {
                new AlertBox("Error","Testtype Already exists");
                flag=-1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void departmentchange(ActionEvent event) {

//            testtype.getEditor().clear();
//            testtype.setEditable(false);
//            btnsavetype.setVisible(false);
//            btnnewtype.setVisible(true);
//            typelist.clear();
//
//            try {
//                if (btnsavedept.isVisible() == true)
//                    if (dept.getSelectionModel().getSelectedItem().toString() == dept.getValue()) {
//                        dept.setEditable(false);
//                        btnsavedept.setVisible(false);
//                        btnnewdept.setVisible(true);
//                    }
//                System.out.println(dept.getValue());
//                PreparedStatement ps = con.prepareStatement("Select * from department where deptname=?");
//                ps.setString(1, String.valueOf(dept.getValue()));
//                ResultSet rs = ps.executeQuery();
//                while (rs.next()) {
//                    deptcode = rs.getInt("deptcode");
//                }
//
//                ps = con.prepareStatement("Select * from testtype where deptcode='" + deptcode + "';");
//                rs = ps.executeQuery();
//                while (rs.next()) {
//                    typelist.add(rs.getString("typename"));
//                }
//                testtype.getItems().setAll(typelist);
//
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//
//            }
        try {
            if (btnsavedept.isPressed() == true) {
                savedept(event);
            } else {
//                if (dept.isEditable() == true || btnsavedept.isVisible() == true) {
//                    cleartest(event);
//                    return;
//                }
//                if(dept.isEditable()==true)
//                {
//                    dept.getItems().removeAll();
//                }
//                while(deptlist.size())
                System.out.println("welcome dept");
                typelist.clear();
//            if(btnsavedept.isVisible()==true)
//            {
//                if(va)
//            }

                PreparedStatement ps = con.prepareStatement("Select * from department where deptname=?");
                ps.setString(1, String.valueOf(dept.getValue()));
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    deptcode = rs.getInt("deptcode");
                }


                ps = con.prepareStatement("Select * from test where deptcode='" + deptcode + "';");

                rs = ps.executeQuery();
                while (rs.next()) {
                    typelist.add(rs.getString("testname"));
                }
                testtype.getItems().setAll(typelist);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
        void testtypechange() {
            System.out.println("welcome type");
//                if (testtype.getValue() != null) {
//                    try {
//                        if(btnsavetype.isVisible()==true)
//                            if (testtype.getSelectionModel().getSelectedItem().toString() == testtype.getValue()) {
//
//                                testtype.setEditable(false);
//                                btnsavetype.setVisible(false);
//                                btnnewtype.setVisible(true);
//                            }
//                    System.out.println(testtype.getValue());
//
//            }


//            if (btnsavetype.isVisible() == true) {
//                init();
//            } else {
                try {
                    PreparedStatement ps = con.prepareStatement("Select testcode from test where testname=?");
                    ps.setString(1, testtype.getValue());
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        testcode = rs.getInt("testcode");
                    }


                } catch (SQLException e) {
                    e.printStackTrace();
//    }
                }
            }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
       init();
       dept.setOnAction(event -> {
           departmentchange(event);
       }
               );
       testtype.setOnAction(event ->{
           testtypechange();
       } );
//        dept.valueProperty().addListener(new ChangeListener<Object>() {
//            @Override
//            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
//
//                departmentchange();
//            }
//        });
//        testtype.valueProperty().addListener(new ChangeListener<Object>() {
//            @Override
//            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
//                testtypechange();
//            }});
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
                    txtfyear.setText("0");
                    txttyear.setText("150");
                    txtfyear.setDisable(true);
                    txttyear.setDisable(true);
                    txtfmonth.setDisable(true);
                    txttmonth.setDisable(true);
                }
                else
                {
                    txtfyear.clear();
                    txttyear.clear();
                    txtfyear.setDisable(false);
                    txttyear.setDisable(false);
                    txtfmonth.setDisable(false);
                    txttmonth.setDisable(false);
                }
            }
        });



    }
}
