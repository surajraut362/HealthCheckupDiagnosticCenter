package ui.pendingReport;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import db.DbHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import ui.support.AlertBox;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static db.DbHandler.date;
import static java.sql.Types.NULL;

public class pendingReport implements Initializable {

    @FXML
    private JFXDatePicker txtdatefrom;

    @FXML
    private JFXDatePicker txtdateto;

    @FXML
    private JFXButton btnfetch;

    @FXML
    private JFXTextField txtsearch;

    @FXML
    private JFXComboBox<String> cbsearchby;

    @FXML
    private JFXButton btnsearch;

    @FXML
    private JFXComboBox<?> cbsortby;

    @FXML
    private TableView<report> tblreport;

    @FXML
    private TableColumn<?, ?> colbillnum;

    @FXML
    private TableColumn<?, ?> colpatientname;

    @FXML
    private TableColumn<?, ?> colage;

    @FXML
    private TableColumn<?, ?> colgender;

    @FXML
    private TableColumn<?, ?> colmobno;

    @FXML
    private TableColumn<?, ?> coldoctor;
    @FXML
    void onfetch(ActionEvent event) {
        reportlist.clear();
        txtsearch.clear();
        cbsearchby.setValue(null);
        initTable(Date.valueOf(txtdatefrom.getValue()),Date.valueOf(txtdateto.getValue()));

    }

    @FXML
    void onsearch(ActionEvent event) {
        list.clear();
        Iterator<report>i= reportlist.iterator();

        switch(cbsearchby.getSelectionModel().getSelectedIndex())
       {

           case 0:
                     while(i.hasNext())
                    {
                        report val=i.next();
                       if(txtsearch.getText().equals(String.valueOf( val.getBillid())))
                       {
                           list.add(val);
                       }
                    }
                     tblreport.getItems().setAll(list);
                     break;
           case 1:

               while(i.hasNext())
               {
                   report val=i.next();
                   if(txtsearch.getText().toUpperCase().equals( val.getPatientname().toUpperCase()))
                   {
                       list.add(val);
                   }
               }
               tblreport.getItems().setAll(list);
               break;
           case 2:

               while(i.hasNext())
               {
                   report val=i.next();
                   if(txtsearch.getText().equals( val.getMobileno()))
                   {
                       list.add(val);
                   }
               }
               tblreport.getItems().setAll(list);
               break;
           case 3:
               while(i.hasNext())
               {
                   report val=i.next();
                   if(txtsearch.getText().toUpperCase().equals( val.getDoctor().toUpperCase()))
                   {
                       list.add(val);
                   }
               }
               tblreport.getItems().setAll(list);
               break;
    }

    }
    @FXML
    private BorderPane borderreport;
    ObservableList<BorderPane> borderlist=FXCollections.observableArrayList();
    ObservableList<ReportGenerate> controllerlist=FXCollections.observableArrayList();
    BorderPane bordermain;
    report sel;
    int count;
    @FXML
    void onselection(Event event) {
        sel= tblreport.getSelectionModel().getSelectedItem();
        if(sel!=null)
        {
            try {
                ps = con.prepareStatement(" select count(*) from report where billid=? and resultset is NULL");
                ps.setInt(1,sel.getBillid());
                rs = ps.executeQuery();
                rs.next();
               count= rs.getInt(1);
              if(rs.getInt(1)>=1)
              {
                  JFXButton printBill=new JFXButton("Print Receipt");
                  JFXButton reportGenerate=new JFXButton("Generate report");
                  new AlertBox(printBill,reportGenerate,new ActionEvent(event.getSource(),event.getTarget()));
                  printBill.setOnAction(e->{
                                    printing_Bill();
                          });
                  reportGenerate.setOnAction(e->{
                      try {

                          ps=con.prepareStatement("Select * from report where billid=?  ");
                          ps.setInt(1,sel.getBillid());
                          ResultSet rs1=ps.executeQuery();
                          JFXButton nextbutton,savebutton;
                          while (rs1.next())
                          {
                              loader= new FXMLLoader(getClass().getResource("/ui/pendingReport/reportGenerate.fxml"));
                              borderPane=(BorderPane) loader.load();
                              controller=loader.getController();
                              borderlist.add(borderPane);
                              controllerlist.add(controller);
                              controller.setTest_Patient(rs1.getString("testname"),sel.getGender(),sel.getAge());
                              if(rs.getRow()==count)
                              {
                                   nextbutton=new JFXButton("Save");
                                  controller.getVbox1().getChildren().add(nextbutton);
                                  nextbutton.setOnAction(event1->{
                                      controller.getBorderPane().toFront();
                                  });


                              }
                              else {
                                   savebutton=new JFXButton("Next");
                                  controller.getVbox1().getChildren().add(savebutton);
                                  savebutton.setOnAction(event1 -> {
                                      controller.getBorderPane().toFront();

                                  });

                              }
                          }

                          borderreport.setCenter((BorderPane)borderlist.get(0));
                          borderreport.toFront();
                          inc++;
                          listing();
                      } catch (SQLException ex) {
                          ex.printStackTrace();
                      }
                     catch (IOException ex) {
                          ex.printStackTrace();
                      }
                  });
              }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    int inc=0;
    public void listing()
    {
        if(inc==controllerlist.size())
        {
            JFXButton print=new JFXButton("Print");
            print.setVisible(false);
            JFXButton btn= ((JFXButton)controllerlist.get(inc-1).getVbox1().getChildren().get(0));
            btn.setOnAction(e->{
                print.setVisible(true);
                btn.setDisable(true);

            });
            controllerlist.get(inc-1).getVbox1().getChildren().add(print);
            controllerlist.get(inc-1).getVbox1().setSpacing(20);
            print.setOnAction(e->
            {
                print.setVisible(true);
            });
            inc++;
        }
        else
        {
            controllerlist.get(inc-1).getBorderPane().setCenter(borderlist.get(inc));
            inc++;
            listing();
        }
    }
    @FXML
    private BorderPane borderoverall;
//     void btnaction()
//    {
//        inc=0;
//        borderreport.setCenter((BorderPane)borderlist.get(inc));
//        borderreport.toFront();
//        while(inc<borderlist.size())
//        {
//            if(inc+1==borderlist.size())
//            {
//                ((JFXButton)  ((ReportGenerate)controllerlist.get(inc)).getVbox1().getChildren().get(0)).setOnAction(event ->
//                {
////                    ((ReportGenerate)controllerlist.get(inc)).savebtn(borderoverall);
//                    savebtn(borderoverall);
//
//
//                });
//
//               inc++;
//            }
//            else
//            {
//                ((JFXButton)  ((ReportGenerate)controllerlist.get(inc)).getVbox1().getChildren().get(0)).setOnAction(event ->
//                {
//                    ((ReportGenerate)controllerlist.get(inc)).nextbtn((BorderPane) borderlist.get(inc+1));
//
//                });
//                inc++;
//            }
//        }
//
//    }
    ReportGenerate controller;
    BorderPane borderPane;
    FXMLLoader loader;
    ArrayList<HashMap<String,Object>> hashlist=new ArrayList<HashMap<String,Object>>();
    float age;
    String sex;
    void printing_Bill()
    {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport("D:\\HealthCheckup\\src\\ui\\jrfxml\\MedicalReceipt.jrxml");
            HashMap<String,Object> hashmap;
            hashlist.clear();
            ps=con.prepareStatement(" select * from report r1 inner join test t1 on t1.testname=r1.testname and billid=?");
            ps.setInt(1,billid);
            rs=ps.executeQuery();
            int i=0;
            while (rs.next())
            {
                hashmap=new HashMap<>();
                hashmap.put("testname",rs.getString("testname"));
                hashmap.put("price",rs.getDouble("price"));
                hashmap.put("serialno",++i);
                ps=con.prepareStatement("select * from billing b1 inner join patient p1 on p1.patientid=b1.patientid and b1.billid=?");
                ps.setInt(1,sel.getBillid());
                ResultSet rs3= ps.executeQuery();
                while (rs3.next()) {
                    age= rs3.getFloat("age");
                    sex=rs3.getString("sex");
                    hashmap.put("patientname",rs3.getString("name") );
                    hashmap.put("age", age);
                    hashmap.put("sex", sex);
                    if (rs3.getInt("docid") != NULL) {
                        docid = rs3.getInt("docid");
                    } else
                        docid = -1;
                    if(docid!=-1) {
                        ps = con.prepareStatement("select * from doctor where docid=?");
                        ps.setInt(1, docid);
                        ResultSet rs2 = ps.executeQuery();
                        rs2.next();
                        hashmap.put("doctorname",rs2.getString("docname"));
                    }
                    else
                    {
                        hashmap.put("doctorname"," ");
                    }
                    hashmap.put("totalamt",rs3.getDouble("totalcost"));
                    hashmap.put("paidamt",rs3.getDouble("paid"));
                    hashmap.put("discountamt",rs3.getDouble("discount"));
                    hashmap.put("pendingamt", String.valueOf( rs3.getDouble("unpaid")));
                }
                hashlist.add(hashmap);
            }
            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(hashlist);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, null, beanColDataSource);
            JasperViewer.viewReport(print,false);
            String path=System.getProperty("user.dir");

        } catch (JRException e) {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    Connection con=DbHandler.getConnection();
    PreparedStatement ps;
    int billid,docid,patientid;
    ObservableList<report> reportlist= FXCollections.observableArrayList();
    ObservableList<report> list=FXCollections.observableArrayList();
    ResultSet rs;
    private void initTable(Date prev,Date next) {
        if (prev.toString() != "" && next.toString() != "") {
            try {

                ps = con.prepareStatement(" select * from sample s1 inner join billing b1 on b1.billid=s1.billid where s1.receivedd between ? and ?;");
                ps.setDate(1,prev);
                ps.setDate(2,next);
                rs = ps.executeQuery();
                while (rs.next()) {
                    billid = rs.getInt("billid");
                    if (rs.getInt("docid") != NULL) {
                        docid = rs.getInt("docid");
                    } else
                        docid = -1;
                    patientid = rs.getInt("patientid");
                    ps = con.prepareStatement("select * from patient where patientid=? ");
                    ps.setInt(1, patientid);
                    ResultSet rs1 = ps.executeQuery();
                    while (rs1.next()) {
                        if (docid != -1) {

                            ps = con.prepareStatement("select * from doctor where docid=?");
                            ps.setInt(1,docid);
                            ResultSet rs2 = ps.executeQuery();
                            rs1.next();
                            reportlist.add(new report(billid, rs1.getString("name"), rs1.getString("sex"), rs1.getString("mob"), rs2.getString("docname"), rs1.getFloat("age")));

                        } else {
                            reportlist.add(new report(billid, rs1.getString("name"), rs1.getString("sex"), rs1.getString("mob"), "", rs1.getFloat("age")));

                        }
                    }
                }
                tblreport.getItems().setAll(reportlist);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else
        {

            new AlertBox("Error","Enter the Dates for the search.");
        }
    }
    Date yesterday()
    {
        Calendar cal=Calendar.getInstance();
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        cal.add(Calendar.DATE,-1);
        System.out.println(dateFormat.format(cal.getTime()));
        return Date.valueOf(dateFormat.format(cal.getTime()));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbsearchby.getItems().setAll("Bill Number","Patient Name","Mobile Number","Doctor");
        colbillnum.setCellValueFactory(new PropertyValueFactory<>("billid"));
        colage.setCellValueFactory(new PropertyValueFactory<>("age"));
        coldoctor.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        colpatientname.setCellValueFactory(new PropertyValueFactory<>("patientname"));
        colgender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colmobno.setCellValueFactory(new PropertyValueFactory<>("mobileno"));
        txtdatefrom.setValue(yesterday().toLocalDate());
        txtdateto.setValue(LocalDate.parse(date()));
        initTable(yesterday(),Date.valueOf( date()));
    }
    public class report
    {
       private int billid;
       private String patientname,gender,mobileno,doctor;
       private float age;
        public report( int billid,String patientname,String gender,String mobileno,String doctor,float age)
        {
            this.billid=billid;
            this.patientname=patientname;
            this.gender=gender;
            this.mobileno=mobileno;
            this.doctor=doctor;
            this.age=age;
        }

        public int getBillid() {
            return billid;
        }

        public void setBillid(int billid) {
            this.billid = billid;
        }

        public String getPatientname() {
            return patientname;
        }

        public void setPatientname(String patientname) {
            this.patientname = patientname;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getMobileno() {
            return mobileno;
        }

        public void setMobileno(String mobileno) {
            this.mobileno = mobileno;
        }

        public String getDoctor() {
            return doctor;
        }

        public void setDoctor(String doctor) {
            this.doctor = doctor;
        }

        public float getAge() {
            return age;
        }

        public void setAge(float age) {
            this.age = age;
        }
    }
}
