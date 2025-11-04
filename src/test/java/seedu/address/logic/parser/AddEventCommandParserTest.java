package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DESC_DESC_MEETING;
import static seedu.address.logic.commands.CommandTestUtil.DURATION_DESC_MEETING;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DURATION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EVENT_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_MEETING;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DURATION_MEETING;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_MEETING;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalEvents.MEETING;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddEventCommand;
import seedu.address.model.event.Event;
import seedu.address.testutil.EventBuilder;

public class AddEventCommandParserTest {
    private AddEventCommandParser parser = new AddEventCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Event expectedEvent = new EventBuilder(MEETING).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_MEETING + DURATION_DESC_MEETING
                + DESC_DESC_MEETING, new AddEventCommand(expectedEvent));
    }

    @Test
    public void parse_repeatedValue_failure() {
        String validExpectedEventString = NAME_DESC_MEETING + DURATION_DESC_MEETING
                + DESC_DESC_MEETING;

        // multiple names
        assertParseFailure(parser, NAME_DESC_MEETING + validExpectedEventString,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));

        // multiple duration
        assertParseFailure(parser, DURATION_DESC_MEETING + validExpectedEventString,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));

        // multiple description
        assertParseFailure(parser, DESC_DESC_MEETING + validExpectedEventString,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_EVENT_NAME_DESC + validExpectedEventString,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));

        // invalid duration
        assertParseFailure(parser, INVALID_DURATION_DESC + validExpectedEventString,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));

        // valid value followed by invalid value
        // invalid name
        assertParseFailure(parser, validExpectedEventString + INVALID_EVENT_NAME_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));

        // invalid duration
        assertParseFailure(parser, validExpectedEventString + INVALID_DURATION_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Event expectedEvent = new EventBuilder(MEETING).withDescription("").build();
        assertParseSuccess(parser, NAME_DESC_MEETING + DURATION_DESC_MEETING,
                new AddEventCommand(expectedEvent));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_MEETING + DURATION_DESC_MEETING + DESC_DESC_MEETING,
                expectedMessage);

        // missing duration prefix
        assertParseFailure(parser, NAME_DESC_MEETING + VALID_DURATION_MEETING + DESC_DESC_MEETING,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_EVENT_NAME_DESC + DURATION_DESC_MEETING + DESC_DESC_MEETING,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));

        // invalid duration
        assertParseFailure(parser, NAME_DESC_MEETING + INVALID_DURATION_DESC + DESC_DESC_MEETING,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_MEETING + DURATION_DESC_MEETING
                + DESC_DESC_MEETING,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));
    }
}
