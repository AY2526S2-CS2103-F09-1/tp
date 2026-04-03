package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIds.ID_FIRST;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Id;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Time;
import seedu.address.model.tag.Tag;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_TIME = "25:00";
    private static final String INVALID_TIME_NO_DAY = "18:00";
    private static final String INVALID_TIME_DURATION = "18:00 - 17:30";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_UNLISTED_TAG = "friend";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "12345678";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_TIME = "Monday 18:00";
    private static final String VALID_TIME_ALTERNATE = "monday 1800";
    private static final String VALID_TIME_DURATION = "Wednesday 18:00 - 19:30";
    private static final String VALID_TIME_DURATION_ALTERNATE = "wednesday 1800 - 1930";
    private static final String VALID_TAG_1 = "Student";
    private static final String VALID_TAG_2 = "Parent";

    private static final String WHITESPACE = " \t\r\n";

    private static final String VALID_REMARK = "my 1st student - graduating soon";
    private static final String INVALID_REMARK = " ";

    @Test
    public void parseId_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseId("10 a"));
    }

    @Test
    public void parseId_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, Id.MESSAGE_CONSTRAINTS, ()
            -> ParserUtil.parseId(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseId_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(ID_FIRST, ParserUtil.parseId("1"));

        // Leading and trailing whitespaces
        assertEquals(ID_FIRST, ParserUtil.parseId("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone(null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(Optional.of(INVALID_PHONE)));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Optional<Phone> expectedPhone = Optional.of(new Phone(VALID_PHONE));
        assertEquals(expectedPhone, ParserUtil.parsePhone(Optional.of(VALID_PHONE)));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Optional<Phone> expectedPhone = Optional.of(new Phone(VALID_PHONE));
        assertEquals(expectedPhone, ParserUtil.parsePhone(Optional.of(phoneWithWhitespace)));
    }

    @Test
    public void parsePhone_emptyOptional_returnsEmptyOptional() throws Exception {
        assertEquals(Optional.empty(), ParserUtil.parsePhone(Optional.empty()));
    }

    @Test
    public void parsePhone_emptyStringInOptional_returnsOptionalEmpty() throws Exception {
        assertEquals(Optional.empty(), ParserUtil.parsePhone(Optional.of("")));
    }

    @Test
    public void parseAddress_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress(null));
    }

    @Test
    public void parseAddress_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAddress(Optional.of(" ")));
    }

    @Test
    public void parseAddress_validValueWithoutWhitespace_returnsAddress() throws Exception {
        Optional<Address> expectedAddress = Optional.of(new Address(VALID_ADDRESS));
        assertEquals(expectedAddress, ParserUtil.parseAddress(Optional.of(VALID_ADDRESS)));
    }

    @Test
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Optional<Address> expectedAddress = Optional.of(new Address(VALID_ADDRESS));
        assertEquals(expectedAddress, ParserUtil.parseAddress(Optional.of(addressWithWhitespace)));
    }

    @Test
    public void parseAddress_emptyOptional_returnsEmptyOptional() throws Exception {
        assertEquals(Optional.empty(), ParserUtil.parseAddress(Optional.empty()));
    }

    @Test
    public void parseAddress_emptyStringInOptional_returnsOptionalEmpty() throws Exception {
        assertEquals(Optional.empty(), ParserUtil.parseAddress(Optional.of("")));
    }

    @Test
    public void parseTime_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTime(null));
    }

    @Test
    public void parseTime_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTime(Optional.of(INVALID_TIME)));
        assertThrows(ParseException.class, () -> ParserUtil.parseTime(Optional.of(INVALID_TIME_NO_DAY)));
        assertThrows(ParseException.class, () -> ParserUtil.parseTime(Optional.of(INVALID_TIME_DURATION)));
    }

    @Test
    public void parseTime_validValueWithoutWhitespace_returnsTime() throws Exception {
        Optional<Time> expectedTime = Optional.of(new Time(VALID_TIME));
        assertEquals(expectedTime, ParserUtil.parseTime(Optional.of(VALID_TIME)));
    }

    @Test
    public void parseTime_alternateValidValueWithoutWhitespace_returnsCanonicalTime() throws Exception {
        Optional<Time> expectedTime = Optional.of(new Time(VALID_TIME));
        assertEquals(expectedTime, ParserUtil.parseTime(Optional.of(VALID_TIME_ALTERNATE)));
    }

    @Test
    public void parseTime_validDurationWithoutWhitespace_returnsTime() throws Exception {
        Optional<Time> expectedTime = Optional.of(new Time(VALID_TIME_DURATION));
        assertEquals(expectedTime, ParserUtil.parseTime(Optional.of(VALID_TIME_DURATION)));
    }

    @Test
    public void parseTime_alternateValidDurationWithoutWhitespace_returnsCanonicalTime() throws Exception {
        Optional<Time> expectedTime = Optional.of(new Time(VALID_TIME_DURATION));
        assertEquals(expectedTime, ParserUtil.parseTime(Optional.of(VALID_TIME_DURATION_ALTERNATE)));
    }

    @Test
    public void parseTime_validValueWithWhitespace_returnsTrimmedTime() throws Exception {
        String timeWithWhitespace = WHITESPACE + VALID_TIME + WHITESPACE;
        Optional<Time> expectedTime = Optional.of(new Time(VALID_TIME));
        assertEquals(expectedTime, ParserUtil.parseTime(Optional.of(timeWithWhitespace)));
    }

    @Test
    public void parseTime_validDurationWithWhitespace_returnsTrimmedTime() throws Exception {
        String timeWithWhitespace = WHITESPACE + VALID_TIME_DURATION_ALTERNATE + WHITESPACE;
        Optional<Time> expectedTime = Optional.of(new Time(VALID_TIME_DURATION));
        assertEquals(expectedTime, ParserUtil.parseTime(Optional.of(timeWithWhitespace)));
    }

    @Test
    public void parseTime_emptyOptional_returnsEmptyOptional() throws Exception {
        assertEquals(Optional.empty(), ParserUtil.parseTime(Optional.empty()));
    }

    @Test
    public void parseTime_emptyStringInOptional_returnsOptionalEmpty() throws Exception {
        assertEquals(Optional.empty(), ParserUtil.parseTime(Optional.of("")));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, Tag.MESSAGE_TAG_CONSTRAINTS, () ->
                ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_invalidUnlistedTag_throwsParseException() {
        assertThrows(ParseException.class, Tag.MESSAGE_TAG_CONSTRAINTS, () ->
                ParserUtil.parseTag(INVALID_UNLISTED_TAG));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsNormalizedTag() throws Exception {
        Tag expectedTag = new Tag("Student");
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsNormalizedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag("Student");
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTag_validValueWithDifferentCase_returnsNormalizedTag() throws Exception {
        Tag expectedTag = new Tag("Tutor");
        assertEquals(expectedTag, ParserUtil.parseTag("tUtOr"));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, Tag.MESSAGE_TAG_CONSTRAINTS, () ->
                ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    public void parseTags_collectionWithInvalidUnlistedTags_throwsParseException() {
        assertThrows(ParseException.class, Tag.MESSAGE_TAG_CONSTRAINTS, () ->
                ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_UNLISTED_TAG)));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag("Student"), new Tag("Parent")));

        assertEquals(expectedTagSet, actualTagSet);
    }

    @Test
    public void parseRemark_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseRemark(null));
    }

    @Test
    public void parseRemark_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseRemark(Optional.of(INVALID_REMARK)));
    }

    @Test
    public void parseRemark_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Optional<Remark> expectedRemark = Optional.of(new Remark(VALID_REMARK));
        assertEquals(expectedRemark, ParserUtil.parseRemark(Optional.of(VALID_REMARK)));
    }

    @Test
    public void parseRemark_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String remarkWithWhitespace = WHITESPACE + VALID_REMARK + WHITESPACE;
        Optional<Remark> expectedPhone = Optional.of(new Remark(VALID_REMARK));
        assertEquals(expectedPhone, ParserUtil.parseRemark(Optional.of(remarkWithWhitespace)));
    }

    @Test
    public void parseRemark_emptyOptional_returnsEmptyOptional() throws Exception {
        assertEquals(Optional.empty(), ParserUtil.parseRemark(Optional.empty()));
    }

    @Test
    public void parseRemark_emptyStringInOptional_returnsOptionalEmpty() throws Exception {
        assertEquals(Optional.empty(), ParserUtil.parseRemark(Optional.of("")));
    }
}
