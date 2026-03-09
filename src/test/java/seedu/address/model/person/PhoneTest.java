package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PhoneTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Phone(null));
    }

    @Test
    public void isValidPhone() {
        // null phone number
        assertThrows(NullPointerException.class, () -> Phone.isValidPhone(null));

        // invalid phone numbers
        assertFalse(Phone.isValidPhone(" ")); // spaces only
        assertFalse(Phone.isValidPhone("91")); // less than 8 numbers
        assertFalse(Phone.isValidPhone("91354353280948239882")); // more than 8 numbers
        assertFalse(Phone.isValidPhone("phone")); // non-numeric
        assertFalse(Phone.isValidPhone("9011p041")); // alphabets within digits
        assertFalse(Phone.isValidPhone("9312 1534")); // spaces within digits
        assertFalse(Phone.isValidPhone("91##5^3?")); // symbols within digits

        // valid phone numbers
        assertTrue(Phone.isValidPhone("")); // empty string is now considered a valid phone number
        assertTrue(Phone.isValidPhone("93121534")); // exactly 8 numbers
        assertTrue(Phone.isValidPhone("10348692")); // exactly 8 numbers
    }

    @Test
    public void equals() {
        Phone phone = new Phone("89104850");

        // same values -> returns true
        assertTrue(phone.equals(new Phone("89104850")));

        // same object -> returns true
        assertTrue(phone.equals(phone));

        // null -> returns false
        assertFalse(phone.equals(null));

        // different types -> returns false
        assertFalse(phone.equals(5.0f));

        // different values -> returns false
        assertFalse(phone.equals(new Phone("99541352")));
    }

    @Test
    public void constructor_validInput_storesCorrectValue() {
        String emptyString = "";
        // no phone number
        assertTrue(new Phone(emptyString).value.equals(emptyString));

        String validPhone = "81120129";
        // a valid phone number
        assertTrue(new Phone(validPhone).value.equals(validPhone));
    }
}
