package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_PARENT;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_STUDENT;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_TUTOR;
import static seedu.address.logic.commands.CommandTestUtil.UNSUPPORTED_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_PARENT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_STUDENT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_TUTOR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.model.person.Id;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser(Optional.<Id>empty());

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(BOB).withTags(VALID_TAG_TUTOR).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_TUTOR, new AddCommand(expectedPerson));

        // multiple tags - all accepted
        Person expectedPersonMultipleTags = new PersonBuilder(BOB)
                .withTags(VALID_TAG_STUDENT, VALID_TAG_PARENT).build();

        assertParseSuccess(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + ADDRESS_DESC_BOB + TAG_DESC_PARENT + TAG_DESC_STUDENT,
                new AddCommand(expectedPersonMultipleTags));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + PHONE_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_STUDENT;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple addresses
        assertParseFailure(parser, ADDRESS_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedPersonString + PHONE_DESC_AMY + NAME_DESC_AMY + ADDRESS_DESC_AMY
                        + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid phone
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // valid address, but multiple addresses
        // assertParseFailure(parser, VALID_ADDRESS_BOB + validExpectedPersonString,
        //         Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid phone
        assertParseFailure(parser, validExpectedPersonString + INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        // assertParseFailure(parser, validExpectedPersonString + INVALID_ADDRESS_DESC,
        //         Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // no optional fields
        Person expectedPersonNoOptionalFields = new PersonBuilder(AMY)
                .withoutPhone()
                .withAddress("")
                .withTags()
                .build();
        assertParseSuccess(parser, NAME_DESC_AMY,
                new AddCommand(expectedPersonNoOptionalFields));

        // zero tags
        Person expectedPersonNoTagsOnly = new PersonBuilder(AMY).withTags().build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + ADDRESS_DESC_AMY,
                new AddCommand(expectedPersonNoTagsOnly));

        // no phone number prefix and no phone number
        Person expectedPersonNoPhoneOnly = new PersonBuilder(AMY).withoutPhone().build();
        assertParseSuccess(parser, NAME_DESC_AMY + ADDRESS_DESC_AMY + TAG_DESC_STUDENT,
                new AddCommand(expectedPersonNoPhoneOnly));

        // there is a phone number prefix, but no phone number - address and tags are present
        assertParseSuccess(parser, NAME_DESC_AMY + " " + PREFIX_PHONE + ADDRESS_DESC_AMY + TAG_DESC_STUDENT,
                new AddCommand(expectedPersonNoPhoneOnly));

        // there is a phone number prefix, but no phone number - no address or tags present
        assertParseSuccess(parser, NAME_DESC_AMY + " " + PREFIX_PHONE,
                new AddCommand(expectedPersonNoOptionalFields));

        // no address
        Person expectedPersonNoAddressOnly = new PersonBuilder(AMY)
                .withAddress("")
                .build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + TAG_DESC_STUDENT,
                new AddCommand(expectedPersonNoAddressOnly));

        // there is an address prefix, but no address - phone numbers and tags present
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + " "
                        + PREFIX_ADDRESS + TAG_DESC_STUDENT,
                new AddCommand(expectedPersonNoAddressOnly));

        // there is an address prefix, but no address - no phone numbers or tags present
        assertParseSuccess(parser, NAME_DESC_AMY + " " + PREFIX_ADDRESS,
                new AddCommand(expectedPersonNoOptionalFields));
    }

    @Test
    public void parse_withCurrentMaxId_assignsNextId() {
        AddCommandParser parserWithCurrentMaxId = new AddCommandParser(Optional.of(Id.of(5)));
        Person expectedPerson = new PersonBuilder()
                .withId(6)
                .withName("Amy Bee")
                .withoutPhone()
                .withAddress("")
                .build();

        assertParseSuccess(parserWithCurrentMaxId, NAME_DESC_AMY,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing phone prefix
        // assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + ADDRESS_DESC_BOB,
        //                 expectedMessage);

        // missing address prefix
        // assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_ADDRESS_BOB,
        //         expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_ADDRESS_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + ADDRESS_DESC_BOB
                + TAG_DESC_PARENT + TAG_DESC_STUDENT, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + ADDRESS_DESC_BOB
                + TAG_DESC_PARENT + TAG_DESC_STUDENT, Phone.MESSAGE_CONSTRAINTS);

        // invalid tag format
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + ADDRESS_DESC_BOB
                + INVALID_TAG_DESC + TAG_DESC_STUDENT, Tag.MESSAGE_CONSTRAINTS);

        // unsupported category
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + ADDRESS_DESC_BOB
                + UNSUPPORTED_TAG_DESC + TAG_DESC_STUDENT, Tag.MESSAGE_TAG_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        // assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + INVALID_ADDRESS_DESC,
        //         Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_PARENT + TAG_DESC_STUDENT,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
