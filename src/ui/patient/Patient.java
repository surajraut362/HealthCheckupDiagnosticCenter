package ui.patient;

import com.jfoenix.controls.*;
import db.DbHandler;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import ui.Billing.billing;
import ui.support.AlertBox;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.ResourceBundle;

import static ui.support.GetStage.getStage;

public class Patient implements Initializable {
   public int assistant;
    FXMLLoader loader;
    BorderPane borderbilling=null;
    @FXML
    private BorderPane patientborder;

    @FXML
    private AnchorPane panetop;
    @FXML
    private AnchorPane paneright;
    @FXML
    private BorderPane panebilling;

    @FXML
    private AnchorPane panepatient;

    @FXML
    private JFXButton btnsearch;
    @FXML
    private JFXButton btnpreview;

    @FXML
    private JFXComboBox<?> txtsearchby;

    @FXML
    private JFXTextField txtsearch;
    @FXML
    private TableView<Test> tbltest;

    @FXML
    private TableColumn<Test,String > coldept;

    @FXML
    private TableColumn<Test, String> coltestname;

    @FXML
    private TableColumn<Test, Float> coltestprice;

    @FXML
    private TableColumn<Test, CheckBox> colselectall;

    @FXML
    private CheckBox chkselectall;

    @FXML
    private JFXButton btnpatientclear;

    @FXML
    private JFXButton btneditpatient;

    @FXML
    private FontAwesomeIconView glyidverify;

    @FXML
    private JFXTextField txtname;

    @FXML
    private JFXComboBox<?> txtsex;

    @FXML
    private JFXComboBox<?> txtyear;
    billing controller=null;
    @FXML
    private JFXTextField txtmobileno;

    @FXML
    private JFXTextField txtaddress;

    @FXML
    private JFXTextField txtstate;
    public float totalamt= Float.valueOf(0);
    @FXML
    private JFXTextField txtemail;

    @FXML
    private JFXTextField txtdetail;

    @FXML
    private JFXButton btnsavepatient;

    @FXML
    private JFXButton btnnextbilling;

    @FXML
    private JFXButton btnshowtestall;
    int deptcode=-1,testcode=-1;
    @FXML
    private JFXButton btncleartest;
    ObservableList title= FXCollections.observableArrayList("Mr./Male","Ms./Female");
    ObservableList<Test> testlist=FXCollections.observableArrayList();
    ObservableList<CheckBox> checkBoxes=FXCollections.observableArrayList();
    ObservableList<Test>selectedlist=FXCollections.observableArrayList();
    @FXML
    void clearpatient(ActionEvent event) {
        clear_patient();
    }
    @FXML
    void previewtest(ActionEvent event)
    {
        selected_test(tbltest);
        v.getItems().setAll(selectedlist);
        VBox vBox=new VBox();
        JFXDialogLayout d=new JFXDialogLayout();
        JFXDialog jfxDialog = new JFXDialog((StackPane) getStage(event).getScene().getRoot(),d,JFXDialog.DialogTransition.TOP);
        Label l=new Label("Selected Test:");
        JFXButton b=new JFXButton("OK");
        vBox.getChildren().setAll(v,b);
        d.setBorder(Border.EMPTY);
        d.prefHeight(100);
        d.prefWidth(300);
        d.setBody(vBox);
        b.layoutXProperty().setValue(250);
        b.layoutYProperty().setValue(90);
        d.setHeading(l);
        b.setOnAction(event1 -> {
            jfxDialog.close();
        });
        jfxDialog.show();
    }
    public void hash__detail(HashMap hashMap)
    {
        hashMap.put("patientname",txtname.getText());
        hashMap.put("age",Float.parseFloat(txtyear.getValue().toString()));
        hashMap.put("sex",txtsex.getValue());
    }
    void selected_test(TableView<Test> tbl)
    {
        selectedlist.clear();
        totalamt=0;
        int row=-1;
        for (CheckBox c :
                checkBoxes) {
          if(c.isSelected()==true)
          {
           row=checkBoxes.indexOf(c);
           selectedlist.add(testlist.get(row));
           totalamt+=testlist.get(row).getPrice();
          }
        }
        row=-1;

    }
    TableView<Test> v=new TableView<Test>();
    TableColumn<Test,String> col1=new TableColumn("Department");
    TableColumn<Test,String> col2=new TableColumn("TestName");
    TableColumn<Test,String> col3=new TableColumn("Price");

    @FXML
    void cleartest(ActionEvent event) {



//    }
//        clear_test();
    }


    @FXML
    void editpatient(ActionEvent event) {
        activation(false);
    }

    @FXML
    void proceed(ActionEvent event) {
    try {
        selected_test(tbltest);
        if(!selectedlist.isEmpty()) {
            controller.selecttest.setAll(selectedlist);
            Patient p = this;
            controller.assistant=assistant;
            controller.setTotalamt(totalamt, p);
            panebilling.setCenter(borderbilling);
            panebilling.toFront();
        }
        else
        {
            new AlertBox("Select Any Test ",event);
        }
    }catch (Exception e)
    {
        e.printStackTrace();
    }


    }
    public void loadpane(int assistant)
    {
        this.assistant=assistant;
        try {
            loader= new FXMLLoader(getClass().getResource("/ui/Billing/Billing1.fxml"));
            borderbilling=(BorderPane) loader.load();
            controller=loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void patient_entry()
    {
        System.out.println("Patient Details");
//        excAction("create table patient(patientid int(100) primary key AUTO_INCREMENT,name varchar(30) not null," +
//                "age float(3,2) not null,sex varchar(10) not null,address varchar(150),state varchar(50)," +
//                "email varchar(80),mob varchar(15) not null,detail varchar(200),dateofreg date not null, " +
//                "assistant varchar(20) not null,foreign key(assistant) references assistant(username));");
        try {
            PreparedStatement ps=con.prepareStatement("insert into patient(name,age,sex,mob,address,state,email,detail,dateofreg)  values(?,?,?,?,?,?,?,?,?) ");
            ps.setString(1,txtname.getText());
            ps.setFloat(2,Float.valueOf((String) txtyear.getValue()));
            ps.setString(3,gender);
            ps.setString(4,txtmobileno.getText());
            ps.setString(5,txtaddress.getText());
            ps.setString(6,txtstate.getText());
            ps.setString(7,txtemail.getText());
            ps.setString(8,txtdetail.getText());
            ps.setDate(9, Date.valueOf(DbHandler.date()));
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void savepatient(ActionEvent event) {
//    if(txtsex.getValue()!=null && txtname.getText()!=null&&txtmobileno.getText()!=null&&txtyear.getValue()!=null) {
        activation(true);
//    }
//    else {
//      new AlertBox("Some required Fields are empty",event);
//    }

    }

    @FXML
    void search(ActionEvent event) {
        testfetch(txtsearchby.getSelectionModel().getSelectedIndex());
    }

    @FXML
    void showAll(ActionEvent event) {
        tbltest.getItems().clear();
        tbltest.getItems().setAll(testlist);
    }
    void clear_patient()
    {
        txtsex.getEditor().clear();
        txtname.clear();
        txtyear.getEditor().clear();
        txtmobileno.clear();
        txtaddress.clear();
        txtstate.clear();
        txtemail.clear();
        txtdetail.clear();

    }
    void clear_test()
    {
        txtsearchby.getItems().clear();
        txtsearch.clear();

    }
     void activation(Boolean val)
    {
        txtsex.setDisable(val);
        txtname.setDisable(val);
        txtyear.setDisable(val);
        txtmobileno.setDisable(val);
        txtaddress.setDisable(val);
        txtstate.setDisable(val);
        txtemail.setDisable(val);
        txtdetail.setDisable(val);
        btnpatientclear.setDisable(val);
        btnsavepatient.setVisible(!val);
        btnsavepatient.setDisable(val);
        btneditpatient.setVisible(val);
        tbltest.setDisable(!val);
        paneright.setDisable(!val);
        panetop.setDisable(!val);
    }
    Connection con= DbHandler.getConnection();

    void testfetch(int index) {
        tbltest.getItems().clear();
        int i = 0;
        PreparedStatement ps;
        ResultSet rs;
        switch (index) {

            case 0:
//                    ps = con.prepareStatement("Select * from test where deptcode=(Select deptcode from department where deptname=?)");
//                    ps.setString(1, txtsearch.getText());
//                    rs = ps.executeQuery();
//
//                    while (rs.next()) {
//                        CheckBox cb = new CheckBox("");
//                        checkBoxes.add(cb);
//                        testlist.add(new Test(txtsearch.getText().toUpperCase(), rs.getString("testname"), rs.getFloat("price"), cb));
//                    }
//                    tbltest.getItems().setAll(testlist);
//                    rs=null;
                i=0;
                while (i <testlist.size()) {
                   if( testlist.get(i).getDepartment().toUpperCase().equals(txtsearch.getText().toUpperCase()))
                       tbltest.getItems().add(testlist.get(i));
                    i++;
                }
                break;
            case 1:
//                    ps = con.prepareStatement("select price,testname,deptname from test t1 inner join department d1 on t1.testname=? and d1.deptcode=(Select deptcode from test where testname=?);");
//                    ps.setString(1, txtsearch.getText());
//                    ps.setString(2,txtsearch.getText());
//                    rs = ps.executeQuery();
//                    while (rs.next()) {
//
//                        CheckBox cb = new CheckBox("");
//                        checkBoxes.add(cb);
//                        testlist.add(new Test(rs.getString("deptname"), rs.getString("testname"), rs.getFloat("price"), cb));
//                    }
//                    tbltest.getItems().setAll(testlist);
//                    rs=null;
                i=0;
                while (i <testlist.size()) {
                    if( testlist.get(i).getTestName().toUpperCase().equals(txtsearch.getText().toUpperCase()))
                        tbltest.getItems().add(testlist.get(i));
                    i++;
                }
                break;
        }
    }

    String gender;
    ObservableList searchingby=FXCollections.observableArrayList("Department","TestName");
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        activation(false);
        coldept.setCellValueFactory(new PropertyValueFactory<>("department"));
        coltestname.setCellValueFactory(new PropertyValueFactory<>("TestName"));
        coltestprice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colselectall.setCellValueFactory(new PropertyValueFactory<>("checkbox"));
        v.getColumns().setAll(col1,col2,col3);
       coldept.setPrefWidth( tbltest.getPrefWidth()/4);
       coltestname.setPrefWidth(tbltest.getPrefWidth()/4);
       coltestprice.setPrefWidth(tbltest.getPrefWidth()/4);
       colselectall.setPrefWidth(tbltest.getPrefWidth()/4);
        col1.setCellValueFactory(new PropertyValueFactory<>("department"));
        col2.setCellValueFactory(new PropertyValueFactory<>("TestName"));
        col3.setCellValueFactory(new PropertyValueFactory<>("price"));
        txtsearchby.getItems().setAll(searchingby);
        txtsex.getItems().setAll(title);
        try {
            testlist.clear();
            PreparedStatement ps = con.prepareStatement("Select deptname,testname,price from department d1  inner join test t1  on d1.deptcode=t1.deptcode");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CheckBox cb = new CheckBox("");
                checkBoxes.add(cb);
                testlist.add(new Test(rs.getString("deptname"), rs.getString("testname"), rs.getFloat("price"), cb));
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        txtsex.setOnAction(event ->
        {
        switch (txtsex.getSelectionModel().getSelectedIndex())
        {
            case 0:gender="Male";
            break;
            case 1:gender="Female";
            break;
        }
        });


        chkselectall.setOnAction(event ->
        {
            int i=0;
            if(chkselectall.isSelected()==true) {
                while(i<tbltest.getItems().size()) {
                    System.out.println(tbltest.getItems().size());
                    ((CheckBox)tbltest.getColumns().get(3).getCellObservableValue(i).getValue()).setSelected(true);
//                    tbltest.getColumns().get(3).getCellObservableValue(i).getValue();
                        i++;
                }
            }
            else {
                if (chkselectall.isSelected() == false) {
                    while(i<tbltest.getItems().size()) {
                        System.out.println(tbltest.getItems().size());
                        ((CheckBox)tbltest.getColumns().get(3).getCellObservableValue(i).getValue()).setSelected(false);
//                    tbltest.getColumns().get(3).getCellObservableValue(i).getValue();
                        i++;
                    }
                }
            }
        });
    }
    public  class Test
    {
        String department,TestName;
        Float price;
        CheckBox checkbox;
        public Test(String department,String TestName,Float price,CheckBox checkbox)
        {

            this.department=department;
            this.TestName=TestName;
            this.price=price;
            this.checkbox=checkbox;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public void setTestName(String testName) {
            TestName = testName;
        }

        public void setPrice(Float price) {
            this.price = price;
        }

        public void setCheckbox(CheckBox checkbox) {
            this.checkbox = checkbox;
        }

        public String getDepartment() {
            return department;
        }

        public String getTestName() {
            return TestName;
        }

        public Float getPrice() {
            return price;
        }

        public CheckBox getCheckbox() {
            return checkbox;
        }
    }

}

