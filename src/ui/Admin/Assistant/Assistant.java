package ui.Admin.Assistant;

import com.jfoenix.controls.*;
import db.DbHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.security.SecureRandom;
import java.sql.Date;
import javafx.scene.input.MouseEvent;
import ui.support.AlertBox;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;

import static ui.security.encryptpassword.encryptpassword;

public class Assistant implements Initializable {

    @FXML
    private JFXButton btnupdate;

    @FXML
    private JFXButton btnaddassistant;

    @FXML
    private JFXTextArea txtaddress;

    @FXML
    private JFXDatePicker txtdob;

    @FXML
    private JFXComboBox<String> cbgender;

    @FXML
    private JFXTextField txtassistantname;

    @FXML
    private JFXTextField txtemail;

    @FXML
    private JFXTextField txtmobno;

    @FXML
    private JFXButton btnclear;

    @FXML
    private JFXButton btndelete;

    @FXML
    private ImageView imageprofile;

    @FXML
    private TableView<ass> tblassistant;

    @FXML
    private TableColumn<?, ?> colassistantname;

    @FXML
    private TableColumn<?, ?> colmobno;

    @FXML
    private TableColumn<?, ?> colemail;

    @FXML
    private TableColumn<?, ?> coldob;

    @FXML
    private TableColumn<?, ?> colgender;

    @FXML
    private TableColumn<?, ?> coladdress;

    @FXML
    private JFXTextField txtsearch;

    @FXML
    private JFXComboBox<?> cbcriteria;

    @FXML
    void Clear(ActionEvent event) {

    }
    static char[] SYMBOLS = (new String("^$*.[]{}()?-\"!@#%&/\\,><':;|_~`")).toCharArray();
    static char[] LOWERCASE = (new String("abcdefghijklmnopqrstuvwxyz")).toCharArray();
    static char[] UPPERCASE = (new String("ABCDEFGHIJKLMNOPQRSTUVWXYZ")).toCharArray();
    static char[] NUMBERS = (new String("0123456789")).toCharArray();
    static char[] ALL_CHARS = (new String("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789^$*.[]{}()?-\"!@#%&/\\,><':;|_~`")).toCharArray();
    static Random rand = new SecureRandom();

        public static String getPassword(int length) {

            assert length >= 4;
            char[] password = new char[length];

            //get the requirements out of the way
            password[0] = LOWERCASE[rand.nextInt(LOWERCASE.length)];
            password[1] = UPPERCASE[rand.nextInt(UPPERCASE.length)];
            password[2] = NUMBERS[rand.nextInt(NUMBERS.length)];
            password[3] = SYMBOLS[rand.nextInt(SYMBOLS.length)];

            //populate rest of the password with random chars
            for (int i = 4; i < length; i++) {
                password[i] = ALL_CHARS[rand.nextInt(ALL_CHARS.length)];
            }

            //shuffle it up
            for (int i = 0; i < password.length; i++) {
                int randomPosition = rand.nextInt(password.length);
                char temp = password[i];
                password[i] = password[randomPosition];
                password[randomPosition] = temp;
            }

            return new String(password);
        }

    @FXML
    void addassistant(ActionEvent event) {
            String username;
            if(txtassistantname.getText().isEmpty()||txtemail.getText().isEmpty()||txtmobno.getText().isEmpty()||txtdob.getValue().toString().isEmpty()||
            cbgender.getSelectionModel().isEmpty()||txtaddress.getText().isEmpty())
            {
                new AlertBox("Requires fields are empty!",event);
            }
            else {
                try {
                    ps=con.prepareStatement("SELECT count(*) from assistant where email=? or mob=?");
                    ps.setString(1,txtemail.getText());
                    ps.setString(2,txtmobno.getText());
                    rs=ps.executeQuery();
                    if(rs.getInt(1)>=1)
                    {
                        new AlertBox("Email or Mobile number are already used!",event);
                    }
                    else {
                        username = username();
                        System.out.println(username);
                        ps = con.prepareStatement("insert into assistant(username,password,name,dob,gender,email,mob,joindate,adddress,adminid) values (?,?,?,?,?,?,?,?,?,?); ");
                        ps.setString(1, txtemail.getText());
                        ps.setString(2, encryptpassword(assistantpass));
                        ps.setString(3, txtassistantname.getText());
                        ps.setDate(4, Date.valueOf(txtdob.getValue()));
                        ps.setString(5, cbgender.getValue());
                        ps.setString(6, txtemail.getText());
                        ps.setString(7, txtmobno.getText());
                        ps.setDate(8, Date.valueOf(DbHandler.date()));
                        ps.setString(9, txtaddress.getText());
                        ps.setInt(10, admin);
                        Clear(event);
                    }
                } catch (SQLException e) {
            e.printStackTrace();
        }
            }
    }

     String username()
    {
        String username="";
        String arr[]=txtassistantname.getText().split(" ");
        for (int i = 0; i <arr.length ; i++) {
            if(arr[i].length()>2)
            {
                System.out.println(arr[i]);
                System.out.println(arr[i].substring(0,2));
                username=username+arr[i].substring(0,2);
                System.out.println(username);
            }
            else
            {
                username=username+arr[i];
                System.out.println(username);

            }

        }
        username= checkuser(username,0);
        System.out.println(username);
        return  username;

    }
      String checkuser(String username, int count)
    {
        try {
            ps=con.prepareStatement("select count(*) from assistant where username=?;");
            ps.setString(1,username);
            rs=ps.executeQuery();
            rs.next();
            if(rs.getInt(1)>=1)
            {
                ++count;
                username=username+String.valueOf(count);
                System.out.println(username);
                username= checkuser(username,count);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  username;
    }
    @FXML
    void chooseProfilePhoto(ActionEvent event) {

    }
    ass a1;

    @FXML
    void loadassistantdata(MouseEvent event) {
        a1=tblassistant.getSelectionModel().getSelectedItem();
        if(a1!=null)
        {
            btnaddassistant.setVisible(false);
            btndelete.setVisible(true);
            btnupdate.setVisible(true);
            btnclear.setVisible(true);
            txtassistantname.setText(a1.getAssname());
            txtemail.setText(a1.getAssemail());
            txtmobno.setText(a1.getAssmobno());
            txtdob.getEditor().setText(String.valueOf(a1.getAssdob()));
            txtaddress.setText(a1.getAssaddress());
            cbgender.setValue(a1.getAssgender());
        }
    }

    @FXML
    void ondelete(ActionEvent event) {
        if(check()==true)
        {
            try {
                ps=con.prepareStatement("delete from assistant where email=?;");
                ps.setString(1,a1.getAssemail());
                ps.execute();
                Clear(event);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        else
        {
            new AlertBox("Please reselect the field Some error occurred!",event);
        }
    }
    Boolean check()
    {
        if(txtassistantname.getText().toUpperCase().equals(a1.getAssname().toUpperCase())&&txtemail.getText().toUpperCase().equals(a1.getAssemail().toUpperCase())&&txtmobno.getText().toUpperCase().equals(a1.getAssmobno().toUpperCase())&&txtdob.equals(a1.getAssdob())&&cbgender.getValue().toUpperCase().equals(a1.getAssgender().toUpperCase())
                &&txtaddress.getText().toUpperCase().equals(a1.getAssaddress().toUpperCase()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    @FXML
    void search(ActionEvent event) {
        if(txtsearch.getText().isEmpty()||cbcriteria.getSelectionModel().isEmpty())
        {
            new AlertBox("Required fields is required for searching!",event);
        }
        else {
            int i;
            allassistant();
            switch (cbcriteria.getSelectionModel().getSelectedIndex()) {
                case 0:
                    i = 0;
                    tblassistant.getItems().clear();
                    while (i < assistantlist.size()) {
                        if (assistantlist.get(i).getAssname().toUpperCase().contains(txtsearch.getText().toUpperCase()))
                            tblassistant.getItems().add(assistantlist.get(i));
                        i++;
                    }
                    break;

                case 1:
                    i = 0;
                    tblassistant.getItems().clear();
                    while (i < assistantlist.size()) {
                        if (assistantlist.get(i).getAssmobno().toUpperCase().contains(txtsearch.getText().toUpperCase()))
                            tblassistant.getItems().add(assistantlist.get(i));
                        i++;
                    }
                    break;
                case 2:
                    i = 0;
                    tblassistant.getItems().clear();
                    while (i < assistantlist.size()) {
                        if (assistantlist.get(i).getAssemail().toUpperCase().contains(txtsearch.getText().toUpperCase()))
                            tblassistant.getItems().add(assistantlist.get(i));
                        i++;
                    }
                    break;
            }
        }
    }
    Connection con= DbHandler.getConnection();
    PreparedStatement ps=null;
    ResultSet rs;
    ObservableList<ass> assistantlist= FXCollections.observableArrayList();
    void allassistant()
    {
        assistantlist.clear();
        try {
            ps=con.prepareStatement("SELECT  * from assistant");
            rs=ps.executeQuery();
            while (rs.next())
            {
                assistantlist.add(new ass(rs.getString("name"),rs.getString("mob"),rs.getString("email"),rs.getString("gender"),rs.getString("address"),rs.getDate("dob")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void showall(ActionEvent event) {
    allassistant();
    tblassistant.getItems().setAll(assistantlist);
    }

    @FXML
    void updateassistant(ActionEvent event) {

    }
    int admin;
    public void loadpane(int admin)
    {
        this.admin=admin;
    }
    void initcolumns()
    {
        colassistantname.setCellValueFactory(new PropertyValueFactory<>("assname"));
        colmobno.setCellValueFactory(new PropertyValueFactory<>("assmobno"));
        coldob.setCellValueFactory(new PropertyValueFactory<>("assdob"));
        colemail.setCellValueFactory(new PropertyValueFactory<>("assemail"));
        colgender.setCellValueFactory(new PropertyValueFactory<>("assgender"));
        coladdress.setCellValueFactory(new PropertyValueFactory<>("assaddress"));
    }
    ObservableList genderlist= FXCollections.observableArrayList();
    ObservableList criterialist=FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        genderlist.setAll("Male","Female");
        criterialist.setAll("Name","Mobile Number","Email");
        cbcriteria.getItems().setAll(criterialist);
        cbcriteria.getSelectionModel().selectFirst();
        cbgender.getItems().setAll(genderlist);
        cbgender.getSelectionModel().selectFirst();
        btnclear.setVisible(false);
        btnupdate.setVisible(false);
        btndelete.setVisible(false);
        initcolumns();
    }
    String email;
    String assistantpass;
    void sendOtp(ActionEvent event) {

                        try {
                            final String username = "snraut820@gmail.com";
                            final String password ="20891814";

                            Properties prop = new Properties();
                            prop.put("mail.smtp.host","smtp.gmail.com");
                            prop.put("mail.smtp.port","587");
                            prop.put("mail.smtp.auth","true");
                            prop.put("mail.smtp.starttls.enable","true");

                            Session session = Session.getInstance(prop, new Authenticator() {
                                @Override
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(username,password);
                                }
                            });

                            ps = DbHandler.getConnection().prepareStatement("SELECT email from login where username = ?");
                            ps.setString(1,txtemail.getText());
                            rs = ps.executeQuery();
                            while (rs.next()){
                                email = rs.getString("email");
                            }


                            Message m = new MimeMessage(session);
                            m.setFrom(new InternetAddress("snraut820@gmail.com","HCD Center"));
                            m.setRecipient(Message.RecipientType.TO,new InternetAddress(email));
                            m.setSubject("Your Assistant Login Credentials on HCD Center:\n");
                            m.setText("Username: "+email+"\nPassword: "+assistantpass);
                            Transport.send(m);

                        }
                        catch (Exception e){

                            e.printStackTrace();
                        }
                    }


    public class ass
    {
        String assname,assmobno,assemail, assgender,assaddress;
        Date assdob;
        public ass(String assname, String assmobno, String assemail, String assgender, String assaddress, Date assdob)
        {
            this.assname=assname;
            this.assmobno=assmobno;
            this.assgender=assgender;
            this.assemail=assemail;
            this.assaddress=assaddress;
            this.assdob=assdob;
        }

        public String getAssname() {
            return assname;
        }

        public void setAssname(String assname) {
            this.assname = assname;
        }

        public String getAssmobno() {
            return assmobno;
        }

        public void setAssmobno(String assmobno) {
            this.assmobno = assmobno;
        }

        public String getAssemail() {
            return assemail;
        }

        public void setAssemail(String assemail) {
            this.assemail = assemail;
        }

        public String getAssgender() {
            return assgender;
        }

        public void setAssgender(String assgender) {
            this.assgender = assgender;
        }

        public String getAssaddress() {
            return assaddress;
        }

        public void setAssaddress(String assaddress) {
            this.assaddress = assaddress;
        }

        public Date getAssdob() {
            return assdob;
        }

        public void setAssdob(Date assdob) {
            this.assdob = assdob;
        }
    }
}








