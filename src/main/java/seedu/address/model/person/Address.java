package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.StyleUtil.MESSAGE_CONSECUTIVE_SPACES;
import static seedu.address.commons.util.StyleUtil.MESSAGE_INCORRECT_CAPITALIZATION;
import static seedu.address.commons.util.StyleUtil.hasConsecutiveSpaces;
import static seedu.address.commons.util.StyleUtil.isCapitalizedWithLetters;

/**
 * Represents a Person's address in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidAddress(String)}
 */
public class Address {

    public static final String MESSAGE_CONSTRAINTS = "Addresses can take any values, and it should not be blank, "
            + "and is at maximum 70 characters long.";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[^\\s].*";

    public final String value;

    /**
     * Constructs an {@code Address}.
     *
     * @param address A valid address.
     */
    public Address(String address) {
        requireNonNull(address);
        checkArgument(isValidAddress(address), MESSAGE_CONSTRAINTS);
        value = address;
    }

    /**
     * Returns true if a given string is a valid email.
     */
    public static boolean isValidAddress(String test) {
        return test.length() <= 70 && test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns a message containing details about not following the recommend style format.
     * Does not throw an error.
     */
    public static String getStyleWarningMessage(String test) {
        StringBuilder styleWarning = new StringBuilder();

        if (hasConsecutiveSpaces(test)) {
            styleWarning.append(
                    String.format(MESSAGE_CONSECUTIVE_SPACES, Address.class.getSimpleName(), test));
        }
        if (!isCapitalizedWithLetters(test)) {
            styleWarning.append(
                    String.format(MESSAGE_INCORRECT_CAPITALIZATION, Address.class.getSimpleName(), test));
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
        if (!(other instanceof Address)) {
            return false;
        }

        Address otherAddress = (Address) other;
        return value.equals(otherAddress.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
