package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests whether a {@code Person} matches any keyword in the enabled fields.
 */
public class PersonContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> nameKeywords;
    private final List<String> addressKeywords;
    private final List<String> phoneKeywords;
    private final List<String> tagKeywords;
    private final MatchMode matchWord;

    /**
     * Determines how matches across different fields are combined.
     */
    public enum MatchMode {
        OR,
        AND
    }

    /**
     * Creates a predicate that searches the specified keywords in their respective fields.
     */
    public PersonContainsKeywordsPredicate(List<String> nameKeywords,
            List<String> addressKeywords,
            List<String> phoneKeywords,
            List<String> tagKeywords,
            MatchMode matchWord) {
        this.nameKeywords = nameKeywords;
        this.addressKeywords = addressKeywords;
        this.phoneKeywords = phoneKeywords;
        this.tagKeywords = tagKeywords;
        // To be implemented
        this.matchWord = matchWord;
    }

    @Override
    public boolean test(Person person) {
        String name = person.getName().fullName.toLowerCase();
        String address = person.getAddress().value.toLowerCase();
        String phone = person.getPhone().map(phoneObj -> phoneObj.value).orElse("");

        boolean matchesName = nameKeywords.stream()
                .anyMatch(keyword -> name.contains(keyword.toLowerCase()));

        boolean matchesAddress = addressKeywords.stream()
                .anyMatch(keyword -> address.contains(keyword.toLowerCase()));

        boolean matchesPhone = phoneKeywords.stream()
                .anyMatch(phone::contains);

        boolean matchesTag = tagKeywords.stream()
                .anyMatch(keyword -> person.getTags().stream()
                        .anyMatch(tag -> tag.tagName.toLowerCase().contains(keyword.toLowerCase())));

        // Currently only OR semantics are supported.
        return matchesName || matchesAddress || matchesPhone || matchesTag;
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
                && matchWord == otherPredicate.matchWord;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nameKeywords", nameKeywords)
                .add("addressKeywords", addressKeywords)
                .add("phoneKeywords", phoneKeywords)
                .add("tagKeywords", tagKeywords)
                .add("matchWord", matchWord)
                .toString();
    }
}
