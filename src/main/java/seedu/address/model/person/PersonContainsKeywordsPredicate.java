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

        boolean matchesName = isAndMode
                ? nameKeywords.stream().allMatch(keyword -> name.contains(keyword.toLowerCase()))
                : nameKeywords.stream().anyMatch(keyword -> name.contains(keyword.toLowerCase()));

        boolean matchesAddress = isAndMode
                ? addressKeywords.stream().allMatch(keyword -> address.contains(keyword.toLowerCase()))
                : addressKeywords.stream().anyMatch(keyword -> address.contains(keyword.toLowerCase()));

        boolean matchesPhone = isAndMode
                ? phoneKeywords.stream().allMatch(phone::contains)
                : phoneKeywords.stream().anyMatch(phone::contains);

        boolean matchesTag = isAndMode
                ? tagKeywords.stream().allMatch(keyword -> person.getTags().stream()
                        .anyMatch(tag -> tag.tagName.toLowerCase().contains(keyword.toLowerCase())))
                : tagKeywords.stream().anyMatch(keyword -> person.getTags().stream()
                        .anyMatch(tag -> tag.tagName.toLowerCase().contains(keyword.toLowerCase())));

        boolean matchesRemark = remarkKeywords.stream()
                .anyMatch(remark::contains);

        if (matchWord == MatchMode.OR) {
            return matchesName || matchesAddress || matchesPhone || matchesTag || matchesRemark;
        } else if (matchWord == MatchMode.AND) {
            boolean nameOk = nameKeywords.isEmpty() || matchesName;
            boolean addressOk = addressKeywords.isEmpty() || matchesAddress;
            boolean phoneOk = phoneKeywords.isEmpty() || matchesPhone;
            boolean tagOk = tagKeywords.isEmpty() || matchesTag;
            return nameOk && addressOk && phoneOk && tagOk;
        } else {
            throw new AssertionError("Unhandled match mode: " + matchWord);
        }
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
