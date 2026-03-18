package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void isValidTagName_nullTagName_throwsNullPointerException() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));
    }

    @Test
    public void isValidCategoryTagName_validAndInvalidInputs_returnsExpectedResult() {
        assertThrows(NullPointerException.class, () -> Tag.isValidCategoryTagName(null));
        assertTrue(Tag.isValidCategoryTagName("student"));
        assertTrue(Tag.isValidCategoryTagName("Tutor"));
        assertFalse(Tag.isValidCategoryTagName("friend"));
    }

}
