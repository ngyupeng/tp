package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.ArrayList;

/**
 * Represents a Person's email in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidEmail(String)}
 */
public class Email {

    public static final String SPECIAL_CHARACTERS = "+_.-";
    public static final Integer EMAIL_LENGTH_LIMIT = 50;

    // Specialise warning message
    public static final String ERROR_MESSAGE_INTRO = "Emails should be of the format local-part@domain. "
            + "Check that:  \n";
    public static final String ERROR_MESSAGE_LENGTH = "Email address length should be "
            + EMAIL_LENGTH_LIMIT
            + " characters or less. \n";
    public static final String ERROR_MESSAGE_SPECIAL_CHARACTER = "Email address should have 1 @ character. \n";
    public static final String ERROR_MESSAGE_LOCAL = "The local-part should only contain alphanumeric characters, "
            + "and these special characters, excluding the parentheses, ("
            + SPECIAL_CHARACTERS
            + "). Special characters to be separated by at least one alphanumeric characters. \n"
            + "The local-part may not start or end with any special characters.\n";
    public static final String ERROR_MESSAGE_DOMAIN = "The domain name is "
            + "made up of domain labels separated by periods. The domain name must:\n"
            + "    - end with a domain label at least 2 characters long\n"
            + "    - have each domain label starts and ends with alphanumeric characters\n"
            + "    - have each domain label consists of alphanumeric characters and hyphens\n"
            + "    - contain at least 2 domain labels.\n";

    public static final String MESSAGE_CONSTRAINTS = ERROR_MESSAGE_INTRO
            + "1. " + ERROR_MESSAGE_LENGTH
            + "2. " + ERROR_MESSAGE_SPECIAL_CHARACTER
            + "3. " + ERROR_MESSAGE_LOCAL
            + "4. " + ERROR_MESSAGE_DOMAIN;

    // alphanumeric and special characters
    private static final String ALPHANUMERIC_NO_UNDERSCORE = "[^\\W_]+"; // alphanumeric characters except underscore
    private static final String LOCAL_PART_REGEX = "^" + ALPHANUMERIC_NO_UNDERSCORE + "([" + SPECIAL_CHARACTERS + "]"
            + ALPHANUMERIC_NO_UNDERSCORE + ")*";
    private static final String DOMAIN_PART_REGEX = ALPHANUMERIC_NO_UNDERSCORE
            + "(-" + ALPHANUMERIC_NO_UNDERSCORE + ")*";
    private static final String DOMAIN_LAST_PART_REGEX = "(" + DOMAIN_PART_REGEX + "){2,}$"; // At least two chars
    private static final String DOMAIN_REGEX = "(" + DOMAIN_PART_REGEX + "\\.)+" + DOMAIN_LAST_PART_REGEX;
    public static final String VALIDATION_REGEX = LOCAL_PART_REGEX + "@" + DOMAIN_REGEX;

    public final String value;

    /**
     * Constructs an {@code Email}.
     *
     * @param email A valid email address.
     */
    public Email(String email) {
        requireNonNull(email);
        checkArgument(isValidEmail(email), MESSAGE_CONSTRAINTS);
        value = email;
    }

    /**
     * Returns if a given string is a valid email.
     */
    public static boolean isValidEmail(String test) {
        if (!hasAtSymbol(test)) {
            return false;
        }
        return test.length() <= EMAIL_LENGTH_LIMIT
                && countNumberOfAtSymbol(test) == 1
                && test.matches(VALIDATION_REGEX);
    }

    /**
     * Lists the various source of invalid email address.
     */
    public enum SourceOfEmailIssue {
        LOCAL, DOMAIN, LENGTH, SPECIAL_CHARACTER
    }

    private static long countNumberOfAtSymbol(String emailAddress) {
        return emailAddress.chars().filter(c -> c == '@').count();
    }

    private static boolean hasAtSymbol(String emailAddress) {
        return emailAddress.contains(String.valueOf('@'));
    }

    /**
     * Identifies what is the source that cause emailAddress to be invalid.
     *
     * @param emailAddress The email address that has the issue.
     * @return An ArrayList containing all the source(s) of error.
     */
    public static ArrayList<SourceOfEmailIssue> identifyEmailSegmentWithError(String emailAddress) {
        ArrayList<SourceOfEmailIssue> result = new ArrayList<>();
        long numberOfAtSymbol = countNumberOfAtSymbol(emailAddress);
        if (numberOfAtSymbol != 1) {
            result.add(SourceOfEmailIssue.SPECIAL_CHARACTER);
        }

        if (emailAddress.length() > EMAIL_LENGTH_LIMIT) {
            result.add(SourceOfEmailIssue.LENGTH);
        }

        if (numberOfAtSymbol == 1) {
            // Split the email address into 2, as email has 2 segment, local and domain segment
            String[] emailSegments = emailAddress.split("@", 2);
            String local = emailSegments[0];
            String domain = emailSegments[1];

            if (!local.matches(LOCAL_PART_REGEX)) {
                result.add(SourceOfEmailIssue.LOCAL);
            }

            if (!domain.matches(DOMAIN_REGEX)) {
                result.add(SourceOfEmailIssue.DOMAIN);
            }
        }

        // Only when the email address have issue, do we call the identifyEmailSegmentWithError function.
        assert result.size() > 0 : "There should be at least one source of error.";
        return result;
    }

    private static String createSubWarning(SourceOfEmailIssue source, SourceOfEmailIssue comparison, String message) {
        if (source == comparison) {
            return message;
        } else {
            return "";
        }
    }

    /**
     * Returns a consolidation of error message(s) depending on the source of error.
     *
     * @param sourcesOfError Sources of error pertaining to the email.
     * @return A description of the sources of error.
     */
    public static String createErrorMessage(ArrayList<SourceOfEmailIssue> sourcesOfError) {
        int counter = 1;
        String message = ERROR_MESSAGE_INTRO;

        for (SourceOfEmailIssue source : sourcesOfError) {
            message += counter + ". ";
            message += Email.createSubWarning(source, SourceOfEmailIssue.LENGTH, ERROR_MESSAGE_LENGTH);
            message += Email.createSubWarning(source,
                    SourceOfEmailIssue.SPECIAL_CHARACTER, ERROR_MESSAGE_SPECIAL_CHARACTER);
            message += Email.createSubWarning(source, SourceOfEmailIssue.LOCAL, ERROR_MESSAGE_LOCAL);
            message += Email.createSubWarning(source, SourceOfEmailIssue.DOMAIN, ERROR_MESSAGE_DOMAIN);
            counter = counter + 1;
        }

        return message;
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
        if (!(other instanceof Email)) {
            return false;
        }

        Email otherEmail = (Email) other;
        return value.equals(otherEmail.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
