package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    public void getNormalizedCategoryTagName_nullTagName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Tag.getNormalizedCategoryTagName(null));
    }

    @Test
    public void getNormalizedCategoryTagName_validTagName_returnsNormalizedCategory() {
        assertEquals("Student", Tag.getNormalizedCategoryTagName("student"));
    }

}
