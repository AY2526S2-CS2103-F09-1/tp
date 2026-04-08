package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Id;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

public class SampleDataUtilTest {

    @Test
    public void getSamplePersons_returnsExpectedPersons() {
        Person[] samplePersons = SampleDataUtil.getSamplePersons();

        assertEquals(6, samplePersons.length);
        assertSamplePerson(samplePersons[0], 1, "Student", "Freshman");
        assertSamplePerson(samplePersons[1], 2, "Student", "Sophomore");
        assertSamplePerson(samplePersons[2], 3, "Tutor", "Graduate");
        assertSamplePerson(samplePersons[3], 4, "Student", "Junior");
        assertSamplePerson(samplePersons[4], 5, "Parent");
        assertSamplePerson(samplePersons[5], 6, "Student", "Senior");
    }

    @Test
    public void getSampleAddressBook_returnsAddressBookWithSamplePersons() {
        ReadOnlyAddressBook sampleAddressBook = SampleDataUtil.getSampleAddressBook();

        List<Person> samplePersons = sampleAddressBook.getPersonList();
        assertEquals(6, samplePersons.size());
        assertSamplePerson(samplePersons.get(0), 1, "Student", "Freshman");
        assertSamplePerson(samplePersons.get(5), 6, "Student", "Senior");
    }

    @Test
    public void getTagSet_validTags_returnsExpectedTags() {
        Set<Tag> tags = SampleDataUtil.getTagSet("Student", "Graduate");

        assertEquals(Set.of(new Tag("Student"), new Tag("Graduate")), tags);
    }

    private void assertSamplePerson(Person person, int expectedId, String... expectedTagNames) {
        assertEquals(Id.of(expectedId), person.getId());
        assertTrue(person.getPhone().isPresent());
        Set<Tag> expectedTags = SampleDataUtil.getTagSet(expectedTagNames);
        assertEquals(expectedTags, person.getTags());
    }
}
