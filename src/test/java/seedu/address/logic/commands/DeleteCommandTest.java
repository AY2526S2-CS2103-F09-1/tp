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
        DeleteCommand deleteCommand = new DeleteCommand(ID_FIRST);

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
        DeleteCommand deleteCommand = new DeleteCommand(notInAddressBookId);

        String expectedMessage = String.format(Messages.MESSAGE_INVALID_PERSON_ID,
                notInAddressBookId.getValue());

        assertCommandFailure(deleteCommand, model, expectedMessage);
    }

    @Test
    public void execute_idInFilteredList_success() {
        showPersonWithId(model, ID_FIRST);

        DeleteCommand deleteCommand = new DeleteCommand(ID_FIRST);

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
        DeleteCommand deleteCommand = new DeleteCommand(idInAddressBookButNotInFilteredList);

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
    public void execute_idNotInEmptyAddressBook_throwsCommandException() {
        Model emptyModel = new ModelManager();
        DeleteCommand deleteCommand = new DeleteCommand(ID_FIRST);

        assertCommandFailure(deleteCommand, emptyModel,
                String.format(Messages.MESSAGE_INVALID_PERSON_ID, ID_FIRST.getValue()));
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(ID_FIRST);
        DeleteCommand deleteSecondCommand = new DeleteCommand(ID_SECOND);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(ID_FIRST);
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
        DeleteCommand deleteCommand = new DeleteCommand(targetId);
        String expected = DeleteCommand.class.getCanonicalName() + "{targetId=" + targetId + "}";
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
