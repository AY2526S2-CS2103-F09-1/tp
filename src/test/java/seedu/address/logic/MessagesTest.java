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
                .withTags("friend")
                .build();

        String formatted = Messages.format(person);

        assertEquals("Alice; Phone: 91234567; Address: 123, Jurong West Ave 6; Tags: [friend]", formatted);
    }

    @Test
    public void format_personWithoutPhone_success() {
        Person person = new Person(
                Id.of(1),
                new Name("Bob"),
                Optional.<Phone>empty(),
                new Address("311, Clementi Ave 2"),
                new HashSet<Tag>());

        String formatted = Messages.format(person);

        assertEquals("Bob; Phone: ; Address: 311, Clementi Ave 2; Tags: ", formatted);
    }
}
