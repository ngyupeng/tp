package seedu.address.model.role;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.StyleUtil.MESSAGE_CONSECUTIVE_SPACES;
import static seedu.address.commons.util.StyleUtil.MESSAGE_INCORRECT_CAPITALIZATION;
import static seedu.address.commons.util.StyleUtil.hasConsecutiveSpaces;
import static seedu.address.commons.util.StyleUtil.isCapitalizedWithLetters;

/**
 * Represents a Role for a contact in the address book.
 * Guarantees: immutable; role name is valid as declared in {@link #isValidRoleName(String)}
 */
public class Role {
    public static final int PERSON_MAX_ROLES = 3;
    public static final String PERSON_ROLES_SIZE_CONSTRAINT = "Each person can have a maximum of 3 roles!";
    public static final String MESSAGE_CONSTRAINTS = "Role names should be alphanumeric with spaces, "
            + "and each role is at max 20 characters long, "
            + "and each person can have a maximum of 3 roles.";
    public static final String FIND_MESSAGE_CONSTRAINTS = "Supplied substrings for roles should be "
            + "alphanumeric with spaces and at max 20 characters long, or empty to search for all "
            + "people with at least one role.";
    public static final String VALIDATION_REGEX = "^[a-zA-Z0-9 ]+$";

    public final String roleName;

    /**
     * Constructs a {@code Role}.
     *
     * @param roleName A valid role name.
     */
    public Role(String roleName) {
        requireNonNull(roleName);
        checkArgument(isValidRoleName(roleName), MESSAGE_CONSTRAINTS);
        this.roleName = roleName;
    }

    /**
     * Returns true if a given string is a valid role name.
     */
    public static boolean isValidRoleName(String test) {
        return test.length() <= 20 && test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if a given string is a valid role name.
     */
    public static boolean isValidFindString(String test) {
        return test.isEmpty() || isValidRoleName(test);
    }

    /**
     * Returns a message containing details about not following the recommend style format.
     * Does not throw an error.
     */
    public static String getStyleWarningMessage(String test) {
        StringBuilder styleWarning = new StringBuilder();

        if (hasConsecutiveSpaces(test)) {
            styleWarning.append(
                    String.format(MESSAGE_CONSECUTIVE_SPACES, Role.class.getSimpleName(), test));
        }
        if (!isCapitalizedWithLetters(test)) {
            styleWarning.append(
                    String.format(MESSAGE_INCORRECT_CAPITALIZATION, Role.class.getSimpleName(), test));
        }
        return styleWarning.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Role)) {
            return false;
        }

        Role otherRole = (Role) other;
        return roleName.equals(otherRole.roleName);
    }

    @Override
    public int hashCode() {
        return roleName.hashCode();
    }

    @Override
    public String toString() {
        return roleName;
    }
}
