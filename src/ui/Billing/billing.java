package ui.Billing;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import db.DbHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import ui.patient.Patient;
import ui.support.AlertBox;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import static ui.support.GetStage.getStage;

public class billing implements Initializable {
    public int assistant;
    @FXML
    private JFXDatePicker txtinvoicedate;

    @FXML
    private JFXComboBox<?> txtpmode;

    @FXML
    private JFXTextField txtamt;

    @FXML
    private JFXTextField txtpaidamt;

    @FXML
    private JFXTextField txtdiscountamt;

    @FXML
    private JFXTextField txtunpaidamt;

    @FXML
    private JFXDatePicker txtsampledate;

    @FXML
    private JFXTimePicker txtsampletime;

    @FXML
    private JFXButton btnback;

    @FXML
    private JFXDatePicker txtreportdate;

    @FXML
    private JFXTimePicker txtreporttime;

    @FXML
    private JFXComboBox<?> txtdoctor;

    @FXML
    private JFXButton btnsavebill;

    @FXML
    private JFXButton btnclearbill;
    Connection con=DbHandler.getConnection();
    @FXML
    private JFXButton btnprintbill;
    int patientid=-1,billid=-1;
    int docid=-1;
    public ObservableList<Patient.Test> selecttest= FXCollections.observableArrayList();
    public ObservableList doclist=FXCollections.observableArrayList();
    public ObservableList paymode=FXCollections.observableArrayList();
    @FXML
    void clearall(ActionEvent event) {
clear_bill();
    }

    void clear_bill()
    {
        txtpmode.getSelectionModel().clearSelection();
        txtpaidamt.setText("0.0");
        txtdiscountamt.setText("0.0");
        txtsampledate.getEditor().clear();
        txtsampletime.getEditor().clear();
        txtreportdate.getEditor().clear();
        txtreporttime.getEditor().clear();
        txtdoctor.getSelectionModel().clearSelection();


    }
    @FXML
    void onback(ActionEvent event) {

        Scene scene = ((Node)event.getSource()).getScene();
        AnchorPane anchorPane = (AnchorPane) scene.lookup("#panepatient");
        anchorPane.toFront();
       System.out.println("in onback");
    }
    ArrayList<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
    @FXML
    public void printbill(ActionEvent event) throws JRException
    {

        JasperReport jasperReport = JasperCompileManager.compileReport("D:\\HealthCheckup\\src\\ui\\jrfxml\\MedicalReceipt.jrxml");
        HashMap<String,Object> haspmap;
        list.clear();
        Scene scene = ((Node)event.getSource()).getScene();
        AnchorPane patientanchor= (AnchorPane)scene.lookup("#panepatient");
        int i = 0;
        while (i < selecttest.size()) {
            haspmap = new HashMap<>();
            haspmap.put("testname",selecttest.get(i).getTestName());
            haspmap.put("price",selecttest.get(i).getPrice().doubleValue());
            haspmap.put("serialno", ++i);
//            if(i==selecttest.size())
//            {
              //  haspmap=new HashMap<>();
                haspmap.put("patientname",((JFXTextField)patientanchor.lookup("#txtname")).getText() );
                haspmap.put("age",(Float.valueOf(( (JFXComboBox)patientanchor.lookup("#txtyear")).getValue().toString())));
                haspmap.put("sex",((JFXComboBox)patientanchor.lookup("#txtsex")).getValue().toString() );
                if(txtdoctor.getValue()==null) {
                    haspmap.put("doctorname", " ");
                }
                else {
                    haspmap.put("doctorname",txtdoctor.getValue().toString());
                }
            //    list.add(haspmap);
             //   haspmap=new HashMap<>();
                haspmap.put("totalamt",Double.parseDouble( txtamt.getText()));
                haspmap.put("paidamt",Double.parseDouble(txtpaidamt.getText()));
                haspmap.put("discountamt",Double.parseDouble(txtdiscountamt.getText()));
                haspmap.put("pendingamt", txtunpaidamt.getText());
            //}
            list.add(haspmap);
        }


        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);
        JasperPrint print = JasperFillManager.fillReport(jasperReport, null, beanColDataSource);
        JasperViewer.viewReport(print,false);
        String path=System.getProperty("user.dir");
    }
    @FXML
    void savebill(ActionEvent event) {
    pcontroller.patient_entry();
    bill_entry(event);
    btnsavebill.setDisable(true);
    btnback.setVisible(false);
    btnclearbill.setDisable(true);
    btnprintbill.setDisable(false);
    }
   public void bill_entry(ActionEvent event)
    {
        Scene scene = ((Node)event.getSource()).getScene();
        String patientname= ((JFXTextField) scene.lookup("#txtname")).getText();
//        excAction("create table billing(billid int(100) primary key AUTO_INCREMENT, totalcost float(30,5)
//        not null,paid float(30,5) not null,unpaid float(30,5) not null,discount float(30,5) not null,
//        pmode varchar(50),docid int(100),patientid int(100),foreign key(docid) references doctor(docid),
//        foreign key(patientid) references patient(patientid));");
        try {
            PreparedStatement ps=con.prepareStatement("select patientid from patient where name=? order by patientid desc limit 1;") ;
            ps.setString(1,patientname);
            ResultSet rs=ps.executeQuery();
            while (rs.next())
            {
               patientid= rs.getInt("patientid");
            }
            rs=null;

            ps=con.prepareStatement("insert into billing(totalcost,paid,unpaid,discount,pmode,docid,patientid,invoiced,assistantid) values(?,?,?,?,?,?,?,?,?);");
            ps.setFloat(1, Float.parseFloat(txtamt.getText()));
            ps.setFloat(2, Float.parseFloat(txtpaidamt.getText()));
            ps.setFloat(3, Float.parseFloat(txtunpaidamt.getText()));
            ps.setFloat(4, Float.parseFloat(txtdiscountamt.getText()));
            ps.setString(5, (String) txtpmode.getSelectionModel().getSelectedItem());
            if(docid!=-1) {
                ps.setInt(6, docid);
            }else {
                ps.setNull(6, Types.INTEGER);
            }
            ps.setInt(7,patientid);
            ps.setDate(8, Date.valueOf(DbHandler.date()));
            ps.setInt(9,assistant);
            ps.execute();

             ps=con.prepareStatement("select billid from billing b1 inner join patient p1 on p1.patientid=b1.patientid and p1.name=? order by billid desc limit 1;") ;
            ps.setString(1,patientname);
           rs=ps.executeQuery();
            while (rs.next())
            {
                billid= rs.getInt("billid");
            }
            //            excAction("create table sample(receivedd date,receivedt time,billid int(100) not null,reportdate date,
//            reporttime time,foreign key(billid) references billing(billid))");
            ps=con.prepareStatement("insert into sample(receivedd,receivedt,reportdate,reporttime,billid) values (?,?,?,?,?);");
            if(txtsampledate.getValue()!=null) {
                ps.setDate(1, Date.valueOf(txtsampledate.getValue()));
            }
            else
            {
                ps.setNull(1, Types.DATE);
            }
            if(txtsampletime.getValue()!=null)
            ps.setTime(2, Time.valueOf(txtsampletime.getValue().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
               else
                   ps.setNull(2,Types.TIME);
               if(txtreportdate.getValue()!=null)
            ps.setDate(3, Date.valueOf(txtreportdate.getValue()));
               else
               ps.setNull(3,Types.DATE);
               if(txtreporttime.getValue()!=null)
            ps.setTime(4, Time.valueOf(txtreporttime.getValue().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
               else
                   ps.setNull(4,Types.TIME);
            ps.setInt(5,billid);
            ps.execute();
            order_entry();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void order_entry()
    {
        try {
                int i = 0;
                while (i < selecttest.size()) {
                    PreparedStatement ps = con.prepareStatement("insert into report(testname,billid) values(?,?);");
                    ps.setString(1,selecttest.get(i).getTestName());
                    ps.setInt(2,billid);
                    ps.execute();
                    i++;
                }

            } catch(SQLException e){
                e.printStackTrace();
            }
        }
    Patient pcontroller;
    public void setTotalamt(float totalamount,Patient patient)
    {
        txtamt.setText(String.valueOf(totalamount));
        txtunpaidamt.setText(String.valueOf(totalamount));
        pcontroller=patient;
    }
    public  void doc_fetch()
    {
        txtdoctor.getItems().clear();
        doclist.clear();
        try {
            PreparedStatement ps=con.prepareStatement("Select docname from doctor;");
            ResultSet rs=ps.executeQuery();
            while (rs.next())
            {
                doclist.add(rs.getString("docname"));
            }
            txtdoctor.getItems().setAll(doclist);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("On initialize of billing");
        doc_fetch();
        System.out.println(resources);
        System.out.println(location);
        paymode.setAll("CASH","CHEQUE","PAYMENT CARD");
        txtpmode.getItems().setAll(paymode);
        txtinvoicedate.setValue(LocalDate.now());
        txtsampledate.setValue(LocalDate.now());
        txtsampletime.setValue(LocalTime.now());

        txtdoctor.setOnAction(event -> {
            try {
                if(txtdoctor.getValue()!=null&& txtdoctor.getValue()!="") {
                    PreparedStatement ps = con.prepareStatement("Select docid from doctor where docname=?;");
                    ps.setString(1, String.valueOf(txtdoctor.getSelectionModel().getSelectedItem()));
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        docid = rs.getInt("docid");
                    }
                }
                else
                {
                   docid=-1;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        txtpaidamt.textProperty().addListener((observable, oldValue, newValue) ->        {
            if(!(txtpaidamt.getText().isEmpty() || txtdiscountamt.getText().isEmpty()))
            {
            if(Float.valueOf(txtpaidamt.getText())<=Float.valueOf( txtamt.getText()))
            {
                if((Float.valueOf(txtdiscountamt.getText())+Float.valueOf(txtpaidamt.getText())<=Float.valueOf(txtamt.getText())))
                {
                    txtunpaidamt.setText(String.valueOf(Float.valueOf(txtamt.getText())-(Float.valueOf(txtdiscountamt.getText())+Float.valueOf(txtpaidamt.getText()))));
                }
                else {
//                    System.out.println("");
                    new AlertBox("Error","Given paid amount is geater than total amt after discount");
                    txtpaidamt.setText("0.00");
                }
            }
            else
            {
//                System.out.println("Paid cant be greater than total amt");
                new AlertBox("Error","Paid cant be greater than total amt");
                txtpaidamt.setText("0.00");
            }
        }});
        txtdiscountamt.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if(!(txtdiscountamt.getText().isEmpty()|| txtpaidamt.getText().isEmpty()))
            {
            if(Float.valueOf(txtdiscountamt.getText())<=Float.valueOf(txtamt.getText()))
            {
                if(Float.valueOf(txtamt.getText())<=(Float.valueOf(txtpaidamt.getText())+Float.valueOf(txtdiscountamt.getText())))
                {
                    txtpaidamt.setText(String.valueOf(Float.valueOf(txtamt.getText())-Float.valueOf(txtdiscountamt.getText())));
                    txtunpaidamt.setText("0.00");
                }
                else
                {
                    if(Float.valueOf(txtamt.getText())>(Float.valueOf(txtpaidamt.getText())+Float.valueOf(txtdiscountamt.getText())))
                    {
                        txtunpaidamt.setText(String.valueOf(Float.valueOf(txtamt.getText())-(Float.valueOf(txtdiscountamt.getText())+Float.valueOf(txtpaidamt.getText()))));
                    }
                }
            }
            else
            {
                new AlertBox("Error","Discount cant be greater than total  ammount");
                txtdiscountamt.setText("0.00");
            }
        }});


    }
    public class billdata
    {

    }
}
