package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_STUDENT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_TUTOR;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Id;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class AddCommandTest {

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validPerson = new PersonBuilder().build();

        CommandResult commandResult = new AddCommand(validPerson).execute(modelStub);

        assertEquals(AddCommand.MESSAGE_SUCCESS, commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validPerson), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicatePersonSameFieldsSameCapitalisation_throwsCommandException() {
        Person validPerson = new PersonBuilder().build();
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        AddCommand addCommand = new AddCommand(validPerson);
        assertThrows(CommandException.class,
                AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicatePersonSameFieldsDifferentCapitalisation_throwsCommandException() {
        Person validPerson = new PersonBuilder().build();
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        String validPersonNameUppercase = validPerson.getName().fullName.toUpperCase();

        Optional<String> validPersonAddressUppercaseFound = validPerson
                .getAddress()
                .map(a -> a.value.toUpperCase());
        assert validPersonAddressUppercaseFound.isPresent()
                : "Valid person built has empty address";
        String validPersonAddressUppercase = validPersonAddressUppercaseFound.get();

        // capitalisation of name and address is different, but these are
        // still duplicates as equality of name and address is case-insensitive
        Person validPersonDifferentCase = new PersonBuilder(validPerson)
                .withName(validPersonNameUppercase)
                .withAddress(validPersonAddressUppercase)
                .build();
        AddCommand addCommand = new AddCommand(validPersonDifferentCase);
        assertThrows(CommandException.class,
                AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicatePersonDifferentIdSameCapitalisation_throwsCommandException() {
        Person validPerson = new PersonBuilder().withId(1).build();
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        // different id, but still considered as a duplicate
        // since the two sets of identity fields are identical
        Person existingDuplicatedPerson = new PersonBuilder().withId(2).build();
        AddCommand addCommand = new AddCommand(existingDuplicatedPerson);
        assertThrows(CommandException.class,
                AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicatePersonDifferentIdDifferentCapitalisation_throwsCommandException() {
        Person validPerson = new PersonBuilder().withId(1).build();
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        String validPersonNameUppercase = validPerson.getName().fullName.toUpperCase();

        Optional<String> validPersonAddressUppercaseFound = validPerson
                .getAddress()
                .map(a -> a.value.toUpperCase());
        assert validPersonAddressUppercaseFound.isPresent()
                : "Valid person built has empty address";
        String validPersonAddressUppercase = validPersonAddressUppercaseFound.get();

        // different id, but still considered as a duplicate
        // since the two sets of identity fields are identical (case-insensitive)
        Person validPersonDifferentIdDifferentCase = new PersonBuilder(validPerson)
                .withId(2)
                .withName(validPersonNameUppercase)
                .withAddress(validPersonAddressUppercase)
                .build();
        AddCommand addCommand = new AddCommand(validPersonDifferentIdDifferentCase);
        assertThrows(CommandException.class,
                AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicatePersonDifferentTagsSameCapitalisation_throwsCommandException() {
        Person validPerson = new PersonBuilder().withTags(VALID_TAG_TUTOR).build();
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        // different tags, but still considered as a duplicate
        // since the two sets of identity fields are identical
        Person existingDuplicatedPerson = new PersonBuilder()
                .withTags(VALID_TAG_STUDENT, VALID_TAG_TUTOR)
                .build();
        AddCommand addCommand = new AddCommand(existingDuplicatedPerson);
        assertThrows(CommandException.class,
                AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicatePersonDifferentTagsDifferentCapitalisation_throwsCommandException() {
        Person validPerson = new PersonBuilder().withTags(VALID_TAG_TUTOR).build();
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        String validPersonNameUppercase = validPerson.getName().fullName.toUpperCase();

        Optional<String> validPersonAddressUppercaseFound = validPerson
                .getAddress()
                .map(a -> a.value.toUpperCase());
        assert validPersonAddressUppercaseFound.isPresent()
                : "Valid person built has empty address";
        String validPersonAddressUppercase = validPersonAddressUppercaseFound.get();

        // different tags, but still considered as a duplicate
        // since the two sets of identity fields are identical (case-insensitive)
        Person validPersonDifferentTagsDifferentCase = new PersonBuilder(validPerson)
                .withName(validPersonNameUppercase)
                .withAddress(validPersonAddressUppercase)
                .withTags(VALID_TAG_STUDENT, VALID_TAG_TUTOR)
                .build();
        AddCommand addCommand = new AddCommand(validPersonDifferentTagsDifferentCase);
        assertThrows(CommandException.class,
                AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicatePersonDifferentLinkSameCapitalisation_throwsCommandException() {
        Person validPerson = new PersonBuilder().withMeetingLink("https://zoom.com/123").build();
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        // different link, but still considered as a duplicate
        // since the two sets of identity fields are identical
        Person existingDuplicatedPerson = new PersonBuilder()
                .withMeetingLink("https://zoom.com/456")
                .build();
        AddCommand addCommand = new AddCommand(existingDuplicatedPerson);
        assertThrows(CommandException.class,
                AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicatePersonDifferentLinkDifferentCapitalisation_throwsCommandException() {
        Person validPerson = new PersonBuilder().withMeetingLink("https://zoom.com/123").build();
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        String validPersonNameUppercase = validPerson.getName().fullName.toUpperCase();

        Optional<String> validPersonAddressUppercaseFound = validPerson
                .getAddress()
                .map(a -> a.value.toUpperCase());
        assert validPersonAddressUppercaseFound.isPresent()
                : "Valid person built has empty address";
        String validPersonAddressUppercase = validPersonAddressUppercaseFound.get();

        // different link, but still considered as a duplicate
        // since the two sets of identity fields are identical (case-insensitive)
        Person validPersonDifferentLinkDifferentCase = new PersonBuilder(validPerson)
                .withName(validPersonNameUppercase)
                .withAddress(validPersonAddressUppercase)
                .withMeetingLink("https://zoom.com/456")
                .build();
        AddCommand addCommand = new AddCommand(validPersonDifferentLinkDifferentCase);
        assertThrows(CommandException.class,
                AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicatePersonDifferentRemarkSameCapitalisation_throwsCommandException() {
        Person validPerson = new PersonBuilder().withRemark("lazy student").build();
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        // different remark, but still considered as a duplicate
        // since the two sets of identity fields are identical
        Person existingDuplicatedPerson = new PersonBuilder()
                .withRemark("gets distracted easily")
                .build();
        AddCommand addCommand = new AddCommand(existingDuplicatedPerson);
        assertThrows(CommandException.class,
                AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicatePersonDifferentRemarkDifferentCapitalisation_throwsCommandException() {
        Person validPerson = new PersonBuilder().withRemark("lazy student").build();
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        String validPersonNameUppercase = validPerson.getName().fullName.toUpperCase();

        Optional<String> validPersonAddressUppercaseFound = validPerson
                .getAddress()
                .map(a -> a.value.toUpperCase());
        assert validPersonAddressUppercaseFound.isPresent()
                : "Valid person built has empty address";
        String validPersonAddressUppercase = validPersonAddressUppercaseFound.get();

        // different remark, but still considered as a duplicate
        // since the two sets of identity fields are identical (case-insensitive)
        Person validPersonDifferentRemarkDifferentCase = new PersonBuilder(validPerson)
                .withName(validPersonNameUppercase)
                .withAddress(validPersonAddressUppercase)
                .withRemark("gets distracted easily")
                .build();
        AddCommand addCommand = new AddCommand(validPersonDifferentRemarkDifferentCase);
        assertThrows(CommandException.class,
                AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicatePersonDifferentTimeSameCapitalisation_throwsCommandException() {
        Person validPerson = new PersonBuilder().withTime("Thursday 16:00").build();
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        // different time, but still considered as a duplicate
        // since the two sets of identity fields are identical
        Person existingDuplicatedPerson = new PersonBuilder()
                .withTime("Sunday 10:00")
                .build();
        AddCommand addCommand = new AddCommand(existingDuplicatedPerson);
        assertThrows(CommandException.class,
                AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicatePersonDifferentTimeDifferentCapitalisation_throwsCommandException() {
        Person validPerson = new PersonBuilder().withTime("Thursday 16:00").build();
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        String validPersonNameUppercase = validPerson.getName().fullName.toUpperCase();

        Optional<String> validPersonAddressUppercaseFound = validPerson
                .getAddress()
                .map(a -> a.value.toUpperCase());
        assert validPersonAddressUppercaseFound.isPresent()
                : "Valid person built has empty address";
        String validPersonAddressUppercase = validPersonAddressUppercaseFound.get();

        // different time, but still considered as a duplicate
        // since the two sets of identity fields are identical (case-insensitive)
        Person validPersonDifferentTimeDifferentCase = new PersonBuilder(validPerson)
                .withName(validPersonNameUppercase)
                .withAddress(validPersonAddressUppercase)
                .withTime("Sunday 10:00")
                .build();
        AddCommand addCommand = new AddCommand(validPersonDifferentTimeDifferentCase);
        assertThrows(CommandException.class,
                AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Person alice = new PersonBuilder().withName(VALID_NAME_AMY).build();
        Person bob = new PersonBuilder().withName(VALID_NAME_BOB).build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different person -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        AddCommand addCommand = new AddCommand(ALICE);
        String expected = AddCommand.class.getCanonicalName() + "{toAdd=" + ALICE + "}";
        assertEquals(expected, addCommand.toString());
    }

    /**
     * A default model stub that has all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePersons(ArrayList<Person> targets) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Optional<Person> findPersonById(Id id) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Optional<Id> findMaxId() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getDisplayPersonList() {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Person person;

        ModelStubWithPerson(Person person) {
            requireNonNull(person);
            this.person = person;
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return this.person.isSamePerson(person);
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
