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
            MatchMode matchWord) {
        this.nameKeywords = nameKeywords;
        this.addressKeywords = addressKeywords;
        this.phoneKeywords = phoneKeywords;
        this.tagKeywords = tagKeywords;
        this.remarkKeywords = remarkKeywords;
        this.matchWord = matchWord;

        // Defensive Programming
        requireNonNull(nameKeywords);
        requireNonNull(addressKeywords);
        requireNonNull(phoneKeywords);
        requireNonNull(tagKeywords);
        requireNonNull(remarkKeywords);
        requireNonNull(matchWord);
    }

    @Override
    public boolean test(Person person) {
        String name = person.getName().fullName.toLowerCase();
        String address = person.getAddress().value.toLowerCase();
        String phone = person.getPhone().map(phoneObj -> phoneObj.value).orElse(EMPTY_STRING);
        String remark = person.getRemark()
                .map(remarkString -> remarkString.value)
                .map(remarkString -> remarkString.toLowerCase())
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

        boolean matchesRemark = matchesKeywords(remarkKeywords, remark::contains, isAndMode);

        return switch (matchWord) {
            case OR -> matchesName || matchesAddress || matchesPhone || matchesTag || matchesRemark;
            case AND -> matchesName && matchesAddress && matchesPhone && matchesTag && matchesRemark;
        };
    }

    private boolean matchesKeywords(List<String> keywords, Predicate<String> matcher, boolean isAndMode) {
        return isAndMode
                ? keywords.stream().allMatch(matcher)
                : keywords.stream().anyMatch(matcher);
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
                && matchWord == otherPredicate.matchWord;
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
                .toString();
    }
}
