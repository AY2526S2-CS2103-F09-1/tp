package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's online meeting link (e.g. Zoom) in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidMeetingLink(String)}
 */
public class MeetingLink {

    public static final String MESSAGE_CONSTRAINTS =
            "Meeting link must be a valid URL starting with http:// or https://";

    public static final String VALIDATION_REGEX = "https?://\\S+";

    private static final String EMPTY_STRING = "";

    public final String value;

    /**
     * Constructs a {@code MeetingLink}.
     *
     * @param link A valid meeting link URL.
     */
    public MeetingLink(String link) {
        requireNonNull(link);
        checkArgument(isValidMeetingLink(link), MESSAGE_CONSTRAINTS);
        value = link;
    }

    /**
     * Returns true if a given string is a valid meeting link.
     */
    public static boolean isValidMeetingLink(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if given string is a valid meeting link or is empty.
     * Used for loading from the JSON storage file.
     */
    public static boolean isValidMeetingLinkOrEmptyString(String link) {
        return link.equals(EMPTY_STRING) || isValidMeetingLink(link);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof MeetingLink)) {
            return false;
        }

        MeetingLink otherLink = (MeetingLink) other;
        return value.equals(otherLink.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
