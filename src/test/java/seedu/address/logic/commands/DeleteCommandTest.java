package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonWithId;
import static seedu.address.testutil.TypicalIds.ID_FIRST;
import static seedu.address.testutil.TypicalIds.ID_SECOND;
import static seedu.address.testutil.TypicalIds.ID_THIRD;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Id;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_idInAddressBook_success() {
        ArrayList<Id> ids = new ArrayList<Id>();
        ids.add(ID_FIRST);
        DeleteCommand deleteCommand = new DeleteCommand(ids);

        Optional<Person> personToDeleteFound = model.findPersonById(ID_FIRST);
        assertTrue(personToDeleteFound.isPresent());
        Person personToDelete = personToDeleteFound.get();

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_idNotInAddressBook_throwsCommandException() {
        Id notInAddressBookId = Id.fromCurrentMaxId(model.findMaxId());
        ArrayList<Id> ids = new ArrayList<Id>();
        ids.add(notInAddressBookId);
        DeleteCommand deleteCommand = new DeleteCommand(ids);

        String expectedMessage = String.format(Messages.MESSAGE_INVALID_PERSON_ID,
                notInAddressBookId.getValue());

        assertCommandFailure(deleteCommand, model, expectedMessage);
    }

    @Test
    public void execute_idInFilteredList_success() {
        showPersonWithId(model, ID_FIRST);

        ArrayList<Id> ids = new ArrayList<Id>();
        ids.add(ID_FIRST);
        DeleteCommand deleteCommand = new DeleteCommand(ids);

        Optional<Person> personToDeleteFound = model.findPersonById(ID_FIRST);
        assertTrue(personToDeleteFound.isPresent());
        Person personToDelete = personToDeleteFound.get();

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showPersonWithId(expectedModel, ID_FIRST);
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_idInAddressBookButNotInFilteredList_success() {
        showPersonWithId(model, ID_FIRST);

        Id idInAddressBookButNotInFilteredList = ID_SECOND;
        ArrayList<Id> ids = new ArrayList<Id>();
        ids.add(ID_SECOND);
        DeleteCommand deleteCommand = new DeleteCommand(ids);

        // ensures that idInAddressBookButNotInFilteredList is
        // still in address book list
        Optional<Person> personToDeleteFound = model.findPersonById(idInAddressBookButNotInFilteredList);
        assertTrue(personToDeleteFound.isPresent());
        Person personToDelete = personToDeleteFound.get();

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showPersonWithId(expectedModel, ID_FIRST);
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleIdsInAddressBook_success() {
        ArrayList<Id> ids = new ArrayList<Id>();
        ids.add(ID_FIRST);
        ids.add(ID_SECOND);
        ids.add(ID_THIRD);
        DeleteCommand deleteCommand = new DeleteCommand(ids);

        Optional<Person> first = model.findPersonById(ID_FIRST);
        Optional<Person> second = model.findPersonById(ID_SECOND);
        Optional<Person> third = model.findPersonById(ID_THIRD);

        String expectedMessage = DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS;

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        ArrayList<Person> personsToDelete = new ArrayList<Person>();
        personsToDelete.add(first.get());
        personsToDelete.add(second.get());
        personsToDelete.add(third.get());
        expectedModel.deletePersons(personsToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_oneOfMultipleIdsNotInAddressBook_throwsCommandExceptionAndDeletesNothing() {
        Id invalidId = Id.fromCurrentMaxId(model.findMaxId());

        ArrayList<Id> ids = new ArrayList<Id>();
        ids.add(ID_FIRST);
        ids.add(invalidId);
        DeleteCommand deleteCommand = new DeleteCommand(ids);

        String expectedMessage = String.format(Messages.MESSAGE_INVALID_PERSON_ID, invalidId.getValue());

        ModelManager modelSnapshot = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandFailure(deleteCommand, model, expectedMessage);
        assertEquals(modelSnapshot.getAddressBook(), model.getAddressBook());
    }

    @Test
    public void execute_idNotInEmptyAddressBook_throwsCommandException() {
        Model emptyModel = new ModelManager();
        ArrayList<Id> ids = new ArrayList<Id>();
        ids.add(ID_FIRST);
        DeleteCommand deleteCommand = new DeleteCommand(ids);

        assertCommandFailure(deleteCommand, emptyModel,
                String.format(Messages.MESSAGE_INVALID_PERSON_ID, ID_FIRST.getValue()));
    }

    @Test
    public void execute_duplicateSingleId_deletesOnce() {
        ArrayList<Id> ids = new ArrayList<Id>();
        ids.add(ID_FIRST);
        ids.add(ID_FIRST);
        DeleteCommand deleteCommand = new DeleteCommand(ids);

        Person personToDelete = model.findPersonById(ID_FIRST).get();
        String expectedMessage = DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS;

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateMultipleIds_deletesEachOnce() {
        ArrayList<Id> ids = new ArrayList<Id>();
        ids.add(ID_THIRD);
        ids.add(ID_THIRD);
        ids.add(ID_SECOND);
        ids.add(ID_SECOND);
        DeleteCommand deleteCommand = new DeleteCommand(ids);

        Person secondPerson = model.findPersonById(ID_SECOND).get();
        Person thirdPerson = model.findPersonById(ID_THIRD).get();
        String expectedMessage = DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS;

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        ArrayList<Person> personsToDelete = new ArrayList<Person>();
        personsToDelete.add(secondPerson);
        personsToDelete.add(thirdPerson);
        expectedModel.deletePersons(personsToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        ArrayList<Id> idsFirst = new ArrayList<Id>();
        idsFirst.add(ID_FIRST);

        ArrayList<Id> idsSecond = new ArrayList<Id>();
        idsSecond.add(ID_SECOND);
        DeleteCommand deleteFirstCommand = new DeleteCommand(idsFirst);
        DeleteCommand deleteSecondCommand = new DeleteCommand(idsSecond);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(idsFirst);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Id targetId = ID_FIRST;
        ArrayList<Id> ids = new ArrayList<Id>();
        ids.add(targetId);
        DeleteCommand deleteCommand = new DeleteCommand(ids);
        String expected = DeleteCommand.class.getCanonicalName() + "{targetIds=" + ids + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
