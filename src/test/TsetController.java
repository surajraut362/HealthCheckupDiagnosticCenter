package test;

import com.jfoenix.controls.JFXComboBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class TsetController implements Initializable {

    ObservableList<String> list = FXCollections.observableArrayList();
    ObservableList<String> plist = FXCollections.observableArrayList();

    @FXML
    private JFXComboBox<String> cb;

    @FXML
    private Label lbl;


    @FXML
    void loadContext(ActionEvent event) {
        cb.show();
    }


    @FXML
    void loadContext2(KeyEvent event) {

    }


    /*

                if (cb.getSelectionModel().isEmpty()){

                    try{
                        String str = cb.getValue();
                        //String str=newValue;
                        if (!str.equals(null))
                        {
                            if (str.matches("^[A-Za-z0-9]+$"))
                            {
                                cb.getItems().clear();
                                for (String s :
                                        plist) {
                                    if (s.contains(str)){
                                        cb.getItems().add(s);
                                    }
                                }
                                cb.show();
                            }
                        }
                    }
                    catch (Exception e){

                    }
                }
                else
                {
                    try{
                        String str1 = cb.getValue();
                        //String str=newValue;
                        if (!str1.equals(null))
                        {
                            if (str1.matches("^[A-Za-z0-9]+$"))
                            {
                                cb.getItems().clear();
                                for (String s1 :
                                        plist) {
                                    if (s1.contains(str1)){
                                        cb.getItems().add(s1);
                                    }
                                }
                                cb.show();
                            }
                        }
                    }
                    catch (Exception e){

                    }

                }
            }

*/

    int c = 1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // list.addAll("Item1","Item2","Item3");

        plist.addAll("Item1", "Item2", "Item3", "Sample1", "Sample2", "Sample3");
        cb.getItems().setAll("");
        cb.valueProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                try {
                    if (newValue != null) {
                        try {
                            if (newValue.equals("")) {
                                newValue = oldValue;
                            }
                            //String str=newValue;
                            if (!newValue.equals(null)) {
                                if (newValue.matches("^[A-Za-z0-9]+$")) {
                                    System.out.println("showing Context Window : " + c++);
                                    cb.getItems().setAll("");
                                    for (String s :
                                            plist) {
                                        if (s.contains(newValue)) {
                                            if (cb.getItems().contains(""))
                                                cb.getItems().remove("");
                                            cb.getItems().add(s);
                                        }
                                        cb.show();
                                    }
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                } catch (IndexOutOfBoundsException e) {
                } catch (Exception e) {

                }
            }
        });


    }
}
