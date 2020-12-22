package ui.Admin.Test;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import db.DbHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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

public class dept implements Initializable {

    @FXML
    private AnchorPane anchorcenter;

    @FXML
    private JFXTextField txtdepartment;

    @FXML
    private JFXButton btncancel;

    @FXML
    private JFXButton btnadddept;
    PreparedStatement ps=null;
    ResultSet rs=  null;
    Connection con= DbHandler.getConnection();
    @FXML
    private JFXButton btnupdatedept;

    @FXML
    private JFXButton btnremovedept;

    @FXML
    private JFXTextField txtsearch;

    @FXML
    private TableView<department> tbldept;

    @FXML
    private TableColumn<?, ?> coldept;

    @FXML
    void adddept(ActionEvent event) {
        try {
            if(!txtdepartment.getText().isEmpty()) {
                ps = con.prepareStatement("select count(*) from department where deptname=?;");
                ps.setString(1, txtdepartment.getText().toUpperCase());
                rs = ps.executeQuery();
                rs.next();
                if (rs.getInt(1) != 1) {
                    ps = con.prepareStatement("insert into department(deptname) values(?);");
                    ps.setString(1, txtdepartment.getText().toUpperCase());
                    ps.execute();
                    cancel(event);

                } else {
                    new AlertBox("Department already exists", event);
                }
            }
            else
            {
                new AlertBox("Please fill the required field!",event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        tbldept.getItems().clear();
        txtdepartment.clear();
        txtsearch.clear();
        tbldept.getItems().clear();
        btnupdatedept.setVisible(false);
        btnremovedept.setVisible(false);
        btncancel.setVisible(false);
        btnadddept.setVisible(true);
    }
    department d;
    @FXML
    void loaddeptdata(MouseEvent event) {
        d=tbldept.getSelectionModel().getSelectedItem();
        if(d!=null) {
            btnadddept.setVisible(false);
            btnupdatedept.setVisible(true);
            btnremovedept.setVisible(true);
            btncancel.setVisible(true);
            txtdepartment.setText(d.getDeptname());
        }
    }

    @FXML
    void print(ActionEvent event) {

    }
    @FXML
    void removedept(ActionEvent event) {
        if(txtdepartment.getText().toUpperCase().equals(d.getDeptname().toUpperCase())) {
            try {

                ps=con.prepareStatement("select count(*) from department d1 inner join test t1 on t1.deptcode=d1.deptcode and deptname=?;");
                ps.setString(1,d.getDeptname());
                rs=ps.executeQuery();
                rs.next();
                if(rs.getInt(1)<1) {
                    ps = con.prepareStatement("delete from department where deptname=?;");
                    ps.setString(1, txtdepartment.getText().toUpperCase());
                    ps.execute();
                    cancel(event);

                }
                else {
                    new AlertBox("Tests already exist with department first delete test ",event);
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
        deptlist.clear();
        tbldept.getItems().clear();
        if(txtsearch.getText()!=null)
        {
            try {
                ps=con.prepareStatement("select * from department where deptname=? ");
                ps.setString(1,txtsearch.getText().toUpperCase());
                rs=ps.executeQuery();
                if(rs!=null)
                {
                    while (rs.next())
                    {
                        deptlist.add(new department(rs.getString("deptname"),rs.getInt("deptcode")));
                    }
                    tbldept.getItems().setAll(deptlist);
                }
                else
                {
                    new AlertBox("Department does not exists",event);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            new AlertBox("Please enter department name to search",event);
        }

    }
    ObservableList<department> deptlist= FXCollections.observableArrayList();
    @FXML
    void showall(ActionEvent event) {
        try {
            tbldept.getItems().clear();
            deptlist.clear();
            ps=con.prepareStatement("Select * from department;");
            rs=ps.executeQuery();
            while (rs.next())
            {
             deptlist.add(new department(rs.getString("deptname"),rs.getInt("deptcode")));
            }
            tbldept.getItems().setAll(deptlist);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void updatedept(ActionEvent event) {
        if(!(txtdepartment.getText().toUpperCase().equals(d.getDeptname().toUpperCase()))) {
            try {
                ps = con.prepareStatement("update department set deptname=? where deptname=?;");
                ps.setString(1,txtdepartment.getText().toUpperCase());
                ps.setString(2,d.getDeptname());
                ps.execute();
                cancel(event);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else
        {
            new AlertBox("! No changes reuired.",event);
        }
    }
    int admin;
   public void loadpane(int admin)
    {
        this.admin=admin;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        coldept.setCellValueFactory(new PropertyValueFactory<>("deptname"));
        btnupdatedept.setVisible(false);
        btnremovedept.setVisible(false);
        btncancel.setVisible(false);
    }

    public class department
    {
        String deptname;
        int deptid;
        public department( String deptname,int deptid)
        {
            this.deptid=deptid;
           this.deptname=deptname;
        }

        public void setDeptname(String deptname) {
            this.deptname = deptname;
        }

        public void setDeptid(int deptid) {
            this.deptid = deptid;
        }

        public String getDeptname() {
            return deptname;
        }

        public int getDeptid() {
            return deptid;
        }
    }
}
