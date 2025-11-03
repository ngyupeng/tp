package seedu.address.ui.detailedpanel;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import seedu.address.model.event.PersonReference;
import seedu.address.ui.UiPart;

/**
 * An UI component that displays information of a {@code PersonReference}.
 */
public class PersonReferenceCard extends UiPart<VBox> {
    private static final String FXML = "detailedpanel/PersonReferenceCard.fxml";

    private static final Image phoneImage = new Image("/images/telephone.png");
    private static final Image userImage = new Image("/images/contact.png");
    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final PersonReference personReference;

    @FXML
    private Label id;
    @FXML
    private Label name;
    private ImageView userIcon = new ImageView(userImage);
    @FXML
    private Label phone;
    private ImageView phoneIcon = new ImageView(phoneImage);

    /**
     * Creates a {@code EventCode} with the given {@code Event} and index to display.
     */
    public PersonReferenceCard(PersonReference personReference, int displayedIndex) {
        super(FXML);
        this.personReference = personReference;
        id.setText(displayedIndex + ". ");
        userIcon.setFitHeight(20.0);
        userIcon.setFitWidth(20.0);
        phoneIcon.setFitHeight(20.0);
        phoneIcon.setFitWidth(20.0);
        name.setGraphic(userIcon);
        name.setText(personReference.getName().fullName);
        phone.setGraphic(phoneIcon);
        phone.setText(personReference.getPhone().value);
    }
}
