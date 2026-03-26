package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIds.ID_FIRST;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.TagCommand;
import seedu.address.model.person.Id;
import seedu.address.model.tag.Tag;

public class TagCommandParserTest {

    private final TagCommandParser parser = new TagCommandParser();

    @Test
    public void parse_validArgs_returnsTagCommand() {
        assertParseSuccess(parser, " 1 " + PREFIX_TAG + "student",
                new TagCommand(ID_FIRST, new Tag("Student")));
    }

    @Test
    public void parse_validArgsMixedCase_returnsNormalizedTagCommand() {
        assertParseSuccess(parser, " 1 " + PREFIX_TAG + "pArEnT",
                new TagCommand(ID_FIRST, new Tag("Parent")));
    }

    @Test
    public void parse_missingIndex_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_TAG + "Student",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingCategoryPrefix_throwsParseException() {
        assertParseFailure(parser, " 1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonEmptyPreamble_throwsParseException() {
        assertParseFailure(parser, " preamble " + PREFIX_TAG + "Student",
                Id.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_duplicateCategoryPrefix_throwsParseException() {
        assertParseFailure(parser, " 1 " + PREFIX_TAG + "Student " + PREFIX_TAG + "Tutor",
                getErrorMessageForDuplicatePrefixes(PREFIX_TAG));
    }

    @Test
    public void parse_invalidCategory_throwsParseException() {
        assertParseFailure(parser, " 1 " + PREFIX_TAG + "Friend",
                Tag.MESSAGE_TAG_CONSTRAINTS);
    }

    @Test
    public void parse_invalidId_throwsParseException() {
        assertParseFailure(parser, " zero " + PREFIX_TAG + "Student",
                Id.MESSAGE_CONSTRAINTS);
    }
}
