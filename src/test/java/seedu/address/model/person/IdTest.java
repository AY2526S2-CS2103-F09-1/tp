package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.person.Id.INCOMING_OVERFLOW_MESSAGE;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

public class IdTest {
    @Test
    public void of_validId_success() {
        int smallestValidId = 1;
        assertEquals(smallestValidId, Id.of(smallestValidId).getValue());

        int largestValidId = Integer.MAX_VALUE;
        assertEquals(largestValidId, Id.of(largestValidId).getValue());
    }

    @Test
    public void of_invalidId_throwsIllegalArgumentException() {
        int invalidId = -1;
        assertThrows(IllegalArgumentException.class, () -> Id.of(invalidId));
    }

    @Test
    public void fromCurrentMaxId_emptyCurrentMaxId_returnsSmallestPossibleId() {
        Optional<Id> currentMaxId = Optional.empty();
        Id expectedId = Id.of(1);

        assertEquals(expectedId, Id.fromCurrentMaxId(currentMaxId));
    }

    @Test
    public void fromCurrentMaxId_nonEmptyCurrentMaxId_success() {
        Optional<Id> currentMaxId = Optional.of(Id.of(5));
        Id expectedId = Id.of(6);

        assertEquals(expectedId, Id.fromCurrentMaxId(currentMaxId));
    }

    @Test
    public void fromCurrentMaxId_largestPossibleCurrentMaxId_throwsIllegalArgumentException() {
        Optional<Id> currentMaxId = Optional.of(Id.of(Integer.MAX_VALUE));
        assertThrows(IllegalArgumentException.class,
                INCOMING_OVERFLOW_MESSAGE, () -> Id.fromCurrentMaxId(currentMaxId));
    }

    @Test
    public void isValidId() {
        // invalid id
        assertFalse(Id.isValidId(-5)); // negative value
        assertFalse(Id.isValidId(0)); // zero

        // valid id
        assertTrue(Id.isValidId(1)); // smallest possible value
        assertTrue(Id.isValidId(23)); // positive value
        assertTrue(Id.isValidId(Integer.MAX_VALUE)); // largest positive value
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
