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
import seedu.address.model.tag.Tag;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_TAG = "#friend";
    private static final String UNSUPPORTED_TAG = "friend";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "12345678";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_TAG_1 = "Student";
    private static final String VALID_TAG_2 = "Parent";

    private static final String WHITESPACE = " \t\r\n";

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
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, Tag.MESSAGE_TAG_CONSTRAINTS, () ->
                ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_unsupportedCategory_throwsParseException() {
        assertThrows(ParseException.class, Tag.MESSAGE_TAG_CONSTRAINTS, () ->
                ParserUtil.parseTag(UNSUPPORTED_TAG));
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
    public void parseTags_collectionWithUnsupportedTags_throwsParseException() {
        assertThrows(ParseException.class, Tag.MESSAGE_TAG_CONSTRAINTS, () ->
                ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, UNSUPPORTED_TAG)));
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
}
