package seedu.address.ui.detailedpanel;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import seedu.address.ui.UiPart;

/**
 * Panel showing consolidated information of the all the persons in the unfiltered person list.
 */
public class ConsolidatePanel extends UiPart<VBox> {
    private static final String FXML = "detailedpanel/ConsolidatePanel.fxml";

    @FXML
    private TextArea name;
    /**
     * Constructs ConsolidatePanel and links the graphics.
     */
    public ConsolidatePanel() {
        super(FXML);
    }

    /**
     * Updates this panel with the new consolidated information.
     */
    public void update(String info) {
        name.setText(info);
    }
}
