package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Address;
import seedu.address.model.person.Id;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class MessagesTest {

    @Test
    public void getErrorMessageForDuplicatePrefixes_deduplicatesAndListsPrefixes() {
        String message = Messages.getErrorMessageForDuplicatePrefixes(
                new Prefix("n/"), new Prefix("p/"), new Prefix("n/"));

        assertTrue(message.startsWith(Messages.MESSAGE_DUPLICATE_FIELDS));

        String prefixList = message.substring(Messages.MESSAGE_DUPLICATE_FIELDS.length());
        Set<String> actualPrefixes = Arrays.stream(prefixList.split(" "))
                .filter(prefix -> !prefix.isEmpty())
                .collect(Collectors.toSet());

        assertEquals(Set.of("n/", "p/"), actualPrefixes);
    }

    @Test
    public void format_personWithPhoneAndTag_success() {
        Person person = new PersonBuilder()
                .withName("Alice")
                .withPhone("91234567")
                .withAddress("123, Jurong West Ave 6")
                .withTags("Student")
                .build();

        String formatted = Messages.format(person);

        assertEquals("Alice; Phone: 91234567; Address: 123, Jurong West Ave 6; Tags: [Student]; Remark: first student",
            formatted);
    }

    @Test
    public void format_personWithoutPhone_success() {
        Person person = new Person(
                Id.of(1),
                new Name("Bob"),
                Optional.<Phone>empty(),
                Optional.of(new Address("311, Clementi Ave 2")),
                new HashSet<Tag>(),
                Optional.<Remark>of(new Remark("Test remark 1")));

        String formatted = Messages.format(person);

        assertEquals("Bob; Phone: ; Address: 311, Clementi Ave 2; Tags: ; Remark: Test remark 1", formatted);
    }

    @Test
    public void format_personWithoutRemark_success() {
        Person person = new Person(
                Id.of(1),
                new Name("Bob"),
                Optional.<Phone>of(new Phone("91234567")),
                Optional.of(new Address("311, Clementi Ave 2")),
                new HashSet<Tag>(),
                Optional.<Remark>empty());

        String formatted = Messages.format(person);

        assertEquals("Bob; Phone: 91234567; Address: 311, Clementi Ave 2; Tags: ; Remark: ", formatted);
    }

    @Test
    public void format_personWithoutAddress_success() {
        Person person = new Person(
                Id.of(1),
                new Name("Bob"),
                Optional.of(new Phone("98765432")),
                Optional.empty(),
                new HashSet<Tag>(),
                Optional.<Remark>empty());

        String formatted = Messages.format(person);

        assertEquals("Bob; Phone: 98765432; Address: ; Tags: ; Remark: ", formatted);
    }

}
