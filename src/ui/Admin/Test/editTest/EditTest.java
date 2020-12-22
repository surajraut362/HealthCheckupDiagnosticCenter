package ui.Admin.Test.editTest;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class EditTest {

    @FXML
    private AnchorPane anchorcenter;

    @FXML
    private FontAwesomeIconView glyidverify;

    @FXML
    private TableView<?> tbltest;

    @FXML
    private TableColumn<?, ?> coldept;

    @FXML
    private TableColumn<?, ?> colprof;

    @FXML
    private TableColumn<?, ?> coltesttype;

    @FXML
    private TableColumn<?, ?> coltestname;

    @FXML
    void print(ActionEvent event) {

    }
    @FXML
    void openTest(ActionEvent event) {

        Scene scene = ((Node) event.getSource()).getScene();
        AnchorPane anchorPane = (AnchorPane) scene.lookup("#panetest");

        anchorPane.toFront();
        System.out.println("in open test");
    }

}
