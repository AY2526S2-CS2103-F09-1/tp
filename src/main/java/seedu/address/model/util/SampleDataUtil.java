package seedu.address.model.util;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Id;
import seedu.address.model.person.MeetingLink;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Time;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(Id.of(1), new Name("Alex Yeoh"), Optional.of(new Phone("87438807")),
                Optional.of(new Address("Blk 30 Geylang Street 29, #06-40")),
                Optional.of(new Time("Sunday 10:00")), getTagSet("Student", "Freshman"),
                Optional.of(new Remark("First student")),
                Optional.of(new MeetingLink("https://www.zoom.com/676767"))),
            new Person(Id.of(2), new Name("Bernice Yu"), Optional.of(new Phone("99272758")),
                Optional.of(new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18")),
                Optional.<Time>empty(), getTagSet("Student", "Sophomore"), Optional.empty(),
                Optional.of(new MeetingLink("https://www.zoom.com/123"))),
            new Person(Id.of(3), new Name("Charlotte Oliveiro"), Optional.of(new Phone("93210283")),
                Optional.of(new Address("Blk 11 Ang Mo Kio Street 74, #11-04")),
                Optional.<Time>empty(), getTagSet("Tutor", "Graduate"), Optional.empty(),
                Optional.empty()),
            new Person(Id.of(4), new Name("David Li"), Optional.of(new Phone("91031282")),
                Optional.of(new Address("Blk 436 Serangoon Gardens Street 26, #16-43")),
                Optional.of(new Time("Saturday 14:00")), getTagSet("Student", "Junior"),
                Optional.of(new Remark("2nd student")),
                Optional.empty()),
            new Person(Id.of(5), new Name("Irfan Ibrahim"), Optional.of(new Phone("92492021")),
                Optional.of(new Address("Blk 47 Tampines Street 20, #17-35")),
                Optional.<Time>empty(), getTagSet("Parent"), Optional.of(new Remark("new student")),
                Optional.empty()),
            new Person(Id.of(6), new Name("Roy Balakrishnan"), Optional.of(new Phone("92624417")),
                Optional.of(new Address("Blk 45 Aljunied Street 85, #11-31")),
                Optional.<Time>empty(), getTagSet("Student", "Senior"), Optional.empty(), Optional.empty())
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
