package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class IdTest {
    @Test
    public void of_invalidId_throwsIllegalArgumentException() {
        int invalidId = -1;
        assertThrows(IllegalArgumentException.class, () -> Id.of(invalidId));
    }

    @Test
    public void isValidId() {
        // invalid id
        assertFalse(Id.isValidId(-5)); // negative value
        assertFalse(Id.isValidId(0)); // zero

        // valid id
        assertTrue(Id.isValidId(1)); // smallest possible value
        assertTrue(Id.isValidId(23)); // positive value
        assertTrue(Id.isValidId(839082238)); // large positive value
    }

    @Test
    public void equals() {
        Id id = Id.of(3);

        // same values -> returns true
        assertTrue(id.equals(Id.of(3)));

        // same object -> returns true
        assertTrue(id.equals(id));

        // null -> returns false
        assertFalse(id.equals(null));

        // different types -> returns false
        assertFalse(id.equals(3));

        // different types -> returns false
        assertFalse(id.equals(5.0f));

        // different values -> returns false
        assertFalse(id.equals(Id.of(5)));
    }
}
