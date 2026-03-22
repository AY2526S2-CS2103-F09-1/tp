package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonWithId;
import static seedu.address.testutil.TypicalIds.ID_FIRST;
import static seedu.address.testutil.TypicalIds.ID_SECOND;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Id;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains unit tests for {@code TagCommand}.
 */
public class TagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_idInAddressBook_success() {
        TagCommand tagCommand = new TagCommand(ID_SECOND, new Tag("Student"));

        Optional<Person> personToTagFound = model.findPersonById(ID_SECOND);
        assertTrue(personToTagFound.isPresent());
        Person personToTag = personToTagFound.get();
        Person taggedPerson = new PersonBuilder(personToTag).withTags("Student").build();

        String expectedMessage = String.format(TagCommand.MESSAGE_TAG_PERSON_SUCCESS, "Student",
                ID_SECOND.getValue());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToTag, taggedPerson);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_idInFilteredList_success() {
        showPersonWithId(model, ID_FIRST);

        TagCommand tagCommand = new TagCommand(ID_FIRST, new Tag("Tutor"));

        Optional<Person> personToTagFound = model.findPersonById(ID_FIRST);
        assertTrue(personToTagFound.isPresent());
        Person personToTag = personToTagFound.get();
        Person taggedPerson = new PersonBuilder(personToTag).withTags("Tutor").build();

        String expectedMessage = String.format(TagCommand.MESSAGE_TAG_PERSON_SUCCESS, "Tutor",
                ID_FIRST.getValue());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        showPersonWithId(expectedModel, ID_FIRST);
        expectedModel.setPerson(personToTag, taggedPerson);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_idNotInAddressBook_failure() {
        Id notInAddressBookId = Id.fromCurrentMaxId(model.findMaxId());
        TagCommand tagCommand = new TagCommand(notInAddressBookId, new Tag("Parent"));

        assertCommandFailure(tagCommand, model,
                String.format(TagCommand.MESSAGE_INVALID_PERSON_ID, notInAddressBookId.getValue()));
    }

    @Test
    public void execute_idInAddressBookButNotInFilteredList_success() {
        showPersonWithId(model, ID_FIRST);

        Id idInAddressBookButNotInFilteredList = ID_SECOND;
        TagCommand tagCommand = new TagCommand(idInAddressBookButNotInFilteredList, new Tag("Parent"));

        // ensures that idInAddressBookButNotInFilteredList is
        // still in address book list
        Optional<Person> personToTagFound = model.findPersonById(idInAddressBookButNotInFilteredList);
        assertTrue(personToTagFound.isPresent());
        Person personToTag = personToTagFound.get();
        Person taggedPerson = new PersonBuilder(personToTag).withTags("Parent").build();

        String expectedMessage = String.format(TagCommand.MESSAGE_TAG_PERSON_SUCCESS, "Parent",
                ID_SECOND.getValue());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        showPersonWithId(expectedModel, ID_FIRST);
        expectedModel.setPerson(personToTag, taggedPerson);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        TagCommand firstCommand = new TagCommand(ID_FIRST, new Tag("Student"));
        TagCommand firstCommandCopy = new TagCommand(ID_FIRST, new Tag("Student"));
        TagCommand firstCommandDifferentTag = new TagCommand(ID_FIRST, new Tag("Parent"));
        TagCommand secondCommand = new TagCommand(ID_SECOND, new Tag("Tutor"));

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(firstCommandCopy));
        assertFalse(firstCommand.equals(1));
        assertFalse(firstCommand.equals(null));
        assertFalse(firstCommand.equals(firstCommandDifferentTag));
        assertFalse(firstCommand.equals(secondCommand));
    }

    @Test
    public void toStringMethod() {
        Tag categoryTag = new Tag("Student");
        TagCommand tagCommand = new TagCommand(ID_FIRST, categoryTag);
        String expected = TagCommand.class.getCanonicalName()
                + "{targetId=" + ID_FIRST + ", categoryTag=" + categoryTag + "}";
        assertEquals(expected, tagCommand.toString());
    }

    private static Predicate<Person> matchPersonName(Person person) {
        return candidate -> candidate.getName().equals(person.getName());
    }
}
