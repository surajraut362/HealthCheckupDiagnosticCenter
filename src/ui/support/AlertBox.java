package ui.support;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.awt.event.MouseEvent;

import static ui.support.GetStage.getStage;


public class AlertBox {
    public AlertBox(String title, String Message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(500);
        window.setHeight(250);
        Label label=new Label();
        label.setText(Message);
        JFXButton close=new JFXButton("close");
      close.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
              window.close();
          }
      });
        Region r=new Region();
        VBox pane=new VBox();
        pane.getChildren().setAll(label,r,close);
        pane.setAlignment(Pos.CENTER);
        window.setScene(new Scene(pane));
        window.showAndWait();

    }
    public AlertBox( String Message,ActionEvent event)
    {
        JFXDialogLayout d=new JFXDialogLayout();
        JFXDialog jfxDialog = new JFXDialog((StackPane) getStage(event).getScene().getRoot(),d,JFXDialog.DialogTransition.TOP);
        Label l=new Label(Message);
        JFXButton b=new JFXButton("OK");
        b.setLayoutX(150.0);
        d.setBorder(Border.EMPTY);
        d.prefHeight(100);
        d.prefWidth(300);
        d.setBody(b);
        d.setHeading(l);
        b.setOnAction(event1 -> {
            jfxDialog.close();
        });
        jfxDialog.show();
    }
    public  AlertBox(JFXButton b1, JFXButton b2, ActionEvent event)
    {
        JFXDialogLayout d=new JFXDialogLayout();
        JFXDialog jfxDialog = new JFXDialog((StackPane) getStage(event).getScene().getRoot(),d,JFXDialog.DialogTransition.TOP);
        HBox v=new HBox(20,b1,b2);
        b1.setLayoutX(150.0);
        b2.setLayoutX(200);
        d.setBorder(Border.EMPTY);
        d.prefHeight(100);
        d.prefWidth(300);
        d.setBody(v);
        jfxDialog.show();
        jfxDialog.setOnMouseClicked(e->{
            jfxDialog.close();
        } );
    }
}
