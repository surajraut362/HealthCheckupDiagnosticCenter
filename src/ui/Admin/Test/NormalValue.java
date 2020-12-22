package ui.Admin.Test;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import ui.support.AlertBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class NormalValue implements Initializable {

    @FXML
    private JFXComboBox<String> txtdept;
    int deptcode=-1,testcode=-1,parametercode=-1,normalcode=-1;
    @FXML
    private JFXComboBox<String> txttestname;

    @FXML
    private JFXComboBox<String> txtparameter;

    @FXML
    private JFXButton btnback;

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

//    @FXML
//    private JFXButton btnclearnormal;

    @FXML
    private JFXTextField txtrangefrom;

    @FXML
    private JFXTextField txtrangeto;

    @FXML
    private JFXButton btnaddnormal;

    @FXML
    private JFXButton btnupdatenormal;

    @FXML
    private JFXButton btnremovenormal;

    @FXML
    private JFXButton btncancel;

    @FXML
    private TableView<normal> tblnormal;

    @FXML
    private TableColumn<?, ?> colsex;

    @FXML
    private TableColumn<?, ?> colagefrom;

    @FXML
    private TableColumn<?, ?> colageto;

    @FXML
    private TableColumn<?, ?> colrangefrom;

    @FXML
    private TableColumn<?, ?> colrangeto;

    @FXML
    private TableColumn<?, ?> colunit;

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
                        if(Float.valueOf(txtyearfrom.getText())<=Float.valueOf(txtyearto.getText())) {
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
                            new AlertBox("New Normal Value is added to the Parameter", event);
                        }
                        else
                        {
                            new AlertBox("Year from cant be greater than year to! ",event);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        clearNormal(event);
        btncancel.setVisible(false);
        txtdept.setDisable(false);
        txttestname.setDisable(false);
        txtparameter.setDisable(false);
        btnaddnormal.setVisible(true);
        btnremovenormal.setVisible(false);
        btnupdatenormal.setVisible(false);
    }

    @FXML
    void clearNormal(ActionEvent event) {
        if(btncancel.isVisible()==false) {
            txtdept.getSelectionModel().clearSelection();
            txttestname.getItems().clear();
            txtparameter.getItems().clear();
            txtparameter.setValue("");
            txttestname.getSelectionModel().clearSelection();
        }
        txtsex.getSelectionModel().clearSelection();
        chknounit.setSelected(false);
        txtunit.clear();
        chkforallages.setSelected(false);
        txtyearfrom.clear();
        txtyearto.clear();
        txtrangefrom.clear();
        txtrangeto.clear();
    }
    normal nv=null;
    @FXML
    void loadnormaldata(MouseEvent event) {
         nv=  tblnormal.getSelectionModel().getSelectedItem();
        if(nv!=null)
        {
            txtdept.setDisable(true);
            txttestname.setDisable(true);
            txtparameter.setDisable(true);
            txtsex.setValue(nv.getSex());
            txtunit.setText(nv.getUnit());
            txtyearfrom.setText(String.valueOf(nv.getAgefrom()));
            txtyearto.setText(String.valueOf(nv.getAgeto()));
            txtrangefrom.setText(String.valueOf(nv.getRangefrom()));
            txtrangeto.setText(String.valueOf(nv.getRangeto()));
            normalcode=nv.getNormalid();
            btncancel.setVisible(true);
            btnaddnormal.setVisible(false);
            btnupdatenormal.setVisible(true);
            btnremovenormal.setVisible(true);
        }
    }

    @FXML
    void onback(ActionEvent event) {

    }

    @FXML
    void removenormal(ActionEvent event) {
        if(txtsex.getValue().equals(nv.getSex())&&txtunit.getText().equals(nv.getUnit())&&nv.getAgefrom()==Float.parseFloat(txtyearfrom.getText()) && nv.getAgeto()==Float.valueOf(txtyearto.getText())&&nv.getRangefrom()==Float.valueOf(txtrangefrom.getText())&&nv.getRangeto()==Float.valueOf(txtrangeto.getText()))
        {
            try {
                PreparedStatement ps=con.prepareStatement("delete from normalvalue where normalcode=?; ");
                ps.setInt(1,normalcode);
                ps.execute();
                new AlertBox("Normal Value is Removed",event);
                normal_fetch();
                clearNormal(event);
                cancel(event);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else
        {
            new AlertBox("Changes are made to the fields please reselect item.",event);
        }
    }

    @FXML
    void updatenormal(ActionEvent event) {
        if(txtsex.getValue().equals(nv.getSex())&&txtunit.getText().equals(nv.getUnit())&&nv.getAgefrom()==Float.parseFloat(txtyearfrom.getText()) && nv.getAgeto()==Float.valueOf(txtyearto.getText())&&nv.getRangefrom()==Float.valueOf(txtrangefrom.getText())&&nv.getRangeto()==Float.valueOf(txtrangeto.getText()))
        {
            new AlertBox("No updation required in normal values",event);
        }
        else
        {
            try {
                if(Float.valueOf(txtyearfrom.getText())<=Float.valueOf(txtyearto.getText())) {
                    ps = con.prepareStatement("update normalvalue set sex=?,fromage=?,toage=?,rangefrom=?,rangeto=?,unit=? where normalcode=? ");
                    ps.setString(1,txtsex.getSelectionModel().getSelectedItem());
                    ps.setFloat(2, Float.parseFloat(txtyearfrom.getText()));
                    ps.setFloat(3, Float.parseFloat(txtyearto.getText()));
                    ps.setFloat(4,Float.parseFloat(txtrangefrom.getText()));
                    ps.setFloat(5,Float.parseFloat(txtrangeto.getText()));
                    ps.setString(6,txtunit.getText());
                    ps.setInt(7,normalcode);
                    ps.execute();
                    cancel(event);
                    normal_fetch();
                }
                else
                {
                    new AlertBox("Year from cant be greater than year to!",event);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
    void normal_fetch()
    {
        try {
            tblnormal.getItems().clear();
            normallist.clear();
            ps = con.prepareStatement("Select * from normalvalue where parametercode=?;");
            ps.setInt(1, parametercode);
            rs = ps.executeQuery();
            while (rs.next()) {
                normallist.add(new normal(rs.getString("sex"), rs.getString("unit"), rs.getFloat("fromage"), rs.getFloat("toage"), rs.getFloat("rangefrom"), rs.getFloat("rangeto"), rs.getInt("normalcode")));
            }
            tblnormal.getItems().setAll(normallist);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    int admin;
    void  loadpane(int admin)
    {
        this.admin=admin;
    }
    ObservableList testlist=FXCollections.observableArrayList();
    ObservableList parameterlist=FXCollections.observableArrayList();
    ObservableList genderlist=FXCollections.observableArrayList();
    ObservableList normallist=FXCollections.observableArrayList();
    AtomicReference<Integer> flag1= new AtomicReference<>(1);
    AtomicReference<Integer> flag2= new AtomicReference<>(1);
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initdept();
        initColumn();
        btnupdatenormal.setVisible(false);
        btnremovenormal.setVisible(false);
        btncancel.setVisible(false);
        genderlist.setAll("Male","Female");
        txtsex.getItems().setAll(genderlist);
       txtdept.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)  ->
       {
           try {
               System.out.println("At DEPT");
               testlist.clear();
               tblnormal.getItems().clear();

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
               if(flag1.get() ==-1) {
                   txttestname.setValue(null);
                   flag1.set(1);
                   if(flag2.get() ==-1) {
              txtparameter.getSelectionModel().clearSelection();
              flag2.set(1);
                              }
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
       });

        txttestname.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->   {
            if(txttestname.getSelectionModel().getSelectedItem()!=null)
            {
                try {
                flag1.set(-1);
                System.out.println("At test");
                parameterlist.clear();
                normallist.clear();
                tblnormal.getItems().clear();
                  txtparameter.getSelectionModel().clearSelection();
                ps = con.prepareStatement("Select testcode from test where testname=?");
                ps.setString(1, txttestname.getSelectionModel().getSelectedItem());
                rs = ps.executeQuery();
                while (rs.next()) {
                    testcode = rs.getInt("testcode");
                }
                ps = con.prepareStatement("Select parametername from parameter where testcode=?;");
                ps.setInt(1, testcode);
                rs = ps.executeQuery();
                while (rs.next()) {
                    parameterlist.add(rs.getString("parametername"));
                }
                txtparameter.getItems().setAll(parameterlist);
                if(flag2.get()==-1)
                {
                    txtparameter.getSelectionModel().clearSelection();
                    flag2.set(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }}});
        txtparameter.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->   {
            if(txtparameter.getSelectionModel().getSelectedItem()!=null)
            {
            try {
                flag2.set(-1);
                System.out.println("At parameter");
                ps=con.prepareStatement("select parametercode from parameter where parametername=?;");
                ps.setString(1,txtparameter.getSelectionModel().getSelectedItem());
                rs=ps.executeQuery();
                while (rs.next())
                {
                    parametercode=rs.getInt("parametercode");
                }
                if( txtdept.getValue()!=null &&txttestname!=null&& txtparameter!=null) {
                 normal_fetch();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }}
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
    private void initColumn() {
        colsex.setCellValueFactory(new PropertyValueFactory("sex"));
        colagefrom.setCellValueFactory(new PropertyValueFactory("agefrom"));
        colageto.setCellValueFactory(new PropertyValueFactory("ageto"));
        colrangefrom.setCellValueFactory(new PropertyValueFactory("rangefrom"));
        colrangeto.setCellValueFactory(new PropertyValueFactory<>("rangeto"));
        colunit.setCellValueFactory(new PropertyValueFactory<>("unit"));
    }
    public class normal
    {
        String sex,unit;
        float agefrom,ageto,rangefrom,rangeto;
        int normalid;
        public normal(String sex, String unit, float agefrom, float ageto, float rangefrom, float rangeto,int normalid)
        {
            this.sex=sex;
            this.unit=unit;
            this.agefrom=agefrom;
            this.ageto=ageto;
            this.rangefrom=rangefrom;
            this.rangeto=rangeto;
            this.normalid=normalid;
        }

        public void setNormalid(int normalid) {
            this.normalid = normalid;
        }

        public int getNormalid() {
            return normalid;
        }

        public String getSex() {
            return sex;
        }

        public String getUnit() {
            return unit;
        }

        public float getAgefrom() {
            return agefrom;
        }

        public float getAgeto() {
            return ageto;
        }

        public float getRangefrom() {
            return rangefrom;
        }

        public float getRangeto() {
            return rangeto;
        }
        public void setSex(String sex) {
            this.sex = sex;
        }
        public void setUnit(String unit) {
            this.unit = unit;
        }
        public void setAgefrom(float agefrom) {
            this.agefrom = agefrom;
        }

        public void setAgeto(float ageto) {
            this.ageto = ageto;
        }

        public void setRangefrom(float rangefrom) {
            this.rangefrom = rangefrom;
        }

        public void setRangeto(float rangeto) {
            this.rangeto = rangeto;
        }
    }
}
