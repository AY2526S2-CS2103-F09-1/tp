package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonWithId;
import static seedu.address.testutil.TypicalIds.ID_FIRST;
import static seedu.address.testutil.TypicalIds.ID_SECOND;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Id;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_idInAddressBookAllFieldsSpecified_success() {
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(ID_FIRST, descriptor);

        Optional<Person> personToEditFound = model.findPersonById(ID_FIRST);
        assertTrue(personToEditFound.isPresent());
        Person personToEdit = personToEditFound.get();

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_idInAddressBookSomeFieldsSpecified_success() {
        Optional<Id> maxIdFound = model.findMaxId();
        assertTrue(maxIdFound.isPresent());
        Id maxId = maxIdFound.get();

        Optional<Person> personWithMaxIdFound = model.findPersonById(maxId);
        assertTrue(personWithMaxIdFound.isPresent());
        Person personWithMaxId = personWithMaxIdFound.get();

        PersonBuilder personBuilderForMaxId = new PersonBuilder(personWithMaxId);
        Person editedPerson = personBuilderForMaxId.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(maxId, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personWithMaxId, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_idInAddressBookNoFieldSpecified_success() {
        EditCommand editCommand = new EditCommand(ID_FIRST, new EditPersonDescriptor());

        Optional<Person> personToEditFound = model.findPersonById(ID_FIRST);
        assertTrue(personToEditFound.isPresent());
        Person editedPerson = personToEditFound.get();

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_idInFilteredList_success() {
        showPersonWithId(model, ID_FIRST);

        EditCommand editCommand = new EditCommand(ID_FIRST,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        Optional<Person> personToEditFound = model.findPersonById(ID_FIRST);
        assertTrue(personToEditFound.isPresent());
        Person personToEdit = personToEditFound.get();
        Person editedPerson = new PersonBuilder(personToEdit).withName(VALID_NAME_BOB).build();

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Edit a person who is not in the current filtered list,
     * but is still present in the address book.
     */
    @Test
    public void execute_idInAddressBookButNotInFilteredList_success() {
        showPersonWithId(model, ID_FIRST);

        Id idInAddressBookButNotInFilteredList = ID_SECOND;
        EditCommand editCommand = new EditCommand(idInAddressBookButNotInFilteredList,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        // ensures that idInAddressBookButNotInFilteredList is
        // still in address book list
        Optional<Person> personToEditFound = model.findPersonById(idInAddressBookButNotInFilteredList);
        assertTrue(personToEditFound.isPresent());
        Person personToEdit = personToEditFound.get();
        Person editedPerson = new PersonBuilder(personToEdit).withName(VALID_NAME_BOB).build();

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_idInAddressBookDuplicatePerson_failure() {
        Optional<Person> firstPersonFound = model.findPersonById(ID_FIRST);
        assertTrue(firstPersonFound.isPresent());
        Person firstPerson = firstPersonFound.get();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        EditCommand editCommand = new EditCommand(ID_SECOND, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_idInFilteredListDuplicatePersonInAddressBook_failure() {
        showPersonWithId(model, ID_FIRST);

        // edit person in filtered list into a duplicate in address book
        Optional<Person> personInBookFound = model.findPersonById(ID_SECOND);
        assertTrue(personInBookFound.isPresent());
        Person personInBook = personInBookFound.get();
        EditCommand editCommand = new EditCommand(ID_FIRST,
                new EditPersonDescriptorBuilder(personInBook).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_idNotInAddressBook_failure() {
        Id notInAddressBookId = Id.fromCurrentMaxId(model.findMaxId());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(notInAddressBookId, descriptor);

        assertCommandFailure(editCommand, model,
                String.format(Messages.MESSAGE_INVALID_PERSON_ID,
                        notInAddressBookId.getValue()));
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(ID_FIRST, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(ID_FIRST, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(ID_SECOND, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(ID_FIRST, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        EditCommand editCommand = new EditCommand(ID_FIRST, editPersonDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{id=" + ID_FIRST + ", editPersonDescriptor="
                + editPersonDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

}
