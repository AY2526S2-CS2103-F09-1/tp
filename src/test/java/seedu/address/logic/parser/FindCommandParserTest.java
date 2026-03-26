package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.model.person.PersonContainsKeywordsPredicate.MatchMode;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_multipleNamePrefixes_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonContainsKeywordsPredicate(Arrays.asList("Alice", "Bob"),
                        Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), MatchMode.OR));
        assertParseSuccess(parser, " " + PREFIX_NAME + "Alice " + PREFIX_NAME + "Bob", expectedFindCommand);

        assertParseSuccess(parser, " " + PREFIX_NAME + "  Alice " + PREFIX_NAME + " \t Bob  \t",
                expectedFindCommand);
    }

    @Test
    public void parse_phonePrefix_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonContainsKeywordsPredicate(Collections.emptyList(), Collections.emptyList(),
                        Collections.singletonList("9435"), Collections.emptyList(), MatchMode.OR));
        assertParseSuccess(parser, " " + PREFIX_PHONE + "9435", expectedFindCommand);
    }

    @Test
    public void parse_namePrefix_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonContainsKeywordsPredicate(Arrays.asList("Alice"),
                        Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), MatchMode.OR));
        assertParseSuccess(parser, " " + PREFIX_NAME + "Alice", expectedFindCommand);
    }

    @Test
    public void parse_addressPrefix_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonContainsKeywordsPredicate(Collections.emptyList(), Collections.singletonList("Clementi"),
                        Collections.emptyList(), Collections.emptyList(), MatchMode.OR));
        assertParseSuccess(parser, " " + PREFIX_ADDRESS + "Clementi", expectedFindCommand);
    }

    @Test
    public void parse_tagPrefix_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonContainsKeywordsPredicate(Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.singletonList("Student"), MatchMode.OR));
        assertParseSuccess(parser, " " + PREFIX_TAG + "Student", expectedFindCommand);
    }

    @Test
    public void parse_nameAndAddressPrefix_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonContainsKeywordsPredicate(Collections.singletonList("Alice"),
                        Collections.singletonList("Clementi"), Collections.emptyList(),
                        Collections.emptyList(), MatchMode.OR));
        assertParseSuccess(parser, " " + PREFIX_NAME + "Alice " + PREFIX_ADDRESS + "Clementi",
                expectedFindCommand);
    }

    @Test
    public void parse_phoneAndAddressPrefix_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonContainsKeywordsPredicate(Collections.emptyList(),
                        Collections.singletonList("Clementi"), Collections.singletonList("9435"),
                        Collections.emptyList(), MatchMode.OR));
        assertParseSuccess(parser, " " + PREFIX_PHONE + "9435 " + PREFIX_ADDRESS + "Clementi",
                expectedFindCommand);
    }

    @Test
    public void parse_nameAndTagPrefix_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonContainsKeywordsPredicate(Collections.singletonList("Alice"), Collections.emptyList(),
                        Collections.emptyList(), Collections.singletonList("Student"), MatchMode.OR));
        assertParseSuccess(parser, " " + PREFIX_NAME + "Alice " + PREFIX_TAG + "Student",
                expectedFindCommand);
    }

    @Test
    public void parse_multipleTagPrefixes_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonContainsKeywordsPredicate(Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Arrays.asList("Student", "Leader"), MatchMode.OR));
        assertParseSuccess(parser, " " + PREFIX_TAG + "Student " + PREFIX_TAG + "Leader",
                expectedFindCommand);
    }

    @Test
    public void parse_allPrefixes_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonContainsKeywordsPredicate(Collections.singletonList("Alice"),
                        Collections.singletonList("Clementi"), Collections.singletonList("9435"),
                        Collections.singletonList("Student"), MatchMode.OR));
        assertParseSuccess(parser, " " + PREFIX_NAME + "Alice " + PREFIX_ADDRESS + "Clementi "
                + PREFIX_PHONE + "9435 " + PREFIX_TAG + "Student", expectedFindCommand);
    }

    @Test
    public void parse_preambleBeforePrefix_throwsParseException() {
        assertParseFailure(parser, "Alice " + PREFIX_PHONE + " 9435",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyNamePrefix_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_NAME,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyPhonePrefix_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_PHONE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyAddressPrefix_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_ADDRESS,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noPrefix_throwsParseException() {
        assertParseFailure(parser, "Alice Bob",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));

        assertParseFailure(parser, " \n Alice \n \t Bob  \t",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }
}
