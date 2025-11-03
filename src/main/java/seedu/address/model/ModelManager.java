package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.event.Event;
import seedu.address.model.person.Person;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Event> filteredEvents;
    /**
     * Whether to zoom in on the selected event/person (e.g. only show events of selected person).
     */
    private ObjectProperty<ZoomIn> zoomInSelected = new SimpleObjectProperty<>(new ZoomIn());

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        filteredPersons.setPredicate(PREDICATE_SHOW_ALL_PERSONS);
        filteredEvents = new FilteredList<>(this.addressBook.getEventList());
        filteredEvents.setPredicate(PREDICATE_SHOW_ALL_EVENTS);
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public void sortAddressBook(Comparator<Person> comparator) {
        addressBook.sort(comparator);
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
        updateSelectedPerson();
    }

    @Override
    public void addPerson(Person person) {
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        addressBook.addPerson(person);
    }

    @Override
    public boolean isPersonEmptyAddressBook() {
        return addressBook.isPersonListEmpty();
    }

    @Override
    public boolean isEventEmptyAddressBook() {
        return addressBook.isEventListEmpty();
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);
        addressBook.setPerson(target, editedPerson);
    }

    @Override
    public void clearPerson() {
        addressBook.clearPerson();
    }

    @Override
    public boolean hasEvent(Event event) {
        requireNonNull(event);
        return addressBook.hasEvent(event);
    }

    @Override
    public void addEvent(Event event) {
        updateFilteredEventList(PREDICATE_SHOW_ALL_EVENTS);
        addressBook.addEvent(event);
    }

    @Override
    public void setEvent(Event target, Event editedEvent) {
        requireAllNonNull(target, editedEvent);
        addressBook.setEvent(target, editedEvent);
    }

    @Override
    public void deleteEvent(Event target) {
        addressBook.removeEvent(target);
    }

    @Override
    public void clearEvent() {
        addressBook.clearEvent();
    }

    //=========== Filtered Event List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Event} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Event> getFilteredEventList() {
        return filteredEvents;
    }

    @Override
    public void updateFilteredEventList(Predicate<Event> predicate) {
        requireNonNull(predicate);
        filteredEvents.setPredicate(predicate);
        // Any time we modify the filtered events list only, we want to reset our filtered persons list to all persons,
        // as we might currently only be showing a subset of persons.
        // (e.g. if we were looking at one student's events, then we add an event).
        filteredPersons.setPredicate(PREDICATE_SHOW_ALL_PERSONS);
        zoomInSelected.set(new ZoomIn());
        updateSelectedEvent();
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
        // Any time we modify the filtered persons list ony, we want to reset our filtered events list to all events,
        // as we might currently only be showing a subset of events (e.g. if we were looking at one student's events).
        filteredEvents.setPredicate(PREDICATE_SHOW_ALL_EVENTS);
        zoomInSelected.set(new ZoomIn());
        updateSelectedPerson();
    }

    //=========== Filtered Person and Event List Accessors ==============================================

    @Override
    public void updateFilteredPersonAndEventList(Predicate<Person> personPredicate, Predicate<Event> eventPredicate,
                                                 ZoomIn zoomIn) {
        requireAllNonNull(personPredicate, eventPredicate);

        // We should only be calling this when we want to zoom in
        assert (zoomIn.getType() != ZoomInType.NONE);
        filteredPersons.setPredicate(personPredicate);
        filteredEvents.setPredicate(eventPredicate);

        zoomInSelected.set(zoomIn);
        switch (zoomIn.getType()) {
        case PERSON:
            updateSelectedPerson();
            break;

        case EVENT:
            updateSelectedEvent();
            break;

        default:
        }
    }

    /**
     * Returns an unmodifiable view of whether to zoom in on the selected event/person.
     */
    public ObjectProperty<ZoomIn> getZoomInSelected() {
        return zoomInSelected;
    }

    //=========== Selected Person Accessors =============================================================

    @Override
    public ObjectProperty<Person> getSelectedPerson() {
        return addressBook.getSelectedPerson();
    }

    @Override
    public void updateSelectedPerson() {
        if (filteredPersons.isEmpty()) {
            getSelectedPerson().set(null);
        } else if (getSelectedPerson().get() == null || !filteredPersons.contains(getSelectedPerson().get())) {
            getSelectedPerson().set(filteredPersons.get(0));
        }
    }

    //=========== Selected Event Accessors =============================================================

    @Override
    public ObjectProperty<Event> getSelectedEvent() {
        return addressBook.getSelectedEvent();
    }

    @Override
    public void updateSelectedEvent() {
        if (filteredEvents.isEmpty()) {
            getSelectedEvent().set(null);
        } else if (getSelectedEvent().get() == null || !filteredEvents.contains(getSelectedEvent().get())) {
            getSelectedEvent().set(filteredEvents.get(0));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons)
                && filteredEvents.equals(otherModelManager.filteredEvents);
    }

}
