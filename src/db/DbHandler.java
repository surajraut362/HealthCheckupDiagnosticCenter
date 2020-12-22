
package db;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ui.security.encryptpassword.encryptpassword;

/**
 *
 * @author Amit
 */
public class DbHandler {
      private static Connection con = null;

      private DbHandler()
      {

      }
      public static String date()
      {
          DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
          java.util.Date d=new Date();
          String s =dateFormat.format(d);
          return s;
      }
      public static Connection getConnection()
      {
        if(con==null)
        {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection ("jdbc:mysql://localhost:3306", "root", "root");
                excAction("create database if not exists healthcenter");
                excAction("use healthcenter");

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        return con;
      }

      public static boolean excAction(String sql)
      {
          try {
              Statement st = getConnection().createStatement();
              boolean flag = st.execute(sql);
              return flag;
          } catch (SQLException ex) {
              Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
              return false;
          }
      }
       public static ResultSet excQuery(String sql)
      {
          try {
              Statement st = getConnection().createStatement();
              ResultSet rs = st.executeQuery(sql);
              return rs;
          } catch (SQLException ex) {
              Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
              return null;
          }
      }


    public static void createAdminTable()
    {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = \"admin\" and table_schema = \"healthcenter\";");

        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                excAction("create table admin(adminid int(100) primary key AUTO_INCREMENT, username varchar(300) unique key not null,password varchar(100) not null,name varchar(50) not null)");
                excAction("insert into admin values(1,'root','"+encryptpassword("root")+"','HealthCenter');");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void createAssistantTable()
    {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = \"assistant\" and table_schema = \"healthcenter\";");

        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                String s=date();
                excAction("create table assistant(assistantid int(100) primary key AUTO_INCREMENT, username varchar(300) unique key not null,password varchar(100) not null,name varchar(30) not null,dob date not null,gender varchar(20) not null,email varchar(80) unique key not null,mob varchar(15) unique not null,joindate date not null, adminid int(100) not null);");
                excAction("insert into assistant values(1,'root','"+ encryptpassword("root")+"','HealthCenter','1999-02-06','Male','healthcenter@gmail.com','9545168438','"+s+"',1);");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createPatientTable()
    {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = \"patient\" and table_schema = \"healthcenter\";");

        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                excAction  ("create table patient(patientid int(100) primary key AUTO_INCREMENT,name varchar(30) not null," +
                        "age float(200,5) not null,sex varchar(10) not null,address varchar(150),state varchar(50)," +
                        "email varchar(80),mob varchar(15) not null,detail varchar(200),dateofreg date not null);" );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void createDoctorTable()
    {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = \"doctor\" and table_schema = \"healthcenter\";");

        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                excAction("create table doctor(docid int(100) primary key AUTO_INCREMENT,docname varchar(100) unique key,comm float(10,5) not null);");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void createDepartmentTable()
    {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = \"department\" and table_schema = \"healthcenter\";");

        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                excAction("create table department(deptcode int(100) primary key AUTO_INCREMENT,deptname varchar(100) unique key not null);");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTestTable()
    {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = \"test\" and table_schema = \"healthcenter\";");

        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                excAction("create table test(testcode int(100) primary key AUTO_INCREMENT,testname varchar(100) unique key not null,cost float(30,5) not null,price float(30,5) not null, deptcode int(100) not null );");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createParameterTable()
    {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = \"parameter\" and table_schema = \"healthcenter\";");

        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                excAction("create table parameter(parametercode int(100) primary key AUTO_INCREMENT,parametername varchar(100) unique key not null,dateofadd date not null,testcode int(100) not null );");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void createNormalValueTable()
    {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = \"normalvalue\" and table_schema = \"healthcenter\";");

        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                excAction("create table normalvalue(normalcode int(100) primary key AUTO_INCREMENT,sex varchar(50) not null,fromage float(150,2) not null,toage float(150,2) not null ,rangefrom float(10,5),rangeto float(10,5),unit varchar(20),parametercode int(100) not null);");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void createBillingTable()
    {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = \"billing\" and table_schema = \"healthcenter\";");

        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                excAction("create table billing(billid int(100) primary key AUTO_INCREMENT, totalcost float(30,5) not null,paid float(30,5) not null,unpaid float(30,5) not null,discount float(30,5) not null,pmode varchar(50),invoiced date not null,docid int(100),patientid int(100) not null,assistantid int(100) not null);");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void createSampleTable()
    {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = \"sample\" and table_schema = \"healthcenter\";");

        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                excAction("create table sample(receivedd date,receivedt time,billid int(100) not null,reportdate date,reporttime time)");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void createReportTable()
    {
        ResultSet rs = excQuery(" select count(*) from information_schema.tables where information_schema.tables.table_name = \"report\" and table_schema = \"healthcenter\";");

        try {
            rs.next();
            if ((rs.getInt(1)) == 0)
            {
                excAction("create table report(reportid int(100) primary key AUTO_INCREMENT,billid int(100) not null,testname varchar(100) not null,resultset varchar(150));");
                //                foreign key(testname) references test(testname)

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
