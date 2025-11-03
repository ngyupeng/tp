package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMERGENCY_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMERGENCY_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ENROLL_YEAR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PIN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyContact;
import seedu.address.model.person.EnrollmentYear;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Pin;
import seedu.address.model.role.Role;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    public static final String ERROR_MESSAGE_MISSING_COMPULSORY_PREFIX = "Missing compulsory prefixes: %s. ";
    public static final String ERROR_MESSAGE_MISSING_FIRST_PREFIX = "There is an input without prefix after 'add'. ";

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    private static String checkMissingPrefix(ArgumentMultimap argMultimap) {
        Prefix[] compulsoryPrefix = {PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_EMAIL};
        String missingPrefix = Arrays.stream(compulsoryPrefix)
                .filter(p -> !arePrefixesPresent(argMultimap, p))
                .map(Prefix::toString)
                .collect(Collectors.joining(", "));
        if (!missingPrefix.isEmpty()) {
            return String.format(ERROR_MESSAGE_MISSING_COMPULSORY_PREFIX, missingPrefix);
        }
        return "";
    }

    private static String checkUntokenInput(ArgumentMultimap argMultimap) {
        if (haveUntokenInput(argMultimap)) {
            return ERROR_MESSAGE_MISSING_FIRST_PREFIX;
        }
        return "";
    }

    private static boolean isMissingCompulsoryPrefixes(ArgumentMultimap argMultimap) {
        return !arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_EMAIL);
    }

    private static boolean haveUntokenInput(ArgumentMultimap argMultimap) {
        return !argMultimap.getPreamble().isEmpty();
    }

    /**
     * Returns error message when either first input after command word does not have prefix,
     * of if we are missing compulsory prefixes.
     *
     * @param argMultimap Contains mapping of prefixes to input.
     * @return Error message providing detailed description on what is missing.
     */
    private static String createErrorMessageForMissingPrefix(ArgumentMultimap argMultimap) {
        String compilationOfErrorMessage = "Note: ";
        compilationOfErrorMessage += AddCommandParser.checkMissingPrefix(argMultimap);
        compilationOfErrorMessage += AddCommandParser.checkUntokenInput(argMultimap);
        compilationOfErrorMessage += String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);
        return compilationOfErrorMessage;
    }

    private static ArgumentMultimap createArgumentMultimap(String args) {
        return ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                PREFIX_ADDRESS, PREFIX_PIN, PREFIX_ROLE, PREFIX_EMERGENCY_NAME, PREFIX_EMERGENCY_PHONE,
                PREFIX_TAG, PREFIX_ENROLL_YEAR);
    }

    private static void verifyDuplicate(ArgumentMultimap argMultimap) throws ParseException {
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_PIN,
                PREFIX_EMERGENCY_NAME, PREFIX_EMERGENCY_PHONE, PREFIX_ENROLL_YEAR);
    }


    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = createArgumentMultimap(args);
        String compilationOfErrorMessage = createErrorMessageForMissingPrefix(argMultimap);

        verifyDuplicate(argMultimap);
        if (isMissingCompulsoryPrefixes(argMultimap) || haveUntokenInput(argMultimap)) {
            throw new ParseException(compilationOfErrorMessage);
        }

        Name name = ParserUtil.parseNameWithWarning(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Address address = ParserUtil.parseAddressWithWarning(argMultimap.getValue(PREFIX_ADDRESS).get());
        Pin pin = ParserUtil.parsePin(argMultimap.getValue(PREFIX_PIN).isPresent()
                ? argMultimap.getValue(PREFIX_PIN).get()
                : "false");
        EmergencyContact emergencyContact =
                ParserUtil.parseEmergencyContact(argMultimap.getValue(PREFIX_EMERGENCY_NAME).orElse(null),
                        argMultimap.getValue(PREFIX_EMERGENCY_PHONE).orElse(null));
        EnrollmentYear enrollmentYear = ParserUtil.parseEnrollmentYear(
                argMultimap.getValue(PREFIX_ENROLL_YEAR).orElse(""));
        Set<Role> roleList = ParserUtil.parseRoles(argMultimap.getAllValues(PREFIX_ROLE));
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        try {
            Person person = new Person(name, phone, email, address, pin, roleList, tagList, emergencyContact,
                    enrollmentYear);
            return new AddCommand(person);
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage());
        }
    }

}
