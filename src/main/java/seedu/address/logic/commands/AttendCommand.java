package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERSON;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.MessageCenter;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Attendance;
import seedu.address.model.event.Description;
import seedu.address.model.event.Duration;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventName;
import seedu.address.model.event.PersonReference;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Adds the list of persons to the attendance list of an event.
 */
public class AttendCommand extends Command {
    public static final String COMMAND_WORD = "attend:event";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds the list of persons at the given indexes to the attendance list of an event.\n"
            + "Parameters: "
            + PREFIX_EVENT + "EVENT_INDEX "
            + PREFIX_PERSON + "PERSON_INDEX [MORE_INDEXES]...\n"
            + "Example: attend:event e/2 p/1 4 5 6";

    public static final String MESSAGE_SUCCESS = "Attendance of Persons added: %1$s";

    private final Index event;
    private final List<Index> attendees;

    /**
     * Constructs a new AttendCommand with an event index and a list of person indexes.
     */
    public AttendCommand(Index event, List<Index> attendees) {
        requireNonNull(event);
        requireNonNull(attendees);
        assert !attendees.isEmpty() : "attendees should not be empty.";

        this.event = event;
        this.attendees = attendees;
    }

    /**
     * Creates and returns a {@code Event} with the new attendance list.
     */
    private static Event createEditedEvent(Event eventToEdit, List<PersonReference> personsToAdd) {
        assert eventToEdit != null;

        EventName name = eventToEdit.getName();
        Duration duration = eventToEdit.getDuration();
        Description description = eventToEdit.getDescription();
        Attendance updatedAttendance = new Attendance(eventToEdit.getAttendance());
        for (PersonReference p : personsToAdd) {
            if (updatedAttendance.contains(p)) {
                MessageCenter.appendEnd(
                        String.format("Warning: %s is already in the event's attendance list. "
                                        + "Person not added.",
                                p.getName().fullName));
            } else {
                updatedAttendance.add(p);
            }
        }
        return new Event(name, duration, description, updatedAttendance);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (model.isPersonEmptyAddressBook()) {
            throw new CommandException(Messages.specifyEmptyUserListMessage(COMMAND_WORD));
        }

        if (model.isEventEmptyAddressBook()) {
            throw new CommandException(Messages.specifyEmptyEventListMessage(COMMAND_WORD));
        }

        if (event.getZeroBased() >= model.getFilteredEventList().size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }

        Event eventToEdit = model.getFilteredEventList().get(event.getZeroBased());
        List<PersonReference> personsToAdd = new ArrayList<>();

        for (Index i : attendees) {
            if (i.getZeroBased() < model.getFilteredPersonList().size()) {
                Person p = model.getFilteredPersonList().get(i.getZeroBased());
                personsToAdd.add(new PersonReference(p));
            } else {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        // Convert to names to display text result of names added.
        List<Name> names = personsToAdd.stream()
                .filter(p -> !eventToEdit.getAttendance().contains(p))
                .map(PersonReference::getName)
                .toList();

        Event edittedEvent = createEditedEvent(eventToEdit, personsToAdd);
        model.setEvent(eventToEdit, edittedEvent);

        return new CommandResult(String.format(MESSAGE_SUCCESS, names));
    }
}
