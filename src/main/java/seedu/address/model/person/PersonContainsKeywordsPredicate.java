package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests whether a {@code Person} matches any keyword in the enabled fields.
 */
public class PersonContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;
    private final boolean searchName;
    private final boolean searchAddress;
    private final boolean searchPhone;
    // To be implemented
    private final boolean searchTag;

    public PersonContainsKeywordsPredicate(List<String> keywords,
                                           boolean searchName,
                                           boolean searchAddress,
                                           boolean searchPhone,
                                           boolean searchTag) {
        this.keywords = keywords;
        this.searchName = searchName;
        this.searchAddress = searchAddress;
        this.searchPhone = searchPhone;
        this.searchTag = searchTag;
    }

    @Override
    public boolean test(Person person) {
        String name = person.getName().fullName.toLowerCase();
        String address = person.getAddress().value.toLowerCase();
        String phone = person.getPhone().value;

        return keywords.stream().anyMatch(keyword -> {
            String lower = keyword.toLowerCase();
            return (searchName && name.contains(lower))
                    || (searchAddress && address.contains(lower))
                    || (searchPhone && phone.contains(keyword));
        });
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonContainsKeywordsPredicate)) {
            return false;
        }

        PersonContainsKeywordsPredicate otherPredicate = (PersonContainsKeywordsPredicate) other;
        return keywords.equals(otherPredicate.keywords)
                && searchName == otherPredicate.searchName
                && searchAddress == otherPredicate.searchAddress
                && searchPhone == otherPredicate.searchPhone
                && searchTag == otherPredicate.searchTag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
