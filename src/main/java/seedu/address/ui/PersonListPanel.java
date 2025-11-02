package seedu.address.ui;

import java.util.logging.Logger;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.ZoomIn;
import seedu.address.model.ZoomInType;
import seedu.address.model.event.Event;
import seedu.address.model.person.Person;


/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    @FXML
    private ListView<Person> personListView;
    @FXML
    private HBox eventHeader;
    @FXML
    private Label eventName;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     * Binds the selected person of the {@code ListView} with the selected person in {@code Model}
     */
    public PersonListPanel(ObservableList<Person> filteredList, ObjectProperty<Person> selectedPerson,
                           ObjectProperty<ZoomIn> zoomIn) {
        super(FXML);
        personListView.setItems(filteredList);
        personListView.setCellFactory(listView -> new PersonListViewCell());
        personListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        selectedPerson.addListener((obs, old, newPerson) -> {
            personListView.getSelectionModel().select(newPerson);
        });
        updateZoomInSelectedEvent(zoomIn);
        zoomIn.addListener((obs, old, newZoom) -> {
            updateZoomInSelectedEvent(zoomIn);
        });
        personListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, old, newPerson) -> {
                    selectedPerson.setValue(newPerson);
                    if (newPerson != null) {
                        personListView.requestFocus();
                    }
                });
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                // Updates the pseudo-class "pin" if pin status changes.
                this.pseudoClassStateChanged(PseudoClass.getPseudoClass("pin"), person.getPin().value);
                setGraphic(new PersonCard(person, getIndex() + 1).getRoot());

                personListView.setFixedCellSize(-1);
            }
        }
    }

    /**
     * Updates UI to hide/show the event header which indicates that the students belong to a particular event.
     */
    private void updateZoomInSelectedEvent(ObjectProperty<ZoomIn> zoomIn) {
        if (zoomIn.get().getType() == ZoomInType.EVENT) {
            eventHeader.setManaged(true);
            eventHeader.setVisible(true);
            Event targetEvent = zoomIn.get().getTargetEvent();
            eventName.setText("Showing Students Of: " + targetEvent.getName().toString()
                    + " (" + targetEvent.getDuration().toString() + ")");
        } else {
            eventHeader.setManaged(false);
            eventHeader.setVisible(false);
        }
    }
}
