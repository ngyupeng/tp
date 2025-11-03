package seedu.address.ui.detailedpanel;

import java.util.logging.Logger;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.event.Event;
import seedu.address.model.person.Person;
import seedu.address.ui.UiPart;

/**
 * Panel that switches between the active detailed panel being shown.
 */
public class DetailedPanel extends UiPart<StackPane> {
    private static final String FXML = "detailedpanel/DetailedPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(DetailedPanel.class);

    // Internal copy
    private final HelpPanel helpPanel = new HelpPanel();
    private final PersonPanel personPanel;
    private final ObservableObjectValue<Person> selectedPerson;
    private final EventPanel eventPanel;
    private final ObservableObjectValue<Event> selectedEvent;
    private final ConsolidatePanel consolidatePanel;

    @FXML
    private VBox helpPanelPlaceholder;
    @FXML
    private VBox personPanelPlaceholder;
    @FXML
    private VBox eventPanelPlaceholder;
    @FXML
    private VBox consolidatePanelPlaceholder;

    private VBox activePanel = helpPanelPlaceholder;

    /**
     * Constructs a container panel that initially shows the help panel.
     */
    public DetailedPanel(ObjectProperty<Person> selectedPerson,
            ObjectProperty<Event> selectedEvent) {
        super(FXML);
        this.selectedPerson = selectedPerson;
        this.selectedEvent = selectedEvent;
        personPanel = new PersonPanel();
        eventPanel = new EventPanel();
        consolidatePanel = new ConsolidatePanel();

        helpPanelPlaceholder.getChildren().add(helpPanel.getRoot());
        personPanelPlaceholder.getChildren().add(personPanel.getRoot());
        eventPanelPlaceholder.getChildren().add(eventPanel.getRoot());
        consolidatePanelPlaceholder.getChildren().add(consolidatePanel.getRoot());

        // Workaround to get panel height to fill up the whole container
        helpPanel.getRoot().prefHeightProperty().bind(personPanelPlaceholder.heightProperty());
        helpPanelPlaceholder.setVisible(true);
        personPanel.getRoot().prefHeightProperty().bind(personPanelPlaceholder.heightProperty());
        personPanelPlaceholder.setVisible(false);
        eventPanel.getRoot().prefHeightProperty().bind(eventPanelPlaceholder.heightProperty());
        eventPanelPlaceholder.setVisible(false);
        consolidatePanel.getRoot().prefHeightProperty().bind(consolidatePanelPlaceholder.heightProperty());
        consolidatePanelPlaceholder.setVisible(false);

        selectedPerson.addListener((obs, old, newPerson) -> {
            if (newPerson == null) {
                if (selectedEvent.get() == null) {
                    showHelp();
                } else {
                    showEvent();
                }
            } else {
                selectedEvent.set(null);
                personPanel.updateDetails(newPerson);
                showPerson();
            }
        });

        selectedEvent.addListener((obs, old, newEvent) -> {
            if (newEvent == null) {
                if (selectedPerson.get() == null) {
                    showHelp();
                } else {
                    showPerson();
                }
            } else {
                selectedPerson.set(null);
                eventPanel.updateDetails(newEvent);
                showEvent();
            }
        });
    }

    /**
     * Shows a helpful message on initialization of AddressBook, or if the person list is empty.
     */
    public void showHelp() {
        if (activePanel == helpPanelPlaceholder) {
            return;
        }
        activePanel.setVisible(false);
        helpPanelPlaceholder.setVisible(true);
        activePanel = helpPanelPlaceholder;
    }

    /**
     * Shows detailed person information upon selection of person.
     */
    public void showPerson() {
        if (activePanel == personPanelPlaceholder) {
            return;
        }
        activePanel.setVisible(false);
        personPanelPlaceholder.setVisible(true);
        activePanel = personPanelPlaceholder;
    }

    /**
     * Shows detailed event information upon selection of event.
     */
    public void showEvent() {
        if (activePanel == eventPanelPlaceholder) {
            return;
        }
        activePanel.setVisible(false);
        eventPanelPlaceholder.setVisible(true);
        activePanel = eventPanelPlaceholder;
    }

    /**
     * Shows the consolidated information of all the persons
     */
    public void showConsolidatedInfo(String info) {
        consolidatePanel.update(info);
        if (activePanel == consolidatePanelPlaceholder) {
            return;
        }
        activePanel.setVisible(false);
        consolidatePanelPlaceholder.setVisible(true);
        activePanel = consolidatePanelPlaceholder;
    }
}
