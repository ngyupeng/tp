package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.SortUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.event.Event;
import seedu.address.model.event.PersonReference;
import seedu.address.model.event.UniqueEventList;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 * AddressBook is sorted according to the specified sort method at all times.
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueEventList events;
    private Comparator<Person> sortMethod = SortUtil.SORT_DEFAULT_PIN;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        events = new UniqueEventList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Replaces the contents of the event list with {@code events}.
     * {@code events} must not contain duplicate events.
     */
    public void setEvents(List<Event> events) {
        this.events.setEvents(events);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setEvents(newData.getEventList());
    }

    //// event-level operations

    /**
     * Returns true if an event with the same identity as {@code event} exists in the address book.
     */
    public boolean hasEvent(Event event) {
        requireNonNull(event);
        return events.contains(event);
    }

    /**
     * Adds an event to the address book.
     * The event must not already exist in the address book.
     */
    public void addEvent(Event e) {
        events.add(e);
        sort();
    }

    /**
     * Replaces the given event {@code target} in the list with {@code editedEvent}.
     * {@code target} must exist in the address book.
     * The event identity of {@code editedEvent} must not be the same as another existing event in the address book.
     */
    public void setEvent(Event target, Event editedEvent) {
        requireNonNull(editedEvent);
        events.setEvent(target, editedEvent);
        sort();
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeEvent(Event key) {
        events.remove(key);
    }

    /**
     * Returns true if the list of event is empty.
     *
     * @return The boolean result of whether the list of event is empty.
     */
    public boolean isEventListEmpty() {
        return events.isEmpty();
    }


    /**
     * Clears the event list.
     */
    public void clearEvent() {
        events.clear();
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
        sort();
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);
        events.forEach(e -> {
            if (e.getAttendance().contains(new PersonReference(target))) {
                e.getAttendance().remove(new PersonReference(target));
                e.getAttendance().add(new PersonReference(editedPerson));
            }
        });
        persons.setPerson(target, editedPerson);
        sort();
    }

    /**
     * Returns true if the list of person is empty.
     *
     * @return The boolean result of whether the list of person is empty.
     */
    public boolean isPersonListEmpty() {
        return persons.isEmpty();
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
        events.forEach(e -> {
            if (e.getAttendance().contains(new PersonReference(key))) {
                e.getAttendance().remove(new PersonReference(key));
            }
        });
    }

    /**
     * Clears the person list and attendance list of events.
     */
    public void clearPerson() {
        persons.clear();
        events.setEvents(events.asUnmodifiableObservableList()
                .stream().peek(e -> e.getAttendance().clear()).toList());
    }

    //// sort methods

    /** Sorts the contents of the person list with the current AddressBook sort method. */
    public void sort() {
        persons.sort(sortMethod);
    }

    /**
     * Sets the AddressBook sorting method to the newly provided sort method,
     * and then sorts the contents of the person list.
     */
    public void sort(Comparator<Person> personComparator) {
        sortMethod = personComparator;
        persons.sort(personComparator);
    }

    //// methods

    /**
     * AddressBook is valid as defined by whether all {@code PersonReference} in all the {@code Event} attendance list
     * properly reference an existing {@code Person} in the {@code UniquePersonList}.
     *
     * @throws DataLoadingException if any {@code PersonReference} does not reference a person in the person list.
     */
    public void isValidAddressBook() throws DataLoadingException {
        Set<PersonReference> personList = persons.asUnmodifiableObservableList()
                .stream()
                .map(PersonReference::new)
                .collect(Collectors.toUnmodifiableSet());

        Set<PersonReference> personReferences = events.asUnmodifiableObservableList().stream()
                .flatMap(e -> e.getAttendance().asUnmodifiableList().stream())
                .collect(Collectors.toSet());

        // If a reference exists in the person list, it will be removed. This should become empty.
        personReferences.removeAll(personList);

        if (!personReferences.isEmpty()) {
            throw new DataLoadingException("Error: Following person references in event attendance is invalid: "
                    + personReferences.stream().map(PersonReference::getName));
        }
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .add("events", events)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Event> getEventList() {
        return events.asUnmodifiableObservableList();
    }

    @Override
    public ObjectProperty<Person> getSelectedPerson() {
        return persons.getObservableSelectedPerson();
    }

    @Override
    public ObjectProperty<Event> getSelectedEvent() {
        return events.getObservableSelectedEvent();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return persons.equals(otherAddressBook.persons)
                && events.equals(otherAddressBook.events);
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
