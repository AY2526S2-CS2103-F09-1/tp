package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_LINK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
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
        assertParseSuccess(parser, "1 " + PREFIX_PHONE, new CopyCommand(ID_FIRST, PREFIX_PHONE.getPrefix()));
        assertParseSuccess(parser, "1 " + PREFIX_NAME, new CopyCommand(ID_FIRST, PREFIX_NAME.getPrefix()));
        assertParseSuccess(parser, "1 " + PREFIX_ADDRESS,
                new CopyCommand(ID_FIRST, PREFIX_ADDRESS.getPrefix()));
        assertParseSuccess(parser, "1 " + PREFIX_MEETING_LINK,
                new CopyCommand(ID_FIRST, PREFIX_MEETING_LINK.getPrefix()));
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
        assertParseFailure(parser, "1 " + PREFIX_PHONE + " extra",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidId_throwsParseException() {
        assertParseFailure(parser, "abc " + PREFIX_PHONE, Id.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "-1 " + PREFIX_PHONE, Id.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "0 " + PREFIX_PHONE, Id.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidField_throwsParseException() {
        assertParseFailure(parser, "1 email", CopyCommand.MESSAGE_INVALID_FIELD);
        assertParseFailure(parser, "1 xyz", CopyCommand.MESSAGE_INVALID_FIELD);
        assertParseFailure(parser, "1 r/", CopyCommand.MESSAGE_INVALID_FIELD);
        assertParseFailure(parser, "1 t/", CopyCommand.MESSAGE_INVALID_FIELD);
        assertParseFailure(parser, "1 d/", CopyCommand.MESSAGE_INVALID_FIELD);
    }

    @Test
    public void parse_validLargeId_returnsCopyCommand() {
        Id largeId = Id.of(6776);
        assertParseSuccess(parser, "6776 " + PREFIX_PHONE, new CopyCommand(largeId, PREFIX_PHONE.getPrefix()));
    }

    @Test
    public void parse_idWithLeadingZeros_treatedAsValidId() {
        // "01" is parsed as integer 1 — leading zeros are silently accepted
        assertParseSuccess(parser, "01 " + PREFIX_PHONE, new CopyCommand(ID_FIRST, PREFIX_PHONE.getPrefix()));
    }

    @Test
    public void parse_fieldOnly_throwsParseException() {
        // field given but no ID
        assertParseFailure(parser, PREFIX_PHONE.getPrefix(),
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_whitespaceOnly_throwsParseException() {
        assertParseFailure(parser, "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopyCommand.MESSAGE_USAGE));
    }
}
