package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class DateTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Date(null));
    }

    @Test
    public void constructor_invalidDate_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Date("2026-02-30"));
    }

    @Test
    public void isValidDate() {
        assertThrows(NullPointerException.class, () -> Date.isValidDate(null));

        assertFalse(Date.isValidDate(" "));
        assertFalse(Date.isValidDate(""));
        assertFalse(Date.isValidDate("2026-02-30"));
        assertFalse(Date.isValidDate("02-04-2026"));
        assertFalse(Date.isValidDate("Apr 2 2026"));

        assertTrue(Date.isValidDate("2026-04-02"));
        assertTrue(Date.isValidDate("2/4/2026"));
    }

    @Test
    public void equals() {
        Date date = new Date("2026-04-02");

        assertTrue(date.equals(new Date("2/4/2026")));
        assertTrue(date.equals(date));
        assertFalse(date.equals(null));
        assertFalse(date.equals(5.0f));
        assertFalse(date.equals(new Date("2026-04-03")));
    }

    @Test
    public void constructor_validInput_storesCanonicalValue() {
        assertEquals("2026-04-02", new Date("2026-04-02").value);
        assertEquals("2026-04-02", new Date("2/4/2026").value);
    }

    @Test
    public void toString_validDate_returnsDisplayValue() {
        assertEquals("Apr 2 2026", new Date("2026-04-02").toString());
    }

    @Test
    public void hashCode_sameDate_sameHashCode() {
        assertEquals(new Date("2026-04-02").hashCode(), new Date("2/4/2026").hashCode());
    }

    @Test
    public void isValidDateOrEmptyString() {
        assertTrue(Date.isValidDateOrEmptyString(""));
        assertTrue(Date.isValidDateOrEmptyString("2026-04-02"));
        assertTrue(Date.isValidDateOrEmptyString("2/4/2026"));
        assertFalse(Date.isValidDateOrEmptyString("2026-02-30"));
    }
}
