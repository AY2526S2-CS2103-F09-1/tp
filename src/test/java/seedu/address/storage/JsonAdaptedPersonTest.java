package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.JIM;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Id;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;

public class JsonAdaptedPersonTest {
    private static final int INVALID_ID = -1;
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_TAG = "#friend";

    private static final int VALID_ID = BENSON.getId().getValue();
    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().get().value.toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().get().value;
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    private static final String VALID_REMARK = JIM.getRemark().get().value;
    private static final String INVALID_REMARK_SPACES = "   ";
    private static final String INVALID_REMARK_NEWLINE = "\n";
    private static final String INVALID_REMARK_TAB = "\t";

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_emptyPhone_returnsPersonWithEmptyPhone() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
            VALID_ID, VALID_NAME, "", VALID_ADDRESS, VALID_TAGS, VALID_REMARK
        );
        Person modelPerson = person.toModelType();

        assertEquals(Optional.empty(), modelPerson.getPhone());
    }

    @Test
    public void toModelType_validPhone_returnsPersonWithPhone() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID, VALID_NAME, VALID_PHONE, VALID_ADDRESS, VALID_TAGS, VALID_REMARK);
        Person modelPerson = person.toModelType();

        assertEquals(Optional.of(new Phone(VALID_PHONE)), modelPerson.getPhone());
    }

    @Test
    public void toModelType_emptyAddress_returnsPersonWithEmptyAddress() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_ID, VALID_NAME, VALID_PHONE, "", VALID_TAGS, VALID_REMARK
        );
        Person modelPerson = person.toModelType();

        assertEquals(Optional.empty(), modelPerson.getAddress());
    }

    @Test
    public void toModelType_invalidId_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(INVALID_ID, VALID_NAME, VALID_PHONE,
                VALID_ADDRESS, VALID_TAGS, VALID_REMARK);
        String expectedMessage = Id.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_ID, INVALID_NAME, VALID_PHONE,
                VALID_ADDRESS, VALID_TAGS, VALID_REMARK);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_ID, null, VALID_PHONE,
                VALID_ADDRESS, VALID_TAGS, VALID_REMARK);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_ID, VALID_NAME, INVALID_PHONE,
                VALID_ADDRESS, VALID_TAGS, VALID_REMARK);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_ID, VALID_NAME, null,
                VALID_ADDRESS, VALID_TAGS, VALID_REMARK);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_ID, VALID_NAME, VALID_PHONE,
                INVALID_ADDRESS, VALID_TAGS, VALID_REMARK);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_ID, VALID_NAME, VALID_PHONE,
                null, VALID_TAGS, VALID_REMARK);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_ID, VALID_NAME, VALID_PHONE,
                VALID_ADDRESS, invalidTags, VALID_REMARK);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidRemark_throwsIllegalValueException() {
        checkInvalidRemark(INVALID_REMARK_SPACES);
        checkInvalidRemark(INVALID_REMARK_NEWLINE);
        checkInvalidRemark(INVALID_REMARK_TAB);
    }

    @Test
    public void toModelType_emptyRemark_returnsPersonWithEmptyRemark() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                // note that empty string ok for JsonAdaptedPerson and is expected to be Optional.empty after parsing
                VALID_ID, VALID_NAME, VALID_PHONE, VALID_ADDRESS, VALID_TAGS, "");
        Person modelPerson = person.toModelType();

        assertEquals(Optional.empty(), modelPerson.getRemark());
    }

    @Test
    public void toModelType_nullRemark_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_ID, VALID_NAME, VALID_PHONE,
                VALID_ADDRESS, VALID_TAGS, null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Remark.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    private void checkInvalidRemark(String invalidRemark) {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_ID, VALID_NAME, VALID_PHONE,
                VALID_ADDRESS, VALID_TAGS, invalidRemark);
        String expectedMessage = Remark.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

}
