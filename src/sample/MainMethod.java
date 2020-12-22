package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.util.ArrayList;
import java.util.HashMap;

public class MainMethod {
    public static  void main(String args[])
    {
        ArrayList<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();

        JasperReport jasperReport = null;
        try {
            jasperReport = JasperCompileManager.compileReport("D:\\HealthCheckup\\src\\ui\\jrfxml\\MedicalReceipt.jrxml");
            HashMap<String,Object> haspmap = new HashMap<>();
            haspmap.put("testname","CBC");
            list.add(haspmap);
            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, null, beanColDataSource);
            JasperViewer.viewReport(print,false);
            String path=System.getProperty("user.dir");
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
