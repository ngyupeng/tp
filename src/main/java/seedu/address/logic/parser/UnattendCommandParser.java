package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERSON;
import static seedu.address.logic.parser.ParserUtil.parseIndex;
import static seedu.address.logic.parser.ParserUtil.parseIndexes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnattendCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnattendCommand object.
 */
public class UnattendCommandParser {
    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Parses the given {@code String} of arguments in the context of the AttendCommand
     * and returns an AttendCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public UnattendCommand parse(String args) throws ParseException {
        try {
            ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PERSON, PREFIX_EVENT);

            List<String> prefixes = new ArrayList<>();
            if (!arePrefixesPresent(argMultimap, PREFIX_PERSON)) {
                prefixes.add(PREFIX_PERSON.getPrefix());
            }
            if (!arePrefixesPresent(argMultimap, PREFIX_EVENT)) {
                prefixes.add(PREFIX_EVENT.getPrefix());
            }
            if (!prefixes.isEmpty()) {
                throw new ParseException(
                        String.format(AddCommandParser.ERROR_MESSAGE_MISSING_COMPULSORY_PREFIX,
                                String.join(", ", prefixes)));
            }

            argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_PERSON, PREFIX_EVENT);

            Index eventIndex = parseIndex(argMultimap.getValue(PREFIX_EVENT).get());
            List<Index> personIndexes = parseIndexes(argMultimap.getValue(PREFIX_PERSON).get());
            return new UnattendCommand(eventIndex, personIndexes);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnattendCommand.MESSAGE_USAGE), pe);
        }
    }
}
