package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIds.ID_FIRST;
import static seedu.address.testutil.TypicalIds.ID_SECOND;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteCommand;
import seedu.address.model.person.Id;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteCommandParserTest {

    private DeleteCommandParser parser = new DeleteCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteCommand() {
        ArrayList<Id> ids = new ArrayList<Id>();
        ids.add(ID_FIRST);
        assertParseSuccess(parser, "1", new DeleteCommand(ids));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        String expected = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "", expected);
        assertParseFailure(parser, " ", expected);
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", Id.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "-1", Id.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 a", Id.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "a 1", Id.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 -1 2", Id.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_manyArgs() {
        ArrayList<Id> ids = new ArrayList<Id>();
        ids.add(ID_FIRST);
        ids.add(ID_SECOND);
        assertParseSuccess(parser, "1 2", new DeleteCommand(ids));
    }
}
