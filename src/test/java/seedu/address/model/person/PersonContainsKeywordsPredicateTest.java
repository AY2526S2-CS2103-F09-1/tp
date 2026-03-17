package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        PersonContainsKeywordsPredicate firstPredicate = new PersonContainsKeywordsPredicate(firstPredicateKeywordList,
                true, true, true, false);
        PersonContainsKeywordsPredicate secondPredicate = new PersonContainsKeywordsPredicate(secondPredicateKeywordList,
                true, true, true, false);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PersonContainsKeywordsPredicate firstPredicateCopy = new PersonContainsKeywordsPredicate(firstPredicateKeywordList, true, true, true, false);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_personContainsKeywords_returnsTrue() {
        // One keyword
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList("Alice"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Multiple keywords
        predicate = new PersonContainsKeywordsPredicate(Arrays.asList("Alice", "Bob"),
        true, true, true, false);
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Only one matching keyword
        predicate = new PersonContainsKeywordsPredicate(Arrays.asList("Bob", "Carol"),
        true, true, true, false);
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Carol").build()));

        // Mixed-case keywords
        predicate = new PersonContainsKeywordsPredicate(Arrays.asList("aLIce", "bOB"),
        true, true, true, false);
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Address keyword
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("Clementi"),
                false, true, false, false);
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withAddress("123 Clementi Road")
                .build()));

        // Phone keyword
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("9435"),
                false, false, true, false);
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withPhone("94351253")
                .build()));

        // Partial keyword
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("Clem"),
                false, true, false, false);
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withAddress("123 Clementi Road")
                .build()));

        // Multiple enabled fields
        predicate = new PersonContainsKeywordsPredicate(Arrays.asList("Alice", "9435"),
                true, false, true, false);
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withPhone("94351253")
                .build()));
    }

@Test
public void test_personDoesNotContainKeywords_returnsFalse() {
    // Zero keywords
    PersonContainsKeywordsPredicate predicate =
            new PersonContainsKeywordsPredicate(Collections.emptyList(),
                    true, true, true, false);
    assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

    // Name search enabled, but keyword only appears in address
    predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("Clementi"),
            true, false, false, false);
    assertFalse(predicate.test(new PersonBuilder()
            .withName("Alice Bob")
            .withAddress("123 Clementi Road")
            .build()));

    // Phone search enabled, but number does not match
    predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("9999"),
            false, false, true, false);
    assertFalse(predicate.test(new PersonBuilder()
            .withName("Alice Bob")
            .withPhone("94351253")
            .build()));

    // Address search enabled, but keyword does not match
    predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("Bedok"),
            false, true, false, false);
    assertFalse(predicate.test(new PersonBuilder()
            .withName("Alice Bob")
            .withAddress("123 Clementi Road")
            .build()));

    // No enabled field contains the keyword
    predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("zzz"),
            true, true, true, false);
    assertFalse(predicate.test(new PersonBuilder()
            .withName("Alice Bob")
            .withPhone("94351253")
            .withAddress("123 Clementi Road")
            .build()));
}

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(keywords);

        String expected = PersonContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
