package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_MEETING_LINK_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DELETE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TIME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TIME_DURATION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TIME_NO_DAY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TIME_ZERO_DURATION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_UNLISTED_TAG_DELETE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_UNLISTED_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.MEETING_LINK_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DELETE_DESC_PARENT;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DELETE_DESC_STUDENT;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_PARENT;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_STUDENT;
import static seedu.address.logic.commands.CommandTestUtil.TIME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TIME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TIME_DESC_DURATION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MEETING_LINK_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_PARENT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_STUDENT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TIME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TIME_DURATION_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_LINK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_DELETE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIds.ID_FIRST;
import static seedu.address.testutil.TypicalIds.ID_SECOND;
import static seedu.address.testutil.TypicalIds.ID_THIRD;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.Id;
import seedu.address.model.person.MeetingLink;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Time;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditPersonDescriptorBuilder;

public class EditCommandParserTest {

    private static final String PHONE_EMPTY = " " + PREFIX_PHONE;
    private static final String TAG_EMPTY = " " + PREFIX_TAG;
    private static final String TAG_DELETE_EMPTY = " " + PREFIX_TAG_DELETE;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // nothing provided alongside "edit" - invalid input format
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);

        // id missing, only a string is provided alongside "edit"
        // this string cannot be parsed into an id, so we treat it as an invalid id
        assertParseFailure(parser, VALID_NAME_AMY, Id.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_noFieldsSpecified_success() {
        Id targetId = ID_FIRST;
        String userInput = String.valueOf(targetId.getValue());

        EditCommand expectedCommand = new EditCommand(targetId, new EditPersonDescriptor());
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // non-positive id
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, Id.MESSAGE_CONSTRAINTS); // negative id
        assertParseFailure(parser, "0" + NAME_DESC_AMY, Id.MESSAGE_CONSTRAINTS); // zero id

        // wrongly formatted id
        assertParseFailure(parser, "1 some random string", Id.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 i/ string", Id.MESSAGE_CONSTRAINTS);

        // id leading to overflow
        long maxPossibleInt = Integer.MAX_VALUE;
        long minPossibleInt = Integer.MIN_VALUE;
        assertParseFailure(parser, Long.toString(maxPossibleInt + 1),
                Id.OVERFLOW_MESSAGE_CONSTRAINTS); // positive id
        assertParseFailure(parser, Long.toString(minPossibleInt - 1),
                Id.OVERFLOW_MESSAGE_CONSTRAINTS); // negative id
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_TIME_DESC, Time.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_TIME_DURATION_DESC, Time.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_TIME_ZERO_DURATION_DESC, Time.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_TIME_NO_DAY_DESC, Time.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_TAG_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_UNLISTED_TAG_DESC, Tag.MESSAGE_TAG_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_MEETING_LINK_DESC, MeetingLink.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_TAG_DELETE_DESC, Tag.MESSAGE_TAG_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_UNLISTED_TAG_DELETE_DESC, Tag.MESSAGE_TAG_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_DELETE_EMPTY, Tag.MESSAGE_TAG_CONSTRAINTS);

        assertParseFailure(parser, "1" + TAG_DESC_STUDENT + TAG_DESC_PARENT + TAG_EMPTY,
                Messages.MESSAGE_INVALID_TAG_RESET);
        assertParseFailure(parser, "1" + TAG_DESC_STUDENT + TAG_EMPTY + TAG_DESC_PARENT,
                Messages.MESSAGE_INVALID_TAG_RESET);
        assertParseFailure(parser, "1" + TAG_EMPTY + TAG_DESC_STUDENT + TAG_DESC_PARENT,
                Messages.MESSAGE_INVALID_TAG_RESET);
        assertParseFailure(parser, "1" + TAG_EMPTY + TAG_DELETE_DESC_STUDENT,
                Messages.MESSAGE_INVALID_TAG_RESET);
        assertParseFailure(parser, "1" + TAG_DESC_STUDENT + TAG_DELETE_DESC_STUDENT,
                Messages.MESSAGE_CONFLICTING_TAG_EDITS);

        assertParseFailure(parser, "1" + INVALID_NAME_DESC + VALID_ADDRESS_AMY + VALID_PHONE_AMY,
                Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Id targetId = ID_SECOND;
        String userInput = targetId.getValue() + PHONE_DESC_BOB + TAG_DESC_PARENT
                + ADDRESS_DESC_AMY + TIME_DESC_AMY + NAME_DESC_AMY + TAG_DESC_STUDENT;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withAddress(VALID_ADDRESS_AMY).withTime(VALID_TIME_AMY)
                .withTags(VALID_TAG_PARENT, VALID_TAG_STUDENT).build();
        EditCommand expectedCommand = new EditCommand(targetId, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Id targetId = ID_FIRST;
        String userInput = targetId.getValue() + PHONE_DESC_BOB;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .build();
        EditCommand expectedCommand = new EditCommand(targetId, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        Id targetId = ID_THIRD;
        String userInput = targetId.getValue() + NAME_DESC_AMY;
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetId, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = targetId.getValue() + PHONE_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new EditCommand(targetId, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = targetId.getValue() + ADDRESS_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withAddress(VALID_ADDRESS_AMY).build();
        expectedCommand = new EditCommand(targetId, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = targetId.getValue() + TIME_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withTime(VALID_TIME_AMY).build();
        expectedCommand = new EditCommand(targetId, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = targetId.getValue() + TAG_DESC_STUDENT;
        descriptor = new EditPersonDescriptorBuilder().withTags(VALID_TAG_STUDENT).build();
        expectedCommand = new EditCommand(targetId, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = targetId.getValue() + MEETING_LINK_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withMeetingLink(VALID_MEETING_LINK_AMY).build();
        expectedCommand = new EditCommand(targetId, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = targetId.getValue() + TAG_DELETE_DESC_STUDENT;
        descriptor = new EditPersonDescriptorBuilder().withTagsToDelete(VALID_TAG_STUDENT).build();
        expectedCommand = new EditCommand(targetId, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetMeetingLink_success() {
        Id targetId = ID_SECOND;
        String userInput = targetId.getValue() + " " + PREFIX_MEETING_LINK;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withoutMeetingLink().build();
        EditCommand expectedCommand = new EditCommand(targetId, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetMeetingLinkWithOtherField_success() {
        Id targetId = ID_SECOND;
        String userInput = targetId.getValue() + NAME_DESC_AMY + " " + PREFIX_MEETING_LINK;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName(VALID_NAME_AMY).withoutMeetingLink().build();
        EditCommand expectedCommand = new EditCommand(targetId, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleTagsSpecified_success() {
        Id targetId = ID_THIRD;
        String userInput = targetId.getValue() + TAG_DESC_STUDENT + TAG_DESC_PARENT;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withTags(VALID_TAG_STUDENT, VALID_TAG_PARENT).build();
        EditCommand expectedCommand = new EditCommand(targetId, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_repeatedSameTagSpecified_success() {
        Id targetId = ID_THIRD;
        String userInput = targetId.getValue() + TAG_DESC_STUDENT + TAG_DESC_STUDENT;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withTags(VALID_TAG_STUDENT).build();
        EditCommand expectedCommand = new EditCommand(targetId, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleCategoriesToDeleteSpecified_success() {
        Id targetId = ID_THIRD;
        String userInput = targetId.getValue() + TAG_DELETE_DESC_STUDENT + TAG_DELETE_DESC_PARENT;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withTagsToDelete(VALID_TAG_STUDENT, VALID_TAG_PARENT).build();
        EditCommand expectedCommand = new EditCommand(targetId, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_addAndDeleteCategoriesSpecified_success() {
        Id targetId = ID_SECOND;
        String userInput = targetId.getValue() + TAG_DESC_PARENT + TAG_DELETE_DESC_STUDENT;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withTags(VALID_TAG_PARENT)
                .withTagsToDelete(VALID_TAG_STUDENT).build();
        EditCommand expectedCommand = new EditCommand(targetId, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetTagsWithOtherField_success() {
        Id targetId = ID_SECOND;
        String userInput = targetId.getValue() + NAME_DESC_AMY + TAG_EMPTY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName(VALID_NAME_AMY).withTags().build();
        EditCommand expectedCommand = new EditCommand(targetId, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetPhone_success() {
        Id targetId = ID_SECOND;
        String userInput = targetId.getValue() + PHONE_EMPTY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withoutPhone().build();
        EditCommand expectedCommand = new EditCommand(targetId, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetPhoneWithOtherField_success() {
        Id targetId = ID_SECOND;
        String userInput = targetId.getValue() + NAME_DESC_AMY + PHONE_EMPTY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName(VALID_NAME_AMY)
                .withoutPhone()
                .build();
        EditCommand expectedCommand = new EditCommand(targetId, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetAddress_success() {
        Id targetId = ID_SECOND;
        String userInput = targetId.getValue() + " " + PREFIX_ADDRESS;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withoutAddress().build();
        EditCommand expectedCommand = new EditCommand(targetId, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetAddressWithOtherField_success() {
        Id targetId = ID_SECOND;
        String userInput = targetId.getValue() + PHONE_DESC_AMY + " " + PREFIX_ADDRESS;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withPhone(VALID_PHONE_AMY)
                .withoutAddress()
                .build();
        EditCommand expectedCommand = new EditCommand(targetId, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        Id targetId = ID_FIRST;
        String userInput = targetId.getValue() + INVALID_PHONE_DESC + PHONE_DESC_BOB;
        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        userInput = targetId.getValue() + PHONE_DESC_BOB + INVALID_PHONE_DESC;
        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        userInput = targetId.getValue() + PHONE_DESC_AMY + ADDRESS_DESC_AMY
                + TIME_DESC_AMY + TAG_DESC_STUDENT + PHONE_DESC_AMY + ADDRESS_DESC_AMY
                + TIME_DESC_AMY + TAG_DESC_STUDENT + PHONE_DESC_BOB + ADDRESS_DESC_BOB
                + TIME_DESC_BOB + TAG_DESC_PARENT;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_ADDRESS, PREFIX_TIME));

        userInput = targetId.getValue() + INVALID_NAME_DESC + INVALID_PHONE_DESC
                + INVALID_TIME_DESC + INVALID_PHONE_DESC + INVALID_NAME_DESC + TIME_DESC_AMY;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_NAME, PREFIX_TIME));
    }

    @Test
    public void parse_resetTags_success() {
        Id targetId = ID_THIRD;
        String userInput = targetId.getValue() + TAG_EMPTY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withTags().build();
        EditCommand expectedCommand = new EditCommand(targetId, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_clearTime_success() {
        Id targetId = ID_THIRD;
        String userInput = targetId.getValue() + " " + PREFIX_TIME;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withoutTime().build();
        EditCommand expectedCommand = new EditCommand(targetId, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_alternateTimeFormat_success() {
        Id targetId = ID_SECOND;
        String userInput = targetId.getValue() + " " + PREFIX_TIME + "monday 1800";

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withTime("Monday 18:00").build();
        EditCommand expectedCommand = new EditCommand(targetId, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_durationTimeFormat_success() {
        Id targetId = ID_SECOND;
        String userInput = targetId.getValue() + TIME_DESC_DURATION_BOB;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withTime(VALID_TIME_DURATION_BOB).build();
        EditCommand expectedCommand = new EditCommand(targetId, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
