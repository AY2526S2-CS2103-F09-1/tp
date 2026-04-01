package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_MODE;
import static seedu.address.logic.Messages.MESSAGE_MORE_THAN_ONE_MODE;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.REMARK_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_PARENT;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_STUDENT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARK_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_PARENT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_STUDENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
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
                new PersonContainsKeywordsPredicate(Arrays.asList(VALID_NAME_AMY, VALID_NAME_BOB),
                        Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                                Collections.emptyList(), MatchMode.OR));
        assertParseSuccess(parser, NAME_DESC_AMY + NAME_DESC_BOB, expectedFindCommand);

        assertParseSuccess(parser, " " + PREFIX_NAME + "  " + VALID_NAME_AMY + NAME_DESC_BOB + "  \t",
                expectedFindCommand);
    }

    @Test
    public void parse_phonePrefix_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonContainsKeywordsPredicate(Collections.emptyList(), Collections.emptyList(),
                        Collections.singletonList(VALID_PHONE_AMY), Collections.emptyList(),
                                Collections.emptyList(), MatchMode.OR));
        assertParseSuccess(parser, PHONE_DESC_AMY, expectedFindCommand);
    }

    @Test
    public void parse_namePrefix_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonContainsKeywordsPredicate(Arrays.asList(VALID_NAME_AMY),
                        Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                                Collections.emptyList(), MatchMode.OR));
        assertParseSuccess(parser, NAME_DESC_AMY, expectedFindCommand);
    }

    @Test
    public void parse_addressPrefix_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonContainsKeywordsPredicate(Collections.emptyList(),
                        Collections.singletonList(VALID_ADDRESS_AMY),
                                Collections.emptyList(), Collections.emptyList(),
                                        Collections.emptyList(), MatchMode.OR));
        assertParseSuccess(parser, ADDRESS_DESC_AMY, expectedFindCommand);
    }

    @Test
    public void parse_tagPrefix_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonContainsKeywordsPredicate(Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Collections.singletonList(VALID_TAG_STUDENT),
                                Collections.emptyList(), MatchMode.OR));
        assertParseSuccess(parser, TAG_DESC_STUDENT, expectedFindCommand);
    }

    @Test
    public void parse_nameAndAddressPrefix_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonContainsKeywordsPredicate(Collections.singletonList(VALID_NAME_AMY),
                        Collections.singletonList(VALID_ADDRESS_AMY), Collections.emptyList(),
                        Collections.emptyList(), Collections.emptyList(), MatchMode.OR));
        assertParseSuccess(parser, NAME_DESC_AMY + ADDRESS_DESC_AMY,
                expectedFindCommand);
    }

    @Test
    public void parse_phoneAndAddressPrefix_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonContainsKeywordsPredicate(Collections.emptyList(),
                        Collections.singletonList(VALID_ADDRESS_AMY), Collections.singletonList(VALID_PHONE_AMY),
                        Collections.emptyList(), Collections.emptyList(), MatchMode.OR));
        assertParseSuccess(parser, PHONE_DESC_AMY + ADDRESS_DESC_AMY,
                expectedFindCommand);
    }

    @Test
    public void parse_nameAndTagPrefix_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonContainsKeywordsPredicate(Collections.singletonList(VALID_NAME_AMY), Collections.emptyList(),
                        Collections.emptyList(), Collections.singletonList(VALID_TAG_STUDENT),
                                Collections.emptyList(), MatchMode.OR));
        assertParseSuccess(parser, NAME_DESC_AMY + TAG_DESC_STUDENT,
                expectedFindCommand);
    }

    @Test
    public void parse_multipleTagPrefixes_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonContainsKeywordsPredicate(Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList(), Arrays.asList(VALID_TAG_STUDENT, VALID_TAG_PARENT),
                        Collections.emptyList(), MatchMode.OR));
        assertParseSuccess(parser, TAG_DESC_STUDENT + TAG_DESC_PARENT,
                expectedFindCommand);
    }

    @Test
    public void parse_allPrefixes_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonContainsKeywordsPredicate(Collections.singletonList(VALID_NAME_AMY),
                        Collections.singletonList(VALID_ADDRESS_AMY), Collections.singletonList(VALID_PHONE_AMY),
                                Collections.singletonList(VALID_TAG_STUDENT), Collections.emptyList(), MatchMode.OR));
        assertParseSuccess(parser, NAME_DESC_AMY + ADDRESS_DESC_AMY + PHONE_DESC_AMY + TAG_DESC_STUDENT,
                expectedFindCommand);
    }

    @Test
    public void parse_andModeWithMultipleNamePrefixes_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonContainsKeywordsPredicate(Arrays.asList(VALID_NAME_AMY, VALID_NAME_BOB),
                        Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), MatchMode.AND));
        assertParseSuccess(parser, " " + PREFIX_MODE + "and" + NAME_DESC_AMY + NAME_DESC_BOB, expectedFindCommand);
    }

    @Test
    public void parse_andModeWithMultipleEnabledFields_returnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(
                new PersonContainsKeywordsPredicate(Collections.singletonList(VALID_NAME_AMY),
                        Collections.singletonList(VALID_ADDRESS_AMY), Collections.singletonList(VALID_PHONE_AMY),
                        Collections.singletonList(VALID_TAG_STUDENT), MatchMode.AND));
        assertParseSuccess(parser,
                " " + PREFIX_MODE + "and" + NAME_DESC_AMY + ADDRESS_DESC_AMY + PHONE_DESC_AMY + TAG_DESC_STUDENT,
                expectedFindCommand);
    }

    @Test
    public void parse_modeVariants_returnsFindCommand() {
        // Blank mode falls back to the default OR behavior, while explicit OR and AND are accepted.
        FindCommand expectedOrFindCommand = new FindCommand(
                new PersonContainsKeywordsPredicate(Collections.singletonList(VALID_NAME_AMY),
                        Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), MatchMode.OR));
        FindCommand expectedAndFindCommand = new FindCommand(
                new PersonContainsKeywordsPredicate(Collections.singletonList(VALID_NAME_AMY),
                        Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), MatchMode.AND));

        assertParseSuccess(parser, " " + PREFIX_MODE + NAME_DESC_AMY, expectedOrFindCommand);
        assertParseSuccess(parser, " " + PREFIX_MODE + "  oR " + NAME_DESC_AMY, expectedOrFindCommand);
        assertParseSuccess(parser, " " + PREFIX_MODE + "   aNd " + NAME_DESC_AMY, expectedAndFindCommand);
    }

    @Test
    public void parse_preambleBeforePrefix_throwsParseException() {
        assertParseFailure(parser, VALID_NAME_AMY + PHONE_DESC_AMY,
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
    public void parse_invalidMode_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_MODE + "xor" + NAME_DESC_AMY,
                String.format(MESSAGE_INVALID_MODE, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_multipleModes_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_MODE + "and " + PREFIX_MODE + "or" + NAME_DESC_AMY,
                String.format(MESSAGE_MORE_THAN_ONE_MODE, FindCommand.MESSAGE_USAGE));

        assertParseFailure(parser, " " + PREFIX_MODE + "and " + PREFIX_MODE + NAME_DESC_AMY,
                String.format(MESSAGE_MORE_THAN_ONE_MODE, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyRemarkPrefix_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_REMARK,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validRemarkPrefix_returnsFindCommand() {
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.singletonList(VALID_REMARK_AMY),
                MatchMode.OR
        );
        FindCommand expectedFindCommand = new FindCommand(predicate);
        assertParseSuccess(parser, REMARK_DESC_AMY,
                expectedFindCommand);
    }

    @Test
    public void parse_remarkAndPhonePrefix_returnsFindCommand() {
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.singletonList(VALID_PHONE_AMY),
                Collections.emptyList(),
                Collections.singletonList(VALID_REMARK_AMY),
                MatchMode.OR
        );
        FindCommand expectedFindCommand = new FindCommand(predicate);
        assertParseSuccess(parser, REMARK_DESC_AMY + PHONE_DESC_AMY,
                expectedFindCommand);
    }

    @Test
    public void parse_allPrefixesWithBlankValues_throwsParseException() {
        String args = " " + PREFIX_NAME + "   "
                + PREFIX_ADDRESS + "   "
                + PREFIX_PHONE + "   "
                + PREFIX_TAG + "   "
                + PREFIX_REMARK + "   ";
        assertParseFailure(parser, args,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noPrefix_throwsParseException() {
        assertParseFailure(parser, VALID_NAME_AMY + " " + VALID_NAME_BOB,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));

        assertParseFailure(parser, " \n Alice \n \t Bob  \t",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }
}
