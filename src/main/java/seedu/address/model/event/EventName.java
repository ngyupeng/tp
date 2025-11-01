package seedu.address.model.event;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.StyleUtil.MESSAGE_BRACKET_NOT_CLOSED;
import static seedu.address.commons.util.StyleUtil.MESSAGE_CONSECUTIVE_SPACES;
import static seedu.address.commons.util.StyleUtil.MESSAGE_INCORRECT_CAPITALIZATION;
import static seedu.address.commons.util.StyleUtil.hasBalancedBrackets;
import static seedu.address.commons.util.StyleUtil.hasConsecutiveSpaces;
import static seedu.address.commons.util.StyleUtil.isCapitalizedWithLetters;

/**
 * Represents an Event's name in the event manager.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class EventName {
    public static final String MESSAGE_CONSTRAINTS = "Event name can take any values, and it should not be blank";

    /*
     * The first character of the name must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[^\\s].*";

    public final String value;

    /**
     * Constructs an {@code EventName}.
     *
     * @param name A valid name.
     */
    public EventName(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        value = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns a message containing details about not following the recommend style format.
     * Does not throw an error.
     */
    public static String getStyleWarningMessage(String test) {
        StringBuilder styleWarning = new StringBuilder();

        if (hasConsecutiveSpaces(test)) {
            styleWarning.append(
                    String.format(MESSAGE_CONSECUTIVE_SPACES, "Event name", test));
        }
        if (!hasBalancedBrackets(test)) {
            styleWarning.append(
                    String.format(MESSAGE_BRACKET_NOT_CLOSED, "Event name", test));
        }
        if (!isCapitalizedWithLetters(test)) {
            styleWarning.append(
                    String.format(MESSAGE_INCORRECT_CAPITALIZATION, "Event name", test));
        }
        return styleWarning.toString();
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EventName)) {
            return false;
        }

        EventName otherName = (EventName) other;
        return value.equals(otherName.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
