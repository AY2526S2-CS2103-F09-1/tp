package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.TagCommand;
import seedu.address.model.tag.Tag;

public class TagCommandParserTest {

    private final TagCommandParser parser = new TagCommandParser();

    @Test
    public void parse_validArgs_returnsTagCommand() {
        assertParseSuccess(parser, " 1 t/student",
                new TagCommand(INDEX_FIRST_PERSON, new Tag("Student")));
    }

    @Test
    public void parse_validArgsMixedCase_returnsNormalizedTagCommand() {
        assertParseSuccess(parser, " 1 t/pArEnT",
                new TagCommand(INDEX_FIRST_PERSON, new Tag("Parent")));
    }

    @Test
    public void parse_missingIndex_throwsParseException() {
        assertParseFailure(parser, " t/Student",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingCategoryPrefix_throwsParseException() {
        assertParseFailure(parser, " 1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonEmptyPreamble_throwsParseException() {
        assertParseFailure(parser, " preamble t/Student", ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_duplicateCategoryPrefix_throwsParseException() {
        assertParseFailure(parser, " 1 t/Student t/Tutor",
                getErrorMessageForDuplicatePrefixes(PREFIX_TAG));
    }

    @Test
    public void parse_invalidCategory_throwsParseException() {
        assertParseFailure(parser, " 1 t/Friend", Tag.MESSAGE_CATEGORY_CONSTRAINTS);
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, " zero t/Student", ParserUtil.MESSAGE_INVALID_INDEX);
    }
}
