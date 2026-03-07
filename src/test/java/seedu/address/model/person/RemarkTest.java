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
    public void constructor_invalidRemark_throwsIllegalArgumentException() {
        String invalidRemark = "\n";
        assertThrows(IllegalArgumentException.class, () -> new Remark(invalidRemark));
    }

    @Test
    public void isValidRemark() {
        // null remark
        assertThrows(NullPointerException.class, () -> Remark.isValidRemark(null));

        // valid remarks
        assertTrue(Remark.isValidRemark("")); // empty string
        assertTrue(Remark.isValidRemark(" ")); // spaces only
        assertTrue(Remark.isValidRemark("Likes to swim")); // alphanumeric
        assertTrue(Remark.isValidRemark("Loves CS2103T")); // mixed content

        // invalid remarks
        assertFalse(Remark.isValidRemark("\n")); // new line
        assertFalse(Remark.isValidRemark("\t")); // tab
    }
}
