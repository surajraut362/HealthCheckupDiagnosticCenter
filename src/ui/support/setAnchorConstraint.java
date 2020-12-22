package ui.support;

import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class setAnchorConstraint {
    public  setAnchorConstraint(Parent borderPane) {

        Double v = 0.0;
        AnchorPane.setTopAnchor(borderPane,v);
        AnchorPane.setBottomAnchor(borderPane,v);
        AnchorPane.setLeftAnchor(borderPane,v);
        AnchorPane.setRightAnchor(borderPane,v);

    }
}
