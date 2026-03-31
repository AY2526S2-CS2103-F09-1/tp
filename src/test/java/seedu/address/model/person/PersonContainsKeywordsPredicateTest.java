package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.PersonContainsKeywordsPredicate.MatchMode;
import seedu.address.testutil.PersonBuilder;

public class PersonContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        PersonContainsKeywordsPredicate firstPredicate = namePredicate(firstPredicateKeywordList);
        PersonContainsKeywordsPredicate secondPredicate = namePredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PersonContainsKeywordsPredicate firstPredicateCopy = namePredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different keywords -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));

        PersonContainsKeywordsPredicate differentFlagsPredicate = addressPredicate(firstPredicateKeywordList);
        assertFalse(firstPredicate.equals(differentFlagsPredicate));

        PersonContainsKeywordsPredicate differentNameFlag = addressPredicate(firstPredicateKeywordList);
        assertFalse(firstPredicate.equals(differentNameFlag));

        PersonContainsKeywordsPredicate differentAddressFlag = phonePredicate(firstPredicateKeywordList);
        assertFalse(firstPredicate.equals(differentAddressFlag));

        PersonContainsKeywordsPredicate differentPhoneFlag = tagPredicate(firstPredicateKeywordList);
        assertFalse(firstPredicate.equals(differentPhoneFlag));

        PersonContainsKeywordsPredicate differentTagFlag = new PersonContainsKeywordsPredicate(
                firstPredicateKeywordList, Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), MatchMode.AND);
        assertFalse(firstPredicate.equals(differentTagFlag));
    }

    @Test
    public void test_personContainsKeywords_returnsTrue() {
        // One keyword
        PersonContainsKeywordsPredicate predicate = namePredicate(Collections.singletonList("Alice"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Multiple keywords
        predicate = namePredicate(Arrays.asList("Alice", "Bob"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Only one matching keyword
        predicate = namePredicate(Arrays.asList("Bob", "Carol"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Carol").build()));

        // Mixed-case keywords
        predicate = namePredicate(Arrays.asList("aLIce", "bOB"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Address keyword
        predicate = addressPredicate(Collections.singletonList("Clementi"));
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withAddress("123 Clementi Road")
                .build()));

        // Phone keyword
        predicate = phonePredicate(Collections.singletonList("9435"));
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withPhone("94351253")
                .build()));

        // Partial keyword
        predicate = addressPredicate(Collections.singletonList("Clem"));
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withAddress("123 Clementi Road")
                .build()));

        // Multiple enabled fields
        predicate = predicate(Arrays.asList("Alice"), Collections.emptyList(),
                Collections.singletonList("9435"), Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withPhone("94351253")
                .build()));

        // Tag keyword
        predicate = tagPredicate(Collections.singletonList("Student"));
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withTags("Student")
                .build()));

        // Case-insensitive tag keyword
        predicate = tagPredicate(Collections.singletonList("sTuDeNt"));
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withTags("Student")
                .build()));

        // OR semantics across fields
        predicate = predicate(Collections.singletonList("zzz"), Collections.singletonList("Clementi"),
                Collections.emptyList(), Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withAddress("123 Clementi Road")
                .build()));
    }

    @Test
    public void test_personDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        PersonContainsKeywordsPredicate predicate = predicate(Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Name search enabled, but keyword only appears in address
        predicate = namePredicate(Collections.singletonList("Clementi"));
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withAddress("123 Clementi Road")
                .build()));

        // Field-specific keywords should not match the wrong field
        predicate = predicate(Collections.singletonList("Geylang"), Collections.singletonList("Anything"),
                Collections.emptyList(), Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withAddress("123 Geylang Road")
                .build()));

        // Address and phone keywords should not match each other's fields
        predicate = predicate(Collections.emptyList(), Collections.singletonList("Anything"),
                Collections.singletonList("123"), Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withAddress("123 Geylang Road")
                .withPhone("56788765")
                .build()));

        // Name and tag keywords should not match each other's fields
        predicate = predicate(Collections.singletonList("Student"), Collections.emptyList(),
                Collections.emptyList(), Collections.singletonList("Alice"));
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withTags("Student")
                .build()));

        // Phone keyword should not match address
        predicate = predicate(Collections.emptyList(), Collections.emptyList(),
                Collections.singletonList("Clementi"), Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withAddress("123 Clementi Road")
                .withPhone("94351253")
                .build()));

        // Tag keyword should not match address
        predicate = predicate(Collections.emptyList(), Collections.singletonList("zzz"),
                Collections.emptyList(), Collections.singletonList("Clementi"));
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withAddress("123 Clementi Road")
                .build()));

        // Phone search enabled, but number does not match
        predicate = phonePredicate(Collections.singletonList("9999"));
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withPhone("94351253")
                .build()));

        // Address search enabled, but keyword does not match
        predicate = addressPredicate(Collections.singletonList("Bedok"));
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withAddress("123 Clementi Road")
                .build()));

        // Tag search enabled, but no tag matches
        predicate = tagPredicate(Collections.singletonList("enemy"));
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withTags("Student", "Parent")
                .build()));

        // Tag search enabled, one of multiple tags matches
        predicate = tagPredicate(Collections.singletonList("stu"));
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withTags("Student", "Parent")
                .build()));

        // Tag search enabled, but person has no tags
        predicate = tagPredicate(Collections.singletonList("Student"));
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .build()));

        // No enabled field contains the keyword
        predicate = predicate(Collections.singletonList("zzz"), Collections.singletonList("zzz"),
                Collections.singletonList("zzz"), Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withPhone("94351253")
                .withAddress("123 Clementi Road")
                .build()));

        // Name search disabled
        predicate = predicate(Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .build()));

        // Address search disabled
        predicate = predicate(Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withAddress("123 Clementi Road")
                .build()));

        // Phone search disabled
        predicate = predicate(Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withPhone("94351253")
                .build()));

        // Tag search disabled
        predicate = namePredicate(Collections.singletonList("Student"));
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withTags("Student")
                .build()));
    }

    @Test
    public void test_andMode_requiresAllKeywordsWithinEnabledField() {
        PersonContainsKeywordsPredicate predicate = predicate(Arrays.asList("Ali", "Lim"),
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), MatchMode.AND);

        assertTrue(predicate.test(new PersonBuilder().withName("Ali Lim").build()));
        assertFalse(predicate.test(new PersonBuilder().withName("Ali Tan").build()));
    }

    @Test
    public void test_andMode_requiresAllEnabledFields() {
        PersonContainsKeywordsPredicate predicate = predicate(Collections.singletonList("Ali"),
                Collections.emptyList(), Collections.singletonList("345"),
                Collections.emptyList(), MatchMode.AND);

        assertTrue(predicate.test(new PersonBuilder().withName("Ali Lim").withPhone("12345678").build()));
        assertFalse(predicate.test(new PersonBuilder().withName("Ali Lim").withPhone("99999999").build()));
    }

    @Test
    public void test_andMode_requiresAllEnabledFieldsIncludingTags() {
        PersonContainsKeywordsPredicate predicate = predicate(Collections.singletonList("Alice"),
                Collections.singletonList("Clementi"), Collections.singletonList("9435"),
                Collections.singletonList("Student"), MatchMode.AND);

        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withAddress("123 Clementi Road")
                .withPhone("94351253")
                .withTags("Student")
                .build()));

        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withAddress("123 Clementi Road")
                .withPhone("94351253")
                .withTags("Parent")
                .build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        PersonContainsKeywordsPredicate predicate = namePredicate(keywords);

        String expected = PersonContainsKeywordsPredicate.class.getCanonicalName()
                + "{nameKeywords=" + keywords + ", addressKeywords=[], phoneKeywords=[], "
                + "tagKeywords=[], matchWord=OR}";
        assertEquals(expected, predicate.toString());
    }

    private PersonContainsKeywordsPredicate predicate(List<String> nameKeywords, List<String> addressKeywords,
            List<String> phoneKeywords, List<String> tagKeywords) {
        return predicate(nameKeywords, addressKeywords, phoneKeywords, tagKeywords, MatchMode.OR);
    }

    private PersonContainsKeywordsPredicate predicate(List<String> nameKeywords, List<String> addressKeywords,
            List<String> phoneKeywords, List<String> tagKeywords, MatchMode matchMode) {
        return new PersonContainsKeywordsPredicate(nameKeywords, addressKeywords, phoneKeywords, tagKeywords,
                matchMode);
    }

    private PersonContainsKeywordsPredicate namePredicate(List<String> keywords) {
        return predicate(keywords, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    private PersonContainsKeywordsPredicate addressPredicate(List<String> keywords) {
        return predicate(Collections.emptyList(), keywords, Collections.emptyList(), Collections.emptyList());
    }

    private PersonContainsKeywordsPredicate phonePredicate(List<String> keywords) {
        return predicate(Collections.emptyList(), Collections.emptyList(), keywords, Collections.emptyList());
    }

    private PersonContainsKeywordsPredicate tagPredicate(List<String> keywords) {
        return predicate(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), keywords);
    }
}
