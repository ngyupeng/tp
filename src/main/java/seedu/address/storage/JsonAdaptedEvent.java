package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.MessageCenter;
import seedu.address.model.event.Attendance;
import seedu.address.model.event.Description;
import seedu.address.model.event.Duration;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventName;
import seedu.address.model.event.PersonReference;

/**
 * Jackson-friendly version of {@link Event}.
 */
class JsonAdaptedEvent {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Event's %s field is missing!";

    private final String name;
    private final String duration;
    private final String description;
    private final List<JsonAdaptedPersonReference> attendance = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedEvent} with the given event details.
     */
    @JsonCreator
    public JsonAdaptedEvent(@JsonProperty("name") String name, @JsonProperty("duration") String duration,
                             @JsonProperty("description") String description,
                             @JsonProperty("attendance") List<JsonAdaptedPersonReference> attendance) {
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.attendance.addAll(attendance);
    }

    /**
     * Converts a given {@code Event} into this class for Jackson use.
     */
    public JsonAdaptedEvent(Event source) {
        name = source.getName().value;
        duration = source.getDuration().toString();
        description = source.getDescription().value;
        attendance.addAll(source.getAttendance().asUnmodifiableList().stream()
                .map(JsonAdaptedPersonReference::new)
                .collect(Collectors.toUnmodifiableSet()));
    }

    /**
     * Converts this Jackson-friendly adapted event object into the model's {@code Event} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted event.
     */
    public Event toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(
                MISSING_FIELD_MESSAGE_FORMAT, EventName.class.getSimpleName()));
        }
        if (!EventName.isValidName(name)) {
            throw new IllegalValueException(EventName.MESSAGE_CONSTRAINTS);
        }
        MessageCenter.appendEnd(EventName.getStyleWarningMessage(name));
        final EventName modelName = new EventName(name);

        if (duration == null) {
            throw new IllegalValueException(String.format(
                MISSING_FIELD_MESSAGE_FORMAT, Duration.class.getSimpleName()));
        }
        if (!Duration.isValidDuration(duration)) {
            throw new IllegalValueException(Duration.MESSAGE_CONSTRAINTS);
        }
        final Duration modelDuration = new Duration(duration);

        if (description == null) {
            throw new IllegalValueException(String.format(
                MISSING_FIELD_MESSAGE_FORMAT, Description.class.getSimpleName()));
        }
        if (!Description.isValidDescription(description)) {
            throw new IllegalValueException(Description.MESSAGE_CONSTRAINTS);
        }
        final Description modelDescription = new Description(description);

        List<PersonReference> modelList = new ArrayList<>();
        for (JsonAdaptedPersonReference pr : attendance) {
            modelList.add(pr.toModelType());
        }
        Attendance modelAttendance = new Attendance(modelList);

        return new Event(modelName, modelDuration, modelDescription, modelAttendance);
    }

}
