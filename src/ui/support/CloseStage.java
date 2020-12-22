package ui.support;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class CloseStage {
    public  static void closeStage(ActionEvent event){
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
    public  static void closeStage(Node node){
        Stage stage = (Stage) (node).getScene().getWindow();
        stage.close();
    }
}
