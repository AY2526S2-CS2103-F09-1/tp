package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RemarkTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Remark(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidRemark = "";
        assertThrows(IllegalArgumentException.class, () -> new Remark(invalidRemark));
    }

    @Test
    public void isValidRemark() {
        // null remark
        assertThrows(NullPointerException.class, () -> Remark.isValidRemark(null));

        // invalid remark
        assertFalse(Remark.isValidRemark("")); // empty string
        assertFalse(Remark.isValidRemark(" ")); // spaces only
        assertFalse(Remark.isValidRemark("  "));
        assertFalse(Remark.isValidRemark("\n")); // newline only
        assertFalse(Remark.isValidRemark("\t")); // tab only
        assertFalse(Remark.isValidRemark(" \n"));
        assertFalse(Remark.isValidRemark(" \t"));
        assertFalse(Remark.isValidRemark("\n "));
        assertFalse(Remark.isValidRemark("\t "));
        assertFalse(Remark.isValidRemark(" \n "));
        assertFalse(Remark.isValidRemark(" \t "));
        assertFalse(Remark.isValidRemark("Hello \n John!")); // contains not only newline
        assertFalse(Remark.isValidRemark("Hello \t John!")); // contains not only tab

        // valid remark
        assertTrue(Remark.isValidRemark("^$^%#")); // only special characters
        assertTrue(Remark.isValidRemark("peter*")); // contains special characters
        assertTrue(Remark.isValidRemark("peter the 2nd")); // contains numbers
        assertTrue(Remark.isValidRemark("12345")); // numbers only
        assertTrue(Remark.isValidRemark("owes tuition fees")); // alphabets only
        assertTrue(Remark.isValidRemark("Is a Naughty Kid")); // with capital letters
        assertTrue(Remark.isValidRemark("Requires more help for homework, assignments and tests")); // with a comma
        assertTrue(Remark.isValidRemark("Good student - does his homework regularly")); // with a dash
        assertTrue(Remark.isValidRemark("doesn't use WhatsApp")); // with apostrophes
        assertTrue(Remark.isValidRemark("Has Completed Homework 5 Consecutive times")); // letters and numbers
        assertTrue(Remark.isValidRemark("This sentence has a fullstop.")); // has a fullstop
    }

    @Test
    public void isValidRemarkOrEmptyString() {
        // null remark
        assertThrows(NullPointerException.class, () -> Remark.isValidRemarkOrEmptyString(null));

        // empty string is allowed for storage loading
        assertTrue(Remark.isValidRemarkOrEmptyString(""));

        // invalid remark
        assertFalse(Remark.isValidRemarkOrEmptyString(" ")); // spaces only
        assertFalse(Remark.isValidRemarkOrEmptyString("\n")); // newline only
        assertFalse(Remark.isValidRemarkOrEmptyString("\t")); // tab only
        assertFalse(Remark.isValidRemarkOrEmptyString("Hello \n John!")); // contains newline
        assertFalse(Remark.isValidRemarkOrEmptyString("Hello \t John!")); // contains tab

        // valid remark
        assertTrue(Remark.isValidRemarkOrEmptyString("owes tuition fees"));
        assertTrue(Remark.isValidRemarkOrEmptyString("peter the 2nd"));
        assertTrue(Remark.isValidRemarkOrEmptyString("This sentence has a fullstop."));
    }

    @Test
    public void equals() {
        String remarkString = "Valid Remark";
        Remark remark = new Remark(remarkString);

        // same values -> returns true
        assertTrue(remark.equals(new Remark(remarkString)));

        // same object -> returns true
        assertTrue(remark.equals(remark));

        // null -> returns false
        assertFalse(remark.equals(null));

        // different types -> returns false
        assertFalse(remark.equals(5.0f));

        // different values -> returns false
        assertFalse(remark.equals(new Remark("Other Valid Remark")));
    }
}
