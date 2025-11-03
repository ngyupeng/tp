package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.role.Role.PERSON_MAX_ROLES;
import static seedu.address.model.tag.Tag.PERSON_MAX_TAGS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.MessageCenter;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.event.Description;
import seedu.address.model.event.Duration;
import seedu.address.model.event.EventName;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyContact;
import seedu.address.model.person.EnrollmentYear;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Pin;
import seedu.address.model.role.Role;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {
    public static final String MESSAGE_EMPTY_INDEX = "Expecting an index, but no index specified.";
    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_EXCEEDS_INTEGER_LIMIT = "Index exceeds integer limit of 2147483647.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (oneBasedIndex.isEmpty()) {
            throw new ParseException(MESSAGE_EMPTY_INDEX);
        }
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            if (!trimmedIndex.isEmpty() && StringUtil.isExceedsIntegerLimit(trimmedIndex)) {
                throw new ParseException(MESSAGE_EXCEEDS_INTEGER_LIMIT);
            } else {
                throw new ParseException(MESSAGE_INVALID_INDEX);
            }
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     * Checks for style guide and appends a warning to the user.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseNameWithWarning(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        Name result = parseName(trimmedName);
        MessageCenter.appendEnd(Name.getStyleWarningMessage(trimmedName));
        return result;
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);

        String trimmedPhone = phone.trim();
        String rawNumber = Phone.convertRawFormat(trimmedPhone);
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        if (Phone.hasWarning(rawNumber)) {
            MessageCenter.appendEnd(Phone.createWarningMessage(rawNumber));
        }

        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddressWithWarning(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        Address result = parseAddress(trimmedAddress);
        MessageCenter.appendEnd(Address.getStyleWarningMessage(trimmedAddress));
        return result;
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);

        String trimmedEmail = email.trim();

        if (!Email.isValidEmail(trimmedEmail)) {
            ArrayList<Email.SourceOfEmailIssue> sourcesOfErrors = Email.identifyEmailSegmentWithError(trimmedEmail);
            throw new ParseException(Email.createErrorMessage(sourcesOfErrors));
        }

        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code boolean pin} into a {@code Pin}.
     */
    public static Pin parsePin(String pin) throws ParseException {
        requireNonNull(pin);
        String trimmedPin = pin.trim();
        if (trimmedPin.equalsIgnoreCase("TRUE")) {
            return new Pin(true);
        } else if (trimmedPin.equalsIgnoreCase("FALSE")) {
            return new Pin(false);
        } else {
            throw new ParseException(Pin.MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Parses an {@code emergencyName, emergencyPhone} into an {@code EmergencyContact}.
     */
    public static EmergencyContact parseEmergencyContact(String emergencyName, String emergencyPhone)
            throws ParseException {
        try {
            Name name = emergencyName == null ? null : ParserUtil.parseName(emergencyName);
            Phone phone = emergencyPhone == null ? null : ParserUtil.parsePhone(emergencyPhone);
            return new EmergencyContact(name, phone);
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage());
        }
    }

    /**
     * Parses a {@code String role} into a {@code Role}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code role} is invalid.
     */
    public static Role parseRole(String role) throws ParseException {
        requireNonNull(role);
        String trimmedRole = role.trim();
        if (!Role.isValidRoleName(trimmedRole)) {
            throw new ParseException(Role.MESSAGE_CONSTRAINTS);
        }
        MessageCenter.appendEnd(Role.getStyleWarningMessage(trimmedRole));
        return new Role(trimmedRole);
    }

    /**
     * Parses {@code Collection<String> roles} into a {@code Set<Role>}.
     */
    public static Set<Role> parseRoles(Collection<String> roles) throws ParseException {
        requireNonNull(roles);
        final Set<Role> roleSet = new HashSet<>();
        for (String roleName : roles) {
            roleSet.add(parseRole(roleName));
        }
        if (roleSet.size() > PERSON_MAX_ROLES) {
            throw new ParseException(Role.PERSON_ROLES_SIZE_CONSTRAINT);
        }
        return roleSet;
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }


    /**
     * Parses a {@code String enrollmentYear} into a {@code EnrollmentYear}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code enrollmentYear} is invalid.
     */
    public static EnrollmentYear parseEnrollmentYear(String year) throws ParseException {
        requireNonNull(year);
        String trimmedYear = year.trim();
        if (trimmedYear.isEmpty()) {
            return new EnrollmentYear();
        } else if (EnrollmentYear.isValidYear(trimmedYear)) {
            return new EnrollmentYear(trimmedYear);
        } else {
            throw new ParseException(EnrollmentYear.MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        if (tagSet.size() > PERSON_MAX_TAGS) {
            throw new ParseException(Tag.PERSON_TAGS_SIZE_CONSTRAINT);
        }
        return tagSet;
    }

    /**
     * Parses a {@code String name} into an {@code EventName}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static EventName parseEventNameWithWarning(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!EventName.isValidName(trimmedName)) {
            throw new ParseException(EventName.MESSAGE_CONSTRAINTS);
        }
        MessageCenter.appendEnd(EventName.getStyleWarningMessage(trimmedName));
        return new EventName(trimmedName);
    }

    /**
     * Parses a {@code String duration} into a {@code Duration}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code duration} is invalid.
     */
    public static Duration parseDuration(String duration) throws ParseException {
        requireNonNull(duration);
        String trimmedDuration = duration.trim();
        if (!Duration.isValidDates(trimmedDuration)) {
            throw new ParseException(Duration.MESSAGE_CONSTRAINTS);
        }
        if (!Duration.isValidDuration(trimmedDuration)) {
            throw new ParseException(Duration.MESSAGE_CONSTRAINTS_RANGE);
        }
        return new Duration(trimmedDuration);
    }

    /**
     * Parses a {@code String desc} into a {@code Description}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code desc} is invalid.
     */
    public static Description parseDescription(String desc) throws ParseException {
        requireNonNull(desc);
        String trimmedDesc = desc.trim();
        if (!Description.isValidDescription(trimmedDesc)) {
            throw new ParseException(Description.MESSAGE_CONSTRAINTS);
        }
        return new Description(trimmedDesc);
    }

    /**
     * Parses a {@code String indexes} into a {@code Set<Index>}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if any of the index is invalid.
     */
    public static List<Index> parseIndexes(String indexes) throws ParseException {
        requireNonNull(indexes);
        String trimmedIndexes = indexes.trim();
        List<Index> indexSet = new ArrayList<>();
        String[] indexList = indexes.split(" ");
        for (String index : indexList) {
            if (index.isEmpty()) {
                continue;
            }
            Index i = parseIndex(index);
            if (indexSet.contains(i)) {
                MessageCenter.appendEnd(
                        String.format("Warning: Duplicate index %d supplied, this is ignored.", i.getOneBased()));
            } else {
                indexSet.add(i);
            }
        }
        if (indexSet.isEmpty()) {
            throw new ParseException(MESSAGE_EMPTY_INDEX);
        }
        return indexSet;
    }
}
