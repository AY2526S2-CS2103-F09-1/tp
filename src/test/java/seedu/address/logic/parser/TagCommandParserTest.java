package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
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
        assertParseSuccess(parser, " /index 1 /tag student",
                new TagCommand(INDEX_FIRST_PERSON, new Tag("Student")));
    }

    @Test
    public void parse_validArgsMixedCase_returnsNormalizedTagCommand() {
        assertParseSuccess(parser, " /index 1 /tag pArEnT",
                new TagCommand(INDEX_FIRST_PERSON, new Tag("Parent")));
    }

    @Test
    public void parse_missingIndexPrefix_throwsParseException() {
        assertParseFailure(parser, " 1 /tag Student",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicatePrefixes_throwsParseException() {
        assertParseFailure(parser, " /index 1 /index 2 /tag Student",
                getErrorMessageForDuplicatePrefixes(PREFIX_INDEX));

        assertParseFailure(parser, " /index 1 /tag Student /tag Tutor",
                getErrorMessageForDuplicatePrefixes(PREFIX_CATEGORY_TAG));
    }

    @Test
    public void parse_invalidCategory_throwsParseException() {
        assertParseFailure(parser, " /index 1 /tag Friend", TagCommandParser.MESSAGE_INVALID_CATEGORY);
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, " /index zero /tag Student", ParserUtil.MESSAGE_INVALID_INDEX);
    }
}
