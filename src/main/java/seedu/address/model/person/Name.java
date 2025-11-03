package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.StyleUtil.MESSAGE_BRACKET_NOT_CLOSED;
import static seedu.address.commons.util.StyleUtil.MESSAGE_CONSECUTIVE_SPACES;
import static seedu.address.commons.util.StyleUtil.MESSAGE_INCORRECT_CAPITALIZATION;
import static seedu.address.commons.util.StyleUtil.hasBalancedBrackets;
import static seedu.address.commons.util.StyleUtil.hasConsecutiveSpaces;
import static seedu.address.commons.util.StyleUtil.isCapitalizedWithLetters;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS = "Names must be at most 50 characters long, should not be blank, "
            + "and names should only contain alphanumeric characters, spaces, and the following characters: "
            + ".,-'()/";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}.,\\-'()][\\p{Alnum}.,\\-'()/ ]*";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        final boolean lengthLessThan50 = test.length() <= 50;
        final boolean hasValidCharacters = test.matches(VALIDATION_REGEX);
        return lengthLessThan50 & hasValidCharacters;
    }

    /**
     * Returns a message containing details about not following the recommend style format.
     * Does not throw an error.
     */
    public static String getStyleWarningMessage(String test) {
        StringBuilder styleWarning = new StringBuilder();

        if (hasConsecutiveSpaces(test)) {
            styleWarning.append(
                    String.format(MESSAGE_CONSECUTIVE_SPACES, Name.class.getSimpleName(), test));
        }
        if (!hasBalancedBrackets(test)) {
            styleWarning.append(
                    String.format(MESSAGE_BRACKET_NOT_CLOSED, Name.class.getSimpleName(), test));
        }
        if (!isCapitalizedWithLetters(test)) {
            styleWarning.append(
                    String.format(MESSAGE_INCORRECT_CAPITALIZATION, Name.class.getSimpleName(), test));
        }
        return styleWarning.toString();
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }



}
