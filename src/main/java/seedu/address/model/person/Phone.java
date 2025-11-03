package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Stack;

/**
 * Represents a Person's phone number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPhone(String)}
 */
public class Phone {

    // Error message has greater severity than warning message.

    public static final String WARNING_MESSAGE_FIRST_CHARACTER = "Phone number does not starts with 3/6/8/9. \n";
    public static final String WARNING_MESSAGE_LENGTH_LIMIT = "After removing spaces and hyphens, "
            + "phone number is not 8 character long. \n";
    public static final String WARNING_MESSAGE_NON_NUMERIC = "This phone number contains non-numeric characters. \n";
    public static final String WARNING_MESSAGE_IMPROPER_BRACKETS = "There is an improper use of brackets. \n";

    public static final String MESSAGE_CONSTRAINTS = "Phone number cannot be empty after removing spaces and hyphens.";

    public final String value;

    /**
     * Constructs a {@code Phone}.
     *
     * @param phone A valid phone number.
     */
    public Phone(String phone) {
        requireNonNull(phone);
        checkArgument(isValidPhone(phone), MESSAGE_CONSTRAINTS);
        value = convertRawFormat(phone);
    }

    /**
     * Removes spaces and hyphens to give raw format of number.
     *
     * @param test Phone number that potentially have spaces and hyphens.
     * @return Phone number with spaces and hyphens removed.
     */
    public static String convertRawFormat(String test) {
        return test.replaceAll("[\\s-]", "");
    }

    /**
     * Checks if the phone number is 8 characters long.
     *
     * @param test Phone number to be tested.
     * @return The conditional check of whether the phone number is 8 characters long.
     */
    public static boolean hasEightCharacters(String test) {
        // Remove space and hyphens from test string first
        test = convertRawFormat(test);

        // Check for conditions
        if (test == null) {
            return false;
        }
        return test.length() == 8;
    }

    private static boolean hasOnlyNumbers(String test) {
        return test.chars().allMatch(Character::isDigit);
    }

    private static boolean hasValidFirstDigit(String test) {
        test = convertRawFormat(test);
        if (test.length() == 0) {
            return false;
        }
        int firstDigit = test.charAt(0);

        // 3, 6, 8 and 9 are valid first digit for Singaporean phone number
        return firstDigit == '3' || firstDigit == '6' || firstDigit == '8' || firstDigit == '9';
    }

    /**
     * Returns true if phone number needs to have warning.
     *
     * @param test Phone number provided.
     * @return The conditional check of whether the phone number has warnings.
     */
    public static boolean hasWarning(String test) {
        test = convertRawFormat(test);
        if (!hasOnlyNumbers(test)) {
            return true;
        }
        if (!hasValidFirstDigit(test)) {
            return true;
        }
        if (!hasEightCharacters(test)) {
            return true;
        }
        return false;
    }

    private static boolean hasBracket(String input) {
        return input.contains("(")
                || input.contains(")")
                || input.contains("[")
                || input.contains("]")
                || input.contains("{")
                || input.contains("}");
    }

    private static boolean hasValidUseOfBracket(String input) {
        if (!hasBracket(input)) {
            // Vacuously true
            return true;
        }

        Stack<Character> characters = new Stack<>();
        for (char character : input.toCharArray()) {
            if (isOpeningBracket(character)) {
                characters.push(character);
            }

            if (!isClosingBracket(character)) {
                continue;
            }

            // When the character is a closing bracket
            if (characters.isEmpty()) {
                return false;
            }
            char previousBracket = characters.pop();
            if (!isValidBracketMatching(previousBracket, character)) {
                return false;
            }
        }

        return characters.isEmpty();
    }

    private static boolean isOpeningBracket(char character) {
        return character == '(' || character == '{' || character == '[';
    }

    private static boolean isClosingBracket(char character) {
        return character == ')' || character == '}' || character == ']';
    }

    private static boolean isValidBracketMatching(char previousBracket, char nextBracket) {
        if (nextBracket == ')' && previousBracket == '(') {
            return true;
        }
        if (nextBracket == ']' && previousBracket == '[') {
            return true;
        }
        if (nextBracket == '}' && previousBracket == '{') {
            return true;
        }
        return false;
    }

    /**
     * Creates warning message depending on the issue(s) of phone number provided.
     *
     * @param test Phone number provided.
     * @return A compilation of the warnings pertaining to the phone number provided.
     */
    public static String createWarningMessage(String test) {
        test = convertRawFormat(test);
        String warningMessage = String.format("Note (Phone number %s may have issues):\n", test);

        int counter = 1;

        if (!hasEightCharacters(test)) {
            warningMessage += counter + ". " + WARNING_MESSAGE_LENGTH_LIMIT;
            counter += 1;
        }
        if (!hasOnlyNumbers(test)) {
            warningMessage += counter + ". " + WARNING_MESSAGE_NON_NUMERIC;
            counter += 1;
        }
        if (!hasValidUseOfBracket(test)) {
            warningMessage += counter + ". " + WARNING_MESSAGE_IMPROPER_BRACKETS;
            counter += 1;
        }
        if (!hasValidFirstDigit(test)) {
            warningMessage += counter + ". " + WARNING_MESSAGE_FIRST_CHARACTER;
        }

        return warningMessage;
    }

    /**
     * Returns true if a given string is a valid phone number.
     *
     * @param test Phone number provided.
     * @return The conditional check of whether the phone number is valid.
     */
    public static boolean isValidPhone(String test) {
        test = convertRawFormat(test);
        if (test.length() == 0) {
            return false;
        }
        // As long as test is not 0 in length, test is a valid phone, since there is
        // no hard constraints for phone.

        return true;
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
        if (!(other instanceof Phone)) {
            return false;
        }

        Phone otherPhone = (Phone) other;
        return value.equals(otherPhone.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
