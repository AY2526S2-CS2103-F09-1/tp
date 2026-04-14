package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests whether a {@code Person} matches any keyword in the enabled fields.
 */
public class PersonContainsKeywordsPredicate implements Predicate<Person> {
    private static final String EMPTY_STRING = "";

    private final List<String> nameKeywords;
    private final List<String> addressKeywords;
    private final List<String> phoneKeywords;
    private final List<String> tagKeywords;
    private final List<String> remarkKeywords;
    private final List<TimeSearchKeyword> dateTimeKeywords;
    private final MatchMode matchWord;

    /**
     * Determines how matches across different fields are combined.
     */
    public enum MatchMode {
        OR,
        AND
    }

    /**
     * Creates a predicate that searches the specified keywords in their respective
     * fields.
     */
    public PersonContainsKeywordsPredicate(List<String> nameKeywords,
            List<String> addressKeywords,
            List<String> phoneKeywords,
            List<String> tagKeywords,
            List<String> remarkKeywords,
            List<TimeSearchKeyword> dateTimeKeywords,
            MatchMode matchWord) {
        this.nameKeywords = nameKeywords;
        this.addressKeywords = addressKeywords;
        this.phoneKeywords = phoneKeywords;
        this.tagKeywords = tagKeywords;
        this.remarkKeywords = remarkKeywords;
        this.dateTimeKeywords = dateTimeKeywords;
        this.matchWord = matchWord;

        // Defensive programming
        requireNonNull(nameKeywords);
        requireNonNull(addressKeywords);
        requireNonNull(phoneKeywords);
        requireNonNull(tagKeywords);
        requireNonNull(remarkKeywords);
        requireNonNull(dateTimeKeywords);
        requireNonNull(matchWord);
    }

    @Override
    public boolean test(Person person) {
        String name = person.getName().fullName.toLowerCase();
        String address = person.getAddress()
                .map(addressObject -> addressObject.value.toLowerCase())
                .orElse(EMPTY_STRING);
        String phone = person.getPhone().map(phoneObj -> phoneObj.value).orElse(EMPTY_STRING);
        String remark = person.getRemark()
                .map(remarkValue -> remarkValue.value.toLowerCase())
                .orElse(EMPTY_STRING);

        boolean isAndMode = matchWord == MatchMode.AND;

        boolean matchesName = matchesKeywords(nameKeywords,
                keyword -> name.contains(keyword.toLowerCase()), isAndMode);

        boolean matchesAddress = matchesKeywords(addressKeywords,
                keyword -> address.contains(keyword.toLowerCase()), isAndMode);

        boolean matchesPhone = matchesKeywords(phoneKeywords, phone::contains, isAndMode);

        boolean matchesTag = matchesKeywords(tagKeywords,
                keyword -> person.getTags().stream()
                        .anyMatch(tag -> tag.tagName.toLowerCase().contains(keyword.toLowerCase())),
                isAndMode);

        boolean matchesRemark = matchesKeywords(remarkKeywords,
                keyword -> remark.contains(keyword.toLowerCase()), isAndMode);

        boolean matchesDateTime = matchesDateTimeKeywords(person, isAndMode);

        return switch (matchWord) {
        case OR -> matchesName || matchesAddress || matchesPhone || matchesTag || matchesRemark || matchesDateTime;
        case AND -> matchesName && matchesAddress && matchesPhone && matchesTag && matchesRemark && matchesDateTime;
        // defensive programming
        default -> throw new AssertionError("Unhandled match mode: " + matchWord);
        };
    }

    private boolean matchesKeywords(List<String> keywords, Predicate<String> matcher, boolean isAndMode) {
        return isAndMode
                ? keywords.stream().allMatch(matcher)
                : keywords.stream().anyMatch(matcher);
    }

    private boolean matchesDateTimeKeywords(Person person, boolean isAndMode) {
        if (dateTimeKeywords.isEmpty()) {
            return isAndMode;
        }

        if (person.getTime().isEmpty()) {
            return false;
        }

        Time personDateTime = person.getTime().get();

        // Parse the day and time.
        String personDay = personDateTime.getDayPart();
        String personTime = personDateTime.getTimePart();
        boolean isPersonTimeDuration = personTime.contains("-");

        for (TimeSearchKeyword dateTimeKeyword : dateTimeKeywords) {
            boolean matchesDate = matchesDay(personDay, dateTimeKeyword.day());
            boolean matchesTime = matchesTime(personTime, dateTimeKeyword.time(), isPersonTimeDuration);

            if (isAndMode) {
                if (!matchesDate || !matchesTime) {
                    return false;
                }
            } else if (matchesDate && matchesTime) {
                return true;
            }
        }

        return isAndMode;
    }

    private boolean matchesDay(String personDay, String queryDay) {
        return queryDay.isEmpty() || personDay.equalsIgnoreCase(queryDay);
    }

    private boolean matchesTime(String personTime, String queryTime, boolean isPersonTimeDuration) {
        if (queryTime.isEmpty()) {
            return true;
        }

        boolean isQueryTimeDuration = queryTime.contains("-");

        if (isQueryTimeDuration) {
            if (!isPersonTimeDuration) {
                return isTimeWithinDuration(personTime, queryTime);
            }

            // Duration queries must match the stored duration exactly.
            return personTime.equals(queryTime);
        }

        if (!isPersonTimeDuration) {
            return personTime.equals(queryTime);
        }

        return isTimeWithinDuration(queryTime, personTime);
    }

    private boolean isTimeWithinDuration(String time, String duration) {
        String[] durationParts = duration.split("\\s*-\\s*", 2);
        if (durationParts.length != 2) {
            return false;
        }

        int timeMinute = getMinuteOfDay(time);
        int startMinute = getMinuteOfDay(durationParts[0]);
        int endMinute = getMinuteOfDay(durationParts[1]);
        return startMinute < endMinute
                ? startMinute <= timeMinute && timeMinute <= endMinute
                : timeMinute >= startMinute || timeMinute <= endMinute;
    }

    private int getMinuteOfDay(String time) {
        return Integer.parseInt(time.substring(0, 2)) * 60 + Integer.parseInt(time.substring(3));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PersonContainsKeywordsPredicate)) {
            return false;
        }

        PersonContainsKeywordsPredicate otherPredicate = (PersonContainsKeywordsPredicate) other;
        return nameKeywords.equals(otherPredicate.nameKeywords)
                && addressKeywords.equals(otherPredicate.addressKeywords)
                && phoneKeywords.equals(otherPredicate.phoneKeywords)
                && tagKeywords.equals(otherPredicate.tagKeywords)
                && remarkKeywords.equals(otherPredicate.remarkKeywords)
                && matchWord == otherPredicate.matchWord
                && dateTimeKeywords.equals(otherPredicate.dateTimeKeywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nameKeywords", nameKeywords)
                .add("addressKeywords", addressKeywords)
                .add("phoneKeywords", phoneKeywords)
                .add("tagKeywords", tagKeywords)
                .add("remarkKeywords", remarkKeywords)
                .add("matchWord", matchWord)
                .add("dateTimeKeywords", dateTimeKeywords)
                .toString();
    }
}
