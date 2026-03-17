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

        PersonContainsKeywordsPredicate firstPredicate = new PersonContainsKeywordsPredicate(firstPredicateKeywordList, true, true, true, true, false);
        PersonContainsKeywordsPredicate secondPredicate = new PersonContainsKeywordsPredicate(secondPredicateKeywordList, true, true, true, true, false);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PersonContainsKeywordsPredicate firstPredicateCopy = new PersonContainsKeywordsPredicate(firstPredicateKeywordList, true, true, true, true, false);
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
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("Alice"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Multiple keywords
        predicate = new PersonContainsKeywordsPredicate(Arrays.asList("Alice", "Bob"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Only one matching keyword
        predicate = new PersonContainsKeywordsPredicate(Arrays.asList("Bob", "Carol"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Carol").build()));

        // Mixed-case keywords
        predicate = new PersonContainsKeywordsPredicate(Arrays.asList("aLIce", "bOB"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Address keywords
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("Clementi"));
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withAddress("123 Clementi Road")
                .build()));

        // Number keywords
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("9435"));
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withPhone("94351253")
                .build()));

        // Partial keywords
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("Clem"));
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withAddress("123 Clementi Road")
                .build()));

        // Prefix name keywords
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("/n Alice"));
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice")
                .withAddress("123 Clementi Road")
                .build()));

        // Prefix address keywords
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("/a Clementi"));
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withAddress("123 Clementi Road")
                .build()));

        // Prefix number keywords
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("/p 9435"));
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withPhone("94351253")
                .build()));
    }

    @Test
    public void test_personDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Non-matching keyword
        predicate = new PersonContainsKeywordsPredicate(Arrays.asList("Carol"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Non-matching keyword2
        predicate = new PersonContainsKeywordsPredicate(Arrays.asList("zzz"));
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withPhone("94351253")
                .withAddress("123 Clementi Road")
                .build()));

        // Prefixes mixed with non-prefix
        predicate = new PersonContainsKeywordsPredicate(Arrays.asList("clementi /n Alice"));
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withPhone("94351253")
                .withAddress("123 Clementi Road")
                .build()));

        // Only Search Prefix name
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("/n Clement"));
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice")
                .withAddress("123 Clementi Road")
                .build()));
        
        // Only Search Phone number
        predicate = new PersonContainsKeywordsPredicate(Arrays.asList("/p 123"));
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withPhone("94357687")
                .withAddress("123 Clementi Road")
                .build()));
        
        // Only Search Address
        predicate = new PersonContainsKeywordsPredicate(Arrays.asList("/a 123"));
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withPhone("94357123")
                .withAddress("64 Clementi Road")
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
