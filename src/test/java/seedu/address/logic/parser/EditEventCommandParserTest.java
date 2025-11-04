package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DESC_DESC_MEETING;
import static seedu.address.logic.commands.CommandTestUtil.DESC_DESC_TRAINING;
import static seedu.address.logic.commands.CommandTestUtil.DURATION_DESC_MEETING;
import static seedu.address.logic.commands.CommandTestUtil.DURATION_DESC_TRAINING;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DURATION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EVENT_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_MEETING;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_TRAINING;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_MEETING;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_TRAINING;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DURATION_MEETING;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DURATION_TRAINING;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_MEETING;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_TRAINING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EVENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_EVENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_EVENT;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditEventCommand;
import seedu.address.logic.commands.EditEventCommand.EditEventDescriptor;
import seedu.address.testutil.EditEventDescriptorBuilder;

public class EditEventCommandParserTest {

    private static final String DESCRIPTION_EMPTY = " " + PREFIX_DESCRIPTION;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditEventCommand.MESSAGE_USAGE);

    private EditEventCommandParser parser = new EditEventCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_MEETING, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_MEETING, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_MEETING, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_EVENT_NAME_DESC, MESSAGE_INVALID_FORMAT); // invalid event name
        assertParseFailure(parser, "1" + INVALID_DURATION_DESC, MESSAGE_INVALID_FORMAT); // invalid duration

        // invalid name followed by valid duration
        assertParseFailure(parser, "1" + INVALID_EVENT_NAME_DESC + DURATION_DESC_MEETING,
                MESSAGE_INVALID_FORMAT);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_EVENT_NAME_DESC + INVALID_DURATION_DESC,
                MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_EVENT;
        String userInput = targetIndex.getOneBased() + NAME_DESC_TRAINING + DURATION_DESC_TRAINING + DESC_DESC_TRAINING;

        EditEventDescriptor descriptor = new EditEventDescriptorBuilder().withName(VALID_NAME_TRAINING)
                .withDuration(VALID_DURATION_TRAINING).withDescription(VALID_DESCRIPTION_TRAINING).build();
        EditEventCommand expectedCommand = new EditEventCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_EVENT;
        String userInput = targetIndex.getOneBased() + NAME_DESC_MEETING;

        EditEventDescriptor descriptor = new EditEventDescriptorBuilder().withName(VALID_NAME_MEETING).build();
        EditEventCommand expectedCommand = new EditEventCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_EVENT;
        String userInput = targetIndex.getOneBased() + NAME_DESC_MEETING;
        EditEventDescriptor descriptor = new EditEventDescriptorBuilder().withName(VALID_NAME_MEETING).build();
        EditEventCommand expectedCommand = new EditEventCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // duration
        userInput = targetIndex.getOneBased() + DURATION_DESC_MEETING;
        descriptor = new EditEventDescriptorBuilder().withDuration(VALID_DURATION_MEETING).build();
        expectedCommand = new EditEventCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // description
        userInput = targetIndex.getOneBased() + DESC_DESC_MEETING;
        descriptor = new EditEventDescriptorBuilder().withDescription(VALID_DESCRIPTION_MEETING).build();
        expectedCommand = new EditEventCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // valid followed by invalid
        Index targetIndex = INDEX_FIRST_EVENT;
        String userInput = targetIndex.getOneBased() + INVALID_DURATION_DESC + DURATION_DESC_TRAINING;

        assertParseFailure(parser, userInput, MESSAGE_INVALID_FORMAT);

        // invalid followed by valid
        userInput = targetIndex.getOneBased() + DURATION_DESC_TRAINING + INVALID_DURATION_DESC;

        assertParseFailure(parser, userInput, MESSAGE_INVALID_FORMAT);

        // multiple valid fields repeated
        userInput = targetIndex.getOneBased() + NAME_DESC_MEETING + NAME_DESC_TRAINING
                + DURATION_DESC_MEETING + DURATION_DESC_TRAINING + DESC_DESC_MEETING + DESC_DESC_TRAINING;

        assertParseFailure(parser, userInput,
                MESSAGE_INVALID_FORMAT);

        // multiple invalid values
        userInput = targetIndex.getOneBased() + INVALID_DURATION_DESC + INVALID_EVENT_NAME_DESC
                + INVALID_DURATION_DESC + INVALID_EVENT_NAME_DESC;

        assertParseFailure(parser, userInput,
                MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_resetDescription_success() {
        Index targetIndex = INDEX_THIRD_EVENT;
        String userInput = targetIndex.getOneBased() + DESCRIPTION_EMPTY;

        EditEventDescriptor descriptor = new EditEventDescriptorBuilder().withDescription("").build();
        EditEventCommand expectedCommand = new EditEventCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
