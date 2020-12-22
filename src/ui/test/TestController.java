package ui.test;

import com.jfoenix.controls.JFXTextField;
import db.DbHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import javax.print.Doc;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;
import java.util.ResourceBundle;

public class TestController implements Initializable
{

    ObservableList<Doctor> dlist = FXCollections.observableArrayList();

    @FXML
    private TableView<Doctor> tbldoctor;

    @FXML
    private TableColumn<Doctor, Integer> colid;

    @FXML
    private TableColumn<Doctor, String> colname;

    @FXML
    private TableColumn<Doctor, String> cols;

    @FXML
    private TableColumn<Doctor, Integer> colage;

    @FXML
    private JFXTextField tid;

    @FXML
    private JFXTextField tname;

    @FXML
    private JFXTextField tspec;

    @FXML
    private JFXTextField tage;
    @FXML
    void loadtextdata(MouseEvent event) {
        Doctor d1 = tbldoctor.getSelectionModel().getSelectedItem();
        if(d1!=null)
        {
            tid.setText(d1.getId()+"");
            tname.setText(d1.getName());
            tspec.setText(d1.getSpeciality());
            tage.setText(d1.getAge()+"");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initColumn();
    }

    private void initColumn() {
        colid.setCellValueFactory(new PropertyValueFactory("id"));
        colage.setCellValueFactory(new PropertyValueFactory("age"));
        cols.setCellValueFactory(new PropertyValueFactory("speciality"));
        colname.setCellValueFactory(new PropertyValueFactory("name"));

        Connection con = DbHandler.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("select * from doctor");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Doctor  d = new Doctor(rs.getString("name"),rs.getString("speciality"),rs.getInt("id"),rs.getInt("age"));
                dlist.add(d);
            }

            tbldoctor.getItems().setAll(dlist);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public  class Doctor{
        private String name,speciality;
        private int id,age;

        public Doctor(String name, String speciality, int id, int age) {
            this.name = name;
            this.speciality = speciality;
            this.id = id;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSpeciality() {
            return speciality;
        }

        public void setSpeciality(String speciality) {
            this.speciality = speciality;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
