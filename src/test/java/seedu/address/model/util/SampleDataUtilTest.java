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
        assertSamplePerson(samplePersons[0], 1, "Student");
        assertSamplePerson(samplePersons[1], 2, "Parent");
        assertSamplePerson(samplePersons[2], 3, "Tutor");
        assertSamplePerson(samplePersons[3], 4, "Student");
        assertSamplePerson(samplePersons[4], 5, "Parent");
        assertSamplePerson(samplePersons[5], 6, "Tutor");
    }

    @Test
    public void getSampleAddressBook_returnsAddressBookWithSamplePersons() {
        ReadOnlyAddressBook sampleAddressBook = SampleDataUtil.getSampleAddressBook();

        List<Person> samplePersons = sampleAddressBook.getPersonList();
        assertEquals(6, samplePersons.size());
        assertSamplePerson(samplePersons.get(0), 1, "Student");
        assertSamplePerson(samplePersons.get(5), 6, "Tutor");
    }

    @Test
    public void getTagSet_validCategories_returnsExpectedTags() {
        Set<Tag> tags = SampleDataUtil.getTagSet("Student", "Parent");

        assertEquals(Set.of(new Tag("Student"), new Tag("Parent")), tags);
    }

    private void assertSamplePerson(Person person, int expectedId, String expectedTagName) {
        assertEquals(Id.of(expectedId), person.getId());
        assertTrue(person.getPhone().isPresent());
        assertEquals(Set.of(new Tag(expectedTagName)), person.getTags());
    }
}
