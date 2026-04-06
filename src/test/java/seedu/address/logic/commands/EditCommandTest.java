package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_PARENT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_STUDENT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_TUTOR;
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
    private static final String TAG_TEST_PHONE = "91234567";
    private static final String TAG_TEST_ADDRESS = "42 Tag Test Avenue";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_idInAddressBookAllFieldsSpecified_success() {
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(ID_FIRST, descriptor);

        Optional<Person> personToEditFound = model.findPersonById(ID_FIRST);
        assertTrue(personToEditFound.isPresent());
        Person personToEdit = personToEditFound.get();

        String expectedMessage = String.format(Messages.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

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
                .withTags(VALID_TAG_TUTOR, VALID_TAG_PARENT).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_PARENT).build();
        EditCommand editCommand = new EditCommand(maxId, descriptor);

        String expectedMessage = String.format(Messages.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personWithMaxId, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_idInAddressBookAppendTag_success() {
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withTags(VALID_TAG_PARENT).build();
        EditCommand editCommand = new EditCommand(ID_FIRST, descriptor);

        Optional<Person> personToEditFound = model.findPersonById(ID_FIRST);
        assertTrue(personToEditFound.isPresent());
        Person personToEdit = personToEditFound.get();
        Person editedPerson = new PersonBuilder(personToEdit)
                .withTags(VALID_TAG_STUDENT, VALID_TAG_PARENT).build();

        String expectedMessage = String.format(Messages.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_idInAddressBookAppendMultipleTags_success() {
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withTags(VALID_TAG_PARENT, VALID_TAG_TUTOR).build();
        EditCommand editCommand = new EditCommand(ID_FIRST, descriptor);

        Optional<Person> personToEditFound = model.findPersonById(ID_FIRST);
        assertTrue(personToEditFound.isPresent());
        Person personToEdit = personToEditFound.get();
        Person editedPerson = new PersonBuilder(personToEdit)
                .withTags(VALID_TAG_STUDENT, VALID_TAG_PARENT, VALID_TAG_TUTOR).build();

        String expectedMessage = String.format(Messages.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_idInAddressBookClearTags_success() {
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withTags().build();
        EditCommand editCommand = new EditCommand(ID_FIRST, descriptor);

        Optional<Person> personToEditFound = model.findPersonById(ID_FIRST);
        assertTrue(personToEditFound.isPresent());
        Person personToEdit = personToEditFound.get();
        Person editedPerson = new PersonBuilder(personToEdit).withTags().build();

        String expectedMessage = String.format(Messages.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_idInAddressBookClearPhone_success() {
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withoutPhone().build();
        EditCommand editCommand = new EditCommand(ID_FIRST, descriptor);

        Optional<Person> personToEditFound = model.findPersonById(ID_FIRST);
        assertTrue(personToEditFound.isPresent());
        Person personToEdit = personToEditFound.get();
        Person editedPerson = new PersonBuilder(personToEdit).withoutPhone().build();

        String expectedMessage = String.format(Messages.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_idInAddressBookClearAddress_success() {
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withoutAddress().build();
        EditCommand editCommand = new EditCommand(ID_FIRST, descriptor);

        Optional<Person> personToEditFound = model.findPersonById(ID_FIRST);
        assertTrue(personToEditFound.isPresent());
        Person personToEdit = personToEditFound.get();
        Person editedPerson = new PersonBuilder(personToEdit).withoutAddress().build();

        String expectedMessage = String.format(Messages.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_idInAddressBookAppendExistingTag_success() {
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withTags(VALID_TAG_STUDENT).build();
        EditCommand editCommand = new EditCommand(ID_FIRST, descriptor);

        Optional<Person> personToEditFound = model.findPersonById(ID_FIRST);
        assertTrue(personToEditFound.isPresent());
        Person personToEdit = personToEditFound.get();

        String expectedMessage = String.format(Messages.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(personToEdit));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, personToEdit);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_repeatedSameTagInputAcrossExistingTagStates_success() {
        // Repeated tag inputs such as `t/Student t/Student` are deduplicated by the parser.
        // These cases verify the resulting edit behavior for all relevant initial tag states.
        assertRepeatedStudentTagAdditionSuccess(
                createTagTestPerson(40, VALID_TAG_STUDENT, VALID_TAG_PARENT),
                VALID_TAG_STUDENT, VALID_TAG_PARENT);
        assertRepeatedStudentTagAdditionSuccess(
                createTagTestPerson(41, VALID_TAG_STUDENT),
                VALID_TAG_STUDENT);
        assertRepeatedStudentTagAdditionSuccess(
                createTagTestPerson(42, VALID_TAG_PARENT),
                VALID_TAG_PARENT, VALID_TAG_STUDENT);
        assertRepeatedStudentTagAdditionSuccess(
                createTagTestPerson(43),
                VALID_TAG_STUDENT);
    }

    @Test
    public void execute_addStudentDeleteParentAcrossExistingTagStates_success() {
        assertAddStudentDeleteParentSuccess(
                createTagTestPerson(44, VALID_TAG_STUDENT, VALID_TAG_PARENT),
                VALID_TAG_STUDENT);
        assertAddStudentDeleteParentSuccess(
                createTagTestPerson(45, VALID_TAG_STUDENT),
                VALID_TAG_STUDENT);
        assertAddStudentDeleteParentSuccess(
                createTagTestPerson(46, VALID_TAG_PARENT),
                VALID_TAG_STUDENT);
        assertAddStudentDeleteParentSuccess(
                createTagTestPerson(47),
                VALID_TAG_STUDENT);
    }

    @Test
    public void execute_idInAddressBookDeleteSpecificTag_success() {
        Person alanTuring = new PersonBuilder().withId(42).withName("Alan Turing")
                .withPhone("91234567").withAddress("Computing Avenue")
                .withTags(VALID_TAG_STUDENT, VALID_TAG_TUTOR).build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(alanTuring);
        Model modelWithAlan = new ModelManager(addressBook, new UserPrefs());

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withTagsToDelete(VALID_TAG_STUDENT)
                .build();
        EditCommand editCommand = new EditCommand(Id.of(42), descriptor);

        Person editedPerson = new PersonBuilder(alanTuring).withTags(VALID_TAG_TUTOR).build();
        String expectedMessage = String.format(Messages.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(modelWithAlan.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(alanTuring, editedPerson);

        assertCommandSuccess(editCommand, modelWithAlan, expectedMessage, expectedModel);
    }

    @Test
    public void execute_idInAddressBookAddAndDeleteTags_success() {
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withTags(VALID_TAG_TUTOR)
                .withTagsToDelete(VALID_TAG_STUDENT)
                .build();
        EditCommand editCommand = new EditCommand(ID_FIRST, descriptor);

        Optional<Person> personToEditFound = model.findPersonById(ID_FIRST);
        assertTrue(personToEditFound.isPresent());
        Person personToEdit = personToEditFound.get();
        Person editedPerson = new PersonBuilder(personToEdit).withTags(VALID_TAG_TUTOR).build();

        String expectedMessage = String.format(Messages.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_idInAddressBookDeleteMissingTag_success() {
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withTagsToDelete(VALID_TAG_PARENT).build();
        EditCommand editCommand = new EditCommand(ID_FIRST, descriptor);

        Optional<Person> personToEditFound = model.findPersonById(ID_FIRST);
        assertTrue(personToEditFound.isPresent());
        Person personToEdit = personToEditFound.get();

        String expectedMessage = String.format(Messages.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(personToEdit));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, personToEdit);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_idInAddressBookNoFieldSpecified_failure() {
        EditCommand editCommand = new EditCommand(ID_FIRST, new EditPersonDescriptor());
        assertCommandFailure(editCommand, model, Messages.MESSAGE_NOT_EDITED);
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

        String expectedMessage = String.format(Messages.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_idInFilteredListAppendTag_success() {
        showPersonWithId(model, ID_FIRST);

        EditCommand editCommand = new EditCommand(ID_FIRST,
                new EditPersonDescriptorBuilder().withTags(VALID_TAG_TUTOR).build());

        Optional<Person> personToEditFound = model.findPersonById(ID_FIRST);
        assertTrue(personToEditFound.isPresent());
        Person personToEdit = personToEditFound.get();
        Person editedPerson = new PersonBuilder(personToEdit).withTags(VALID_TAG_STUDENT, VALID_TAG_TUTOR).build();

        String expectedMessage = String.format(Messages.MESSAGE_EDIT_PERSON_SUCCESS,
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

        Optional<Person> personToEditFound = model.findPersonById(idInAddressBookButNotInFilteredList);
        assertTrue(personToEditFound.isPresent());
        Person personToEdit = personToEditFound.get();
        Person editedPerson = new PersonBuilder(personToEdit).withName(VALID_NAME_BOB).build();

        String expectedMessage = String.format(Messages.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_idInAddressBookButNotInFilteredListAppendTag_success() {
        showPersonWithId(model, ID_FIRST);

        Id idInAddressBookButNotInFilteredList = ID_SECOND;
        EditCommand editCommand = new EditCommand(idInAddressBookButNotInFilteredList,
                new EditPersonDescriptorBuilder().withTags(VALID_TAG_TUTOR).build());

        Optional<Person> personToEditFound = model.findPersonById(idInAddressBookButNotInFilteredList);
        assertTrue(personToEditFound.isPresent());
        Person personToEdit = personToEditFound.get();
        Person editedPerson = new PersonBuilder(personToEdit)
                .withTags(VALID_TAG_PARENT, VALID_TAG_TUTOR).build();

        String expectedMessage = String.format(Messages.MESSAGE_EDIT_PERSON_SUCCESS,
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

        assertCommandFailure(editCommand, model, Messages.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_idInFilteredListDuplicatePersonInAddressBook_failure() {
        showPersonWithId(model, ID_FIRST);

        Optional<Person> personInBookFound = model.findPersonById(ID_SECOND);
        assertTrue(personInBookFound.isPresent());
        Person personInBook = personInBookFound.get();
        EditCommand editCommand = new EditCommand(ID_FIRST,
                new EditPersonDescriptorBuilder(personInBook).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_DUPLICATE_PERSON);
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

        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(ID_FIRST, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        assertTrue(standardCommand.equals(standardCommand));
        assertFalse(standardCommand.equals(null));
        assertFalse(standardCommand.equals(new ClearCommand()));
        assertFalse(standardCommand.equals(new EditCommand(ID_SECOND, DESC_AMY)));
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

    private void assertRepeatedStudentTagAdditionSuccess(Person personToEdit, String... expectedTags) {
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(personToEdit);
        Model modelWithPerson = new ModelManager(addressBook, new UserPrefs());

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withTags(VALID_TAG_STUDENT).build();
        EditCommand editCommand = new EditCommand(personToEdit.getId(), descriptor);

        Person editedPerson = new PersonBuilder(personToEdit).withTags(expectedTags).build();
        String expectedMessage = String.format(Messages.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(modelWithPerson.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, modelWithPerson, expectedMessage, expectedModel);
    }

    private void assertAddStudentDeleteParentSuccess(Person personToEdit, String... expectedTags) {
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(personToEdit);
        Model modelWithPerson = new ModelManager(addressBook, new UserPrefs());

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withTags(VALID_TAG_STUDENT)
                .withTagsToDelete(VALID_TAG_PARENT)
                .build();
        EditCommand editCommand = new EditCommand(personToEdit.getId(), descriptor);

        Person editedPerson = new PersonBuilder(personToEdit).withTags(expectedTags).build();
        String expectedMessage = String.format(Messages.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(modelWithPerson.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, modelWithPerson, expectedMessage, expectedModel);
    }

    private Person createTagTestPerson(int id, String... tags) {
        return new PersonBuilder().withId(id)
                .withPhone(TAG_TEST_PHONE)
                .withAddress(TAG_TEST_ADDRESS)
                .withTags(tags)
                .build();
    }
}
