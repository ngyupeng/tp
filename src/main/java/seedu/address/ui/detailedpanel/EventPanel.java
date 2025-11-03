package seedu.address.ui.detailedpanel;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import seedu.address.model.event.Event;
import seedu.address.model.event.PersonReference;
import seedu.address.ui.UiPart;

/**
 * Panel showing detailed information of the event selected.
 */
public class EventPanel extends UiPart<VBox> {
    private static final String FXML = "detailedpanel/EventPanel.fxml";

    private static final Image infoImage = new Image("/images/info_icon.png");
    private static final Image yearImage = new Image("/images/year.png");

    @FXML
    private Label name;
    @FXML
    private Label duration;
    @FXML
    private Label description;
    @FXML
    private ImageView yearIcon;
    @FXML
    private ImageView infoIcon;
    @FXML
    private VBox attendees;

    /**
     * Constructs EventPanel and links the graphics.
     */
    public EventPanel() {
        super(FXML);

        yearIcon.setImage(yearImage);
        infoIcon.setImage(infoImage);

        duration.setGraphic(yearIcon);
        description.setGraphic(infoIcon);
    }

    /**
     * Populates the panel with details of the selected event.
     */
    public void updateDetails(Event selected) {
        assert selected != null : "event cannot be null";

        name.setText(selected.getName().value);
        duration.setText(selected.getDuration().toString());
        description.setText(selected.getDescription().value);
        attendees.getChildren().clear();
        List<PersonReference> attendanceList = selected.getAttendance().asUnmodifiableList();
        for (int i = 0; i < attendanceList.size(); i++) {
            attendees.getChildren().add(new PersonReferenceCard(attendanceList.get(i), i + 1).getRoot());
        }
    }
}
