package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

/**
 * Represents a Person's time in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTime(String)}
 */
public class Time {

    public static final String MESSAGE_CONSTRAINTS =
            "Times should be in the format HH:mm or HHmm. Durations should be in the format "
                    + "HH:mm - HH:mm or HHmm - HHmm. All values must be valid 24-hour times, "
                    + "and a duration must not end before it starts";
    private static final String EMPTY_STRING = "";
    private static final String DURATION_SEPARATOR = " - ";
    private static final String DURATION_SPLIT_REGEX = "\\s*-\\s*";
    private static final DateTimeFormatter STORAGE_TIME_FORMATTER = formatter("HH:mm");
    private static final List<DateTimeFormatter> USER_TIME_FORMATTERS = List.of(
            formatter("HH:mm"),
            formatter("HHmm"));

    public final String value;

    /**
     * Constructs a {@code Time}.
     *
     * @param time A valid time.
     */
    public Time(String time) {
        requireNonNull(time);
        String canonicalValue = getCanonicalValue(time);
        checkArgument(canonicalValue != null, MESSAGE_CONSTRAINTS);
        value = canonicalValue;
    }

    private static DateTimeFormatter formatter(String pattern) {
        return DateTimeFormatter.ofPattern(pattern).withResolverStyle(ResolverStyle.STRICT);
    }

    /**
     * Returns true if a given string is a valid time.
     */
    public static boolean isValidTime(String test) {
        requireNonNull(test);
        return getCanonicalValue(test) != null;
    }

    private static String getCanonicalValue(String time) {
        String trimmedTime = time.trim();
        if (trimmedTime.isEmpty()) {
            return null;
        }

        if (!trimmedTime.contains("-")) {
            return getCanonicalSingleTime(trimmedTime);
        }

        return getCanonicalDuration(trimmedTime);
    }

    private static String getCanonicalSingleTime(String time) {
        LocalTime parsedTime = parseSingleTime(time);
        if (parsedTime == null) {
            return null;
        }

        return formatTime(parsedTime);
    }

    private static String getCanonicalDuration(String duration) {
        String[] durationParts = duration.split(DURATION_SPLIT_REGEX, -1);
        if (durationParts.length != 2) {
            return null;
        }

        String startTimeString = durationParts[0].trim();
        String endTimeString = durationParts[1].trim();
        if (!isMatchingTimeFormat(startTimeString, endTimeString)) {
            return null;
        }

        LocalTime startTime = parseSingleTime(startTimeString);
        LocalTime endTime = parseSingleTime(endTimeString);
        if (startTime == null || endTime == null || endTime.isBefore(startTime)) {
            return null;
        }

        return formatTime(startTime) + DURATION_SEPARATOR + formatTime(endTime);
    }

    private static boolean isMatchingTimeFormat(String startTime, String endTime) {
        boolean startUsesColon = startTime.contains(":");
        boolean endUsesColon = endTime.contains(":");
        return startUsesColon == endUsesColon;
    }

    private static LocalTime parseSingleTime(String time) {
        String trimmedTime = time.trim();

        for (DateTimeFormatter formatter : USER_TIME_FORMATTERS) {
            try {
                return LocalTime.parse(trimmedTime, formatter);
            } catch (DateTimeParseException ignored) {
                // Try next format.
            }
        }

        return null;
    }

    private static String formatTime(LocalTime time) {
        return time.format(STORAGE_TIME_FORMATTER);
    }

    /**
     * Returns the display value of the time.
     */
    public String getDisplayValue() {
        return value;
    }

    /**
     * Returns true if given string is a valid time or is empty.
     * Used for loading from the JSON storage file.
     *
     * @param time time given to check for validity.
     * @return true if valid time or empty, else false.
     */
    public static boolean isValidTimeOrEmptyString(String time) {
        requireNonNull(time);
        return time.equals(EMPTY_STRING) || isValidTime(time);
    }

    @Override
    public String toString() {
        return getDisplayValue();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Time)) {
            return false;
        }

        Time otherTime = (Time) other;
        return value.equals(otherTime.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
