package seedu.address.commons.util;

import java.util.Arrays;

/**
 * Defines the style guide check functions and the message associated.
 */
public class StyleUtil {
    public static final String MESSAGE_CONSECUTIVE_SPACES = "Style warning: "
        + "%s `%s` contains multiple consecutive spaces.";
    public static final String MESSAGE_BRACKET_NOT_CLOSED = "Style warning: %s `%s` opening bracket "
            + "does not have a matching closing bracket.";
    public static final String MESSAGE_INCORRECT_CAPITALIZATION = "Style warning: "
        + "%s `%s` does not have proper capitalization or alphabetical name.";

    /**
     * Separates the text into words based on spaces or certain special characters.
     * Tests if for each word, it either has no letters, or the first letter character that appears is capitalized
     * with following letters that are not capitalized.
     */
    public static boolean isCapitalizedWithLetters(String trimmedText) {
        final String regex = "([^a-zA-Z]*[A-Z][^A-Z]*)|([^a-zA-Z]*)";
        String[] words = trimmedText.split("[ \\-,/()]"); // Does not include apostrophe, e.g. John's.
        return Arrays.stream(words).allMatch(w -> w.matches(regex));
    }

    /**
     * Tests if the string contains two or more consecutive spaces.
     */
    public static boolean hasConsecutiveSpaces(String trimmedText) {
        final String regexDoubleSpace = ".* {2}.*";
        return trimmedText.matches(regexDoubleSpace);
    }

    /**
     * Returns true if the given string has a balanced bracket matching.
     */
    public static boolean hasBalancedBrackets(String trimmedText) {
        int openBracketCount = 0;
        for (char c : trimmedText.toCharArray()) {
            if (c == '(') {
                openBracketCount++;
            } else if (c == ')') {
                openBracketCount--;
            }
            if (openBracketCount < 0) {
                return false;
            }
        }
        return openBracketCount == 0;
    }
}
