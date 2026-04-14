package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

/**
 * Represents a Person's scheduled day and time in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTime(String)}
 */
public class Time {

    public static final String MESSAGE_CONSTRAINTS =
            "Invalid weekly timeslot format.\n"
                    + "Use: Day HH:mm, Day HHmm, Day HH:mm - HH:mm, or Day HHmm - HHmm.\n"
                    + "Day must begin with at least the first two letters of a valid day name.\n"
                    + "Time must use valid 24-hour values.\n"
                    + "For durations, the end time must not be earlier than the start time.";
    private static final String EMPTY_STRING = "";
    private static final String DAY_TIME_SEPARATOR = " ";
    private static final String DURATION_SEPARATOR = " - ";
    private static final String DURATION_SPLIT_REGEX = "\\s*-\\s*";
    private static final DateTimeFormatter STORAGE_TIME_FORMATTER = formatter("HH:mm");
    private static final List<DateTimeFormatter> USER_TIME_FORMATTERS = List.of(
            formatter("HH:mm"),
            formatter("HHmm"));

    public final String value;

    private enum ValidationMode {
        USER_INPUT,
        STORAGE
    }

    /**
     * Constructs a {@code Time}.
     *
     * @param time A valid time.
     */
    public Time(String time) {
        requireNonNull(time);
        String canonicalValue = getCanonicalValue(time, ValidationMode.USER_INPUT);
        checkArgument(canonicalValue != null, MESSAGE_CONSTRAINTS);
        value = canonicalValue;
    }

    private Time(String canonicalValue, boolean isCanonicalValue) {
        assert isCanonicalValue : "This constructor should only be used with canonical values.";
        value = canonicalValue;
    }

    private static DateTimeFormatter formatter(String pattern) {
        return DateTimeFormatter.ofPattern(pattern).withResolverStyle(ResolverStyle.STRICT);
    }

    /**
     * Returns true if a given string is a valid weekday time slot for user input.
     */
    public static boolean isValidTime(String test) {
        requireNonNull(test);
        return getCanonicalValue(test, ValidationMode.USER_INPUT) != null;
    }

    /**
     * Returns a {@code Time} built from the stored value in the data file.
     * Legacy time-only values remain supported for backward compatibility.
     */
    public static Time fromStoredValue(String storedTime) {
        requireNonNull(storedTime);
        String canonicalValue = getCanonicalValue(storedTime, ValidationMode.STORAGE);
        checkArgument(canonicalValue != null, MESSAGE_CONSTRAINTS);
        return new Time(canonicalValue, true);
    }

    private static String getCanonicalValue(String time, ValidationMode validationMode) {
        String trimmedTime = time.trim();
        if (trimmedTime.isEmpty()) {
            return null;
        }

        String canonicalDayTime = getCanonicalDayTime(trimmedTime);
        if (canonicalDayTime != null) {
            return canonicalDayTime;
        }

        if (validationMode == ValidationMode.STORAGE) {
            return getCanonicalLegacyTime(trimmedTime);
        }

        return null;
    }

    private static String getCanonicalDayTime(String rawValue) {
        String[] dayAndTimeParts = rawValue.split("\\s+", 2);
        if (dayAndTimeParts.length != 2) {
            return null;
        }

        DayOfWeek dayOfWeek = parseDayOfWeek(dayAndTimeParts[0]);
        if (dayOfWeek == null) {
            return null;
        }

        String canonicalTime = getCanonicalLegacyTime(dayAndTimeParts[1]);
        if (canonicalTime == null) {
            return null;
        }

        return formatDayOfWeek(dayOfWeek) + DAY_TIME_SEPARATOR + canonicalTime;
    }

    /**
     * Returns the canonical full weekday name for a day-prefix query, or {@code null} if invalid/ambiguous.
     */
    public static String getCanonicalDayQuery(String dayToken) {
        requireNonNull(dayToken);
        String trimmedDayToken = dayToken.trim();
        // d/t OR d/s are not allowed
        if (trimmedDayToken.isEmpty() || trimmedDayToken.length() <= 1) {
            return null;
        }

        DayOfWeek matchedDayOfWeek = null;
        String lowerCaseDayToken = trimmedDayToken.toLowerCase();
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            String lowerCaseDay = dayOfWeek.name().toLowerCase();
            if (!lowerCaseDay.startsWith(lowerCaseDayToken)) {
                continue;
            }
            if (matchedDayOfWeek != null) {
                return null;
            }
            matchedDayOfWeek = dayOfWeek;
        }

        return matchedDayOfWeek == null ? null : formatDayOfWeek(matchedDayOfWeek);
    }

    private static DayOfWeek parseDayOfWeek(String dayToken) {
        String canonicalDay = getCanonicalDayQuery(dayToken);
        if (canonicalDay == null) {
            return null;
        }

        return DayOfWeek.valueOf(canonicalDay.toUpperCase());
    }

    private static String formatDayOfWeek(DayOfWeek dayOfWeek) {
        String lowerCaseDay = dayOfWeek.name().toLowerCase();
        return Character.toUpperCase(lowerCaseDay.charAt(0)) + lowerCaseDay.substring(1);
    }

    public static String getCanonicalLegacyTime(String time) {
        String trimmedTime = time.trim();
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
            LocalTime parsedTime = parseSingleTimeWithFormatter(trimmedTime, formatter);
            if (parsedTime != null) {
                return parsedTime;
            }
        }

        return null;
    }

    private static LocalTime parseSingleTimeWithFormatter(String time, DateTimeFormatter formatter) {
        try {
            return LocalTime.parse(time, formatter);
        } catch (DateTimeParseException dateTimeParseException) {
            return null;
        }
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
     * Returns the weekday portion of this time value.
     */
    public String getDayPart() {
        String[] dayAndTimeParts = value.split("\\s+", 2);
        assert dayAndTimeParts.length == 2 : "Time value should contain both day and time: " + value;
        return dayAndTimeParts[0];
    }

    /**
     * Returns the time portion of this time value.
     */
    public String getTimePart() {
        String[] dayAndTimeParts = value.split("\\s+", 2);
        assert dayAndTimeParts.length == 2 : "Time value should contain both day and time: " + value;
        return dayAndTimeParts[1];
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
        return time.equals(EMPTY_STRING) || getCanonicalValue(time, ValidationMode.STORAGE) != null;
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
