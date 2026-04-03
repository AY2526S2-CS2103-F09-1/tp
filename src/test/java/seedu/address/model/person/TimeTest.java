package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TimeTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Time(null));
    }

    @Test
    public void constructor_invalidTime_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Time("25:00"));
        assertThrows(IllegalArgumentException.class, () -> new Time("18:00 - 17:30"));
        assertThrows(IllegalArgumentException.class, () -> new Time("Monday"));
        assertThrows(IllegalArgumentException.class, () -> new Time("Funday 18:00"));
        assertThrows(IllegalArgumentException.class, () -> new Time("Monday 18:00 - 17:30"));
    }

    @Test
    public void isValidTime() {
        assertThrows(NullPointerException.class, () -> Time.isValidTime(null));

        assertFalse(Time.isValidTime(" "));
        assertFalse(Time.isValidTime(""));
        assertFalse(Time.isValidTime("25:00"));
        assertFalse(Time.isValidTime("18:00"));
        assertFalse(Time.isValidTime("1800"));
        assertFalse(Time.isValidTime("9:30"));
        assertFalse(Time.isValidTime("093"));
        assertFalse(Time.isValidTime("18:00 - 17:30"));
        assertFalse(Time.isValidTime("18:00 - 1830"));
        assertFalse(Time.isValidTime("Monday"));
        assertFalse(Time.isValidTime("Funday 18:00"));
        assertFalse(Time.isValidTime("Monday 18:00 - 1830"));

        assertTrue(Time.isValidTime("Monday 18:00"));
        assertTrue(Time.isValidTime("monday 1800"));
        assertTrue(Time.isValidTime("Sunday 18:00 - 19:30"));
        assertTrue(Time.isValidTime("sunday 1800 - 1930"));
    }

    @Test
    public void equals() {
        Time time = new Time("Monday 18:00");

        assertTrue(time.equals(new Time("monday 1800")));
        assertTrue(time.equals(time));
        assertFalse(time.equals(null));
        assertFalse(time.equals(5.0f));
        assertFalse(time.equals(new Time("Monday 18:30")));
        assertFalse(time.equals(new Time("Monday 18:00 - 19:00")));
    }

    @Test
    public void constructor_validInput_storesCanonicalValue() {
        assertEquals("Monday 18:00", new Time("Monday 18:00").value);
        assertEquals("Monday 18:00", new Time("monday 1800").value);
        assertEquals("Sunday 18:00 - 19:30", new Time("Sunday 18:00 - 19:30").value);
        assertEquals("Sunday 18:00 - 19:30", new Time("sunday 1800 - 1930").value);
    }

    @Test
    public void toString_validTime_returnsDisplayValue() {
        assertEquals("Monday 18:00", new Time("Monday 18:00").toString());
        assertEquals("Sunday 18:00 - 19:30", new Time("sunday 1800 - 1930").toString());
    }

    @Test
    public void hashCode_sameTime_sameHashCode() {
        assertEquals(new Time("Monday 18:00").hashCode(), new Time("monday 1800").hashCode());
        assertEquals(new Time("Sunday 18:00 - 19:30").hashCode(),
                new Time("sunday 1800 - 1930").hashCode());
    }

    @Test
    public void fromStoredValue_legacyTime_returnsCanonicalLegacyValue() {
        assertEquals("18:00", Time.fromStoredValue("1800").value);
        assertEquals("18:00 - 19:30", Time.fromStoredValue("18:00 - 19:30").value);
    }

    @Test
    public void isValidTimeOrEmptyString() {
        assertTrue(Time.isValidTimeOrEmptyString(""));
        assertTrue(Time.isValidTimeOrEmptyString("18:00"));
        assertTrue(Time.isValidTimeOrEmptyString("1800"));
        assertTrue(Time.isValidTimeOrEmptyString("18:00 - 19:30"));
        assertTrue(Time.isValidTimeOrEmptyString("Monday 18:00"));
        assertFalse(Time.isValidTimeOrEmptyString("25:00"));
    }
}
