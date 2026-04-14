package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_MEETING_LINK_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_REMARK_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TIME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TIME_DURATION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TIME_NO_DAY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TIME_ZERO_DURATION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_UNLISTED_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.MEETING_LINK_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.MEETING_LINK_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.REMARK_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.REMARK_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_PARENT;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_STUDENT;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_TUTOR;
import static seedu.address.logic.commands.CommandTestUtil.TIME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TIME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MEETING_LINK_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARK_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_PARENT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_STUDENT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_TUTOR;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TIME_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_LINK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.model.person.Id;
import seedu.address.model.person.MeetingLink;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Time;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser(Optional.<Id>empty());

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(BOB).withTags(VALID_TAG_TUTOR).build();

        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB
                + ADDRESS_DESC_BOB + TIME_DESC_BOB + TAG_DESC_TUTOR + REMARK_DESC_BOB + MEETING_LINK_DESC_BOB,
                new AddCommand(expectedPerson));

        Person expectedPersonMultipleTags = new PersonBuilder(BOB)
                .withTags(VALID_TAG_STUDENT, VALID_TAG_PARENT).build();

        assertParseSuccess(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + ADDRESS_DESC_BOB + TIME_DESC_BOB + TAG_DESC_PARENT
                        + TAG_DESC_STUDENT + REMARK_DESC_BOB + MEETING_LINK_DESC_BOB,
                new AddCommand(expectedPersonMultipleTags));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + PHONE_DESC_BOB
                + ADDRESS_DESC_BOB + TIME_DESC_BOB
                + TAG_DESC_STUDENT + REMARK_DESC_BOB
                + MEETING_LINK_DESC_BOB;

        // one repetition, for each non-tag field (valid field inputs)
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));
        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));
        assertParseFailure(parser, ADDRESS_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));
        assertParseFailure(parser, TIME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TIME));
        assertParseFailure(parser, REMARK_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_REMARK));
        assertParseFailure(parser, MEETING_LINK_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_MEETING_LINK));

        // multiple non-tag fields are being repeated
        assertParseFailure(parser, validExpectedPersonString + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_PHONE,
                        PREFIX_ADDRESS, PREFIX_TIME,
                        PREFIX_REMARK, PREFIX_MEETING_LINK));

        // one repetition, for each non-tag field (invalid field inputs placed at the front)
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));
        assertParseFailure(parser, INVALID_ADDRESS_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));
        assertParseFailure(parser, INVALID_REMARK_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_REMARK));
        assertParseFailure(parser, INVALID_TIME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TIME));
        assertParseFailure(parser, INVALID_MEETING_LINK_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_MEETING_LINK));

        // one repetition, for each non-tag field (invalid field inputs placed at the back)
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));
        assertParseFailure(parser, validExpectedPersonString + INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));
        assertParseFailure(parser, validExpectedPersonString + INVALID_ADDRESS_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));
        assertParseFailure(parser, validExpectedPersonString + INVALID_REMARK_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_REMARK));
        assertParseFailure(parser, validExpectedPersonString + INVALID_TIME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TIME));
        assertParseFailure(parser, validExpectedPersonString + INVALID_MEETING_LINK_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_MEETING_LINK));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        Person expectedPersonNoOptionalFields = new PersonBuilder(AMY)
                .withoutPhone()
                .withoutAddress()
                .withoutTime()
                .withTags()
                .withoutRemark()
                .withoutMeetingLink()
                .build();
        Person expectedPersonNoTagsOnly = new PersonBuilder(AMY).withTags().build();
        Person expectedPersonNoPhoneOnly = new PersonBuilder(AMY).withoutPhone().build();
        Person expectedPersonNoAddressOnly = new PersonBuilder(AMY).withoutAddress().build();
        Person expectedPersonNoRemarkOnly = new PersonBuilder(AMY).withoutRemark().build();
        Person expectedPersonNoTimeOnly = new PersonBuilder(AMY).withoutTime().build();
        Person expectedPersonNoMeetingLinkOnly = new PersonBuilder(AMY).withoutMeetingLink().build();

        // omit the fields entirely
        assertParseSuccess(parser, NAME_DESC_AMY,
                new AddCommand(expectedPersonNoOptionalFields));
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY
                        + ADDRESS_DESC_AMY + TIME_DESC_AMY
                        + REMARK_DESC_AMY + MEETING_LINK_DESC_AMY,
                new AddCommand(expectedPersonNoTagsOnly));
        assertParseSuccess(parser, NAME_DESC_AMY + ADDRESS_DESC_AMY
                        + TIME_DESC_AMY + REMARK_DESC_AMY
                        + TAG_DESC_STUDENT + MEETING_LINK_DESC_AMY,
                new AddCommand(expectedPersonNoPhoneOnly));
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY
                        + TIME_DESC_AMY + TAG_DESC_STUDENT
                        + REMARK_DESC_AMY + MEETING_LINK_DESC_AMY,
                new AddCommand(expectedPersonNoAddressOnly));
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY
                        + ADDRESS_DESC_AMY + TIME_DESC_AMY
                        + TAG_DESC_STUDENT + MEETING_LINK_DESC_AMY,
                new AddCommand(expectedPersonNoRemarkOnly));
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY
                        + ADDRESS_DESC_AMY + REMARK_DESC_AMY
                        + TAG_DESC_STUDENT + MEETING_LINK_DESC_AMY,
                new AddCommand(expectedPersonNoTimeOnly));
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY
                        + ADDRESS_DESC_AMY + TIME_DESC_AMY
                        + REMARK_DESC_AMY + TAG_DESC_STUDENT,
                new AddCommand(expectedPersonNoMeetingLinkOnly));

        // provide only the prefix, without a field value
        assertParseSuccess(parser, NAME_DESC_AMY + " " + PREFIX_PHONE,
                new AddCommand(expectedPersonNoOptionalFields));
        assertParseSuccess(parser, NAME_DESC_AMY + " " + PREFIX_ADDRESS,
                new AddCommand(expectedPersonNoOptionalFields));
        assertParseSuccess(parser, NAME_DESC_AMY + " " + PREFIX_PHONE
                        + ADDRESS_DESC_AMY + TIME_DESC_AMY
                        + TAG_DESC_STUDENT + REMARK_DESC_AMY
                        + MEETING_LINK_DESC_AMY,
                new AddCommand(expectedPersonNoPhoneOnly));
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + " "
                        + PREFIX_ADDRESS + TIME_DESC_AMY
                        + TAG_DESC_STUDENT + REMARK_DESC_AMY
                        + MEETING_LINK_DESC_AMY,
                new AddCommand(expectedPersonNoAddressOnly));
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY
                        + ADDRESS_DESC_AMY + " " + PREFIX_TIME
                        + TAG_DESC_STUDENT + REMARK_DESC_AMY
                        + MEETING_LINK_DESC_AMY,
                new AddCommand(expectedPersonNoTimeOnly));
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY
                        + ADDRESS_DESC_AMY + TIME_DESC_AMY
                        + TAG_DESC_STUDENT + " " + PREFIX_REMARK
                        + MEETING_LINK_DESC_AMY,
                new AddCommand(expectedPersonNoRemarkOnly));
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY
                        + ADDRESS_DESC_AMY + TIME_DESC_AMY
                        + TAG_DESC_STUDENT + REMARK_DESC_AMY
                        + " " + PREFIX_MEETING_LINK,
                new AddCommand(expectedPersonNoMeetingLinkOnly));
    }

    @Test
    public void parse_withCurrentMaxId_assignsNextId() {
        AddCommandParser parserWithCurrentMaxId = new AddCommandParser(Optional.of(Id.of(5)));
        Person expectedPerson = new PersonBuilder()
                .withId(6)
                .withName("Amy Bee")
                .withoutPhone()
                .withoutAddress()
                .withoutTime()
                .withoutRemark()
                .withoutMeetingLink()
                .build();

        assertParseSuccess(parserWithCurrentMaxId, NAME_DESC_AMY,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + ADDRESS_DESC_BOB
                + TIME_DESC_BOB + TAG_DESC_STUDENT + REMARK_DESC_BOB + MEETING_LINK_DESC_BOB,
                expectedMessage);

        // missing name prefix and all other prefixes
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_ADDRESS_BOB
                + VALID_TIME_BOB + VALID_TAG_STUDENT + VALID_REMARK_BOB + VALID_MEETING_LINK_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // presence of text input between the command word and the first prefix
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB
                + ADDRESS_DESC_BOB + TIME_DESC_BOB + TAG_DESC_PARENT
                + TAG_DESC_STUDENT + REMARK_DESC_BOB + MEETING_LINK_DESC_BOB,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + ADDRESS_DESC_BOB
                + TIME_DESC_BOB + TAG_DESC_PARENT + TAG_DESC_STUDENT
                + REMARK_DESC_BOB + MEETING_LINK_DESC_BOB,
                Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + ADDRESS_DESC_BOB
                + TIME_DESC_BOB + TAG_DESC_PARENT + TAG_DESC_STUDENT
                + REMARK_DESC_BOB + MEETING_LINK_DESC_BOB,
                Phone.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + ADDRESS_DESC_BOB
                + TIME_DESC_BOB + INVALID_TAG_DESC + TAG_DESC_STUDENT
                + REMARK_DESC_BOB + MEETING_LINK_DESC_BOB,
                Tag.MESSAGE_TAG_CONSTRAINTS);
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + ADDRESS_DESC_BOB
                + TIME_DESC_BOB + INVALID_UNLISTED_TAG_DESC + TAG_DESC_STUDENT
                + REMARK_DESC_BOB + MEETING_LINK_DESC_BOB,
                Tag.MESSAGE_TAG_CONSTRAINTS);

        // invalid meeting link
        assertParseFailure(parser, NAME_DESC_AMY + PHONE_DESC_AMY + ADDRESS_DESC_AMY
                + TIME_DESC_AMY + TAG_DESC_STUDENT + REMARK_DESC_AMY + INVALID_MEETING_LINK_DESC,
                MeetingLink.MESSAGE_CONSTRAINTS);

        // invalid time
        assertParseFailure(parser, NAME_DESC_AMY + PHONE_DESC_AMY + ADDRESS_DESC_AMY
                + INVALID_TIME_DESC + TAG_DESC_STUDENT + REMARK_DESC_AMY + MEETING_LINK_DESC_AMY,
                Time.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, NAME_DESC_AMY + PHONE_DESC_AMY + ADDRESS_DESC_AMY
                + INVALID_TIME_DURATION_DESC + TAG_DESC_STUDENT + REMARK_DESC_AMY + MEETING_LINK_DESC_AMY,
                Time.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, NAME_DESC_AMY + PHONE_DESC_AMY + ADDRESS_DESC_AMY
                + INVALID_TIME_ZERO_DURATION_DESC + TAG_DESC_STUDENT + REMARK_DESC_AMY + MEETING_LINK_DESC_AMY,
                Time.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, NAME_DESC_AMY + PHONE_DESC_AMY + ADDRESS_DESC_AMY
                + INVALID_TIME_NO_DAY_DESC + TAG_DESC_STUDENT + REMARK_DESC_AMY + MEETING_LINK_DESC_AMY,
                Time.MESSAGE_CONSTRAINTS);
    }
}
