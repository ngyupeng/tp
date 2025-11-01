package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERSON;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
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

/**
 * Removes the list of persons to the attendance list of an event.
 */
public class UnattendCommand extends Command {
    public static final String COMMAND_WORD = "unattend:event";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes attendance from an event index of people at the given indexes.\n"
            + "Parameters: "
            + PREFIX_EVENT + "EVENT_INDEX "
            + PREFIX_PERSON + "PERSON_INDEX [MORE_INDEXES]...\n"
            + "Example: unattend:event e/2 p/1 4 5 6";

    public static final String MESSAGE_SUCCESS = "Attendance of Persons removed: %1$s";
    private final Index eventIndex;
    private final List<Index> attendees;

    /**
     * Constructs a new UnattendCommand with an event index and a list of person indexes from its attendance to remove.
     */
    public UnattendCommand(Index event, List<Index> attendees) {
        this.eventIndex = event;
        this.attendees = attendees;
    }

    /**
     * Creates and returns a {@code Event} with the new attendance list.
     */
    private static Event createEditedEvent(Event eventToEdit, List<PersonReference> personsToRemove) {
        assert eventToEdit != null;

        EventName name = eventToEdit.getName();
        Duration duration = eventToEdit.getDuration();
        Description description = eventToEdit.getDescription();
        Attendance updatedAttendance = new Attendance(eventToEdit.getAttendance());
        updatedAttendance.removeAll(personsToRemove);

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

        if (eventIndex.getZeroBased() >= model.getFilteredEventList().size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }

        Event eventToEdit = model.getFilteredEventList().get(eventIndex.getZeroBased());
        Attendance attendance = eventToEdit.getAttendance();
        List<PersonReference> personList = attendance.asUnmodifiableList();
        List<PersonReference> personsToRemove = new ArrayList<>();

        for (Index i : attendees) {
            if (i.getZeroBased() < personList.size()) {
                personsToRemove.add(personList.get(i.getZeroBased()));
            } else {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        // Convert to names to display text result of names added.
        List<Name> names = personsToRemove.stream()
                .map(PersonReference::getName)
                .toList();

        Event editedEvent = createEditedEvent(eventToEdit, personsToRemove);
        model.setEvent(eventToEdit, editedEvent);

        return new CommandResult(String.format(MESSAGE_SUCCESS, names));
    }
}
