package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class MeetingLinkTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new MeetingLink(null));
    }

    @Test
    public void constructor_invalidMeetingLink_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new MeetingLink("not-a-url"));
        assertThrows(IllegalArgumentException.class, () -> new MeetingLink(""));
        assertThrows(IllegalArgumentException.class, () -> new MeetingLink("ftp://wrong-scheme.com"));
    }

    @Test
    public void isValidMeetingLink() {
        // null
        assertThrows(NullPointerException.class, () -> MeetingLink.isValidMeetingLink(null));

        // invalid links
        assertFalse(MeetingLink.isValidMeetingLink("")); // empty string
        assertFalse(MeetingLink.isValidMeetingLink("zoom.us/123123")); // missing scheme
        assertFalse(MeetingLink.isValidMeetingLink("asfdkjl;//zoom.us/j/123")); // wrong format
        assertFalse(MeetingLink.isValidMeetingLink("not a url"));

        // valid links
        assertTrue(MeetingLink.isValidMeetingLink("https://zoom.us/dontwanttoedittoomuch")); // https
        assertTrue(MeetingLink.isValidMeetingLink("http://meet.google.com/pleasework")); // http
        assertTrue(MeetingLink.isValidMeetingLink("https://teams.microsoft.com/verycool"));
    }

    @Test
    public void isValidMeetingLinkOrEmptyString() {
        // empty string treated as valid (represents absent meeting link in JSON)
        assertTrue(MeetingLink.isValidMeetingLinkOrEmptyString(""));

        // valid meeting link
        assertTrue(MeetingLink.isValidMeetingLinkOrEmptyString("https://zoom.us/123123"));

        // invalid (non-empty, non-URL)
        assertFalse(MeetingLink.isValidMeetingLinkOrEmptyString("notrealurllol"));
        assertFalse(MeetingLink.isValidMeetingLinkOrEmptyString("nothttps://"));
    }

    @Test
    public void equals() {
        MeetingLink link = new MeetingLink("https://zoom.us/6767");

        // same values -> returns true
        assertTrue(link.equals(new MeetingLink("https://zoom.us/6767")));

        // same object -> returns true
        assertTrue(link.equals(link));

        // null -> returns false
        assertFalse(link.equals(null));

        // different type -> returns false
        assertFalse(link.equals("https://zoom.us/notevenameetinglink"));

        // different value -> returns false
        assertFalse(link.equals(new MeetingLink("https://zoom.us/definitelydifferent")));
    }

    @Test
    public void constructor_validInput_storesCorrectValue() {
        String validLink = "https://zoom.us/shouldbecorrect";
        assertTrue(new MeetingLink(validLink).value.equals(validLink));
    }
}
