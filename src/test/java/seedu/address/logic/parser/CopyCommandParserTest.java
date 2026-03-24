package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIds.ID_FIRST;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CopyCommand;
import seedu.address.model.person.Id;

public class CopyCommandParserTest {

    private CopyCommandParser parser = new CopyCommandParser();

    @Test
    public void parse_validArgs_returnsCopyCommand() {
        assertParseSuccess(parser, "1 " + CopyCommand.FIELD_PHONE, new CopyCommand(ID_FIRST, CopyCommand.FIELD_PHONE));
        assertParseSuccess(parser, "1 " + CopyCommand.FIELD_NAME, new CopyCommand(ID_FIRST, CopyCommand.FIELD_NAME));
        assertParseSuccess(parser, "1 " + CopyCommand.FIELD_ADDRESS,
                new CopyCommand(ID_FIRST, CopyCommand.FIELD_ADDRESS));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopyCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingField_throwsParseException() {
        assertParseFailure(parser, "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_tooManyArgs_throwsParseException() {
        assertParseFailure(parser, "1 " + CopyCommand.FIELD_PHONE + " extra",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidId_throwsParseException() {
        assertParseFailure(parser, "abc " + CopyCommand.FIELD_PHONE, Id.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "-1 " + CopyCommand.FIELD_PHONE, Id.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "0 " + CopyCommand.FIELD_PHONE, Id.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidField_throwsParseException() {
        assertParseFailure(parser, "1 email", CopyCommand.MESSAGE_INVALID_FIELD);
        assertParseFailure(parser, "1 xyz", CopyCommand.MESSAGE_INVALID_FIELD);
    }
}
