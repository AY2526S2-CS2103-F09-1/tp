package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_PARENT;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() {
        Person validPerson = new PersonBuilder().withId(8).build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(validPerson);

        assertCommandSuccess(new AddCommand(validPerson), model,
                AddCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_newPersonWithoutPhoneAndAddress_success() {
        Person validPerson = new PersonBuilder()
                .withId(8)
                .withName("No Phone")
                .withoutPhone()
                .withoutAddress()
                .build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(validPerson);

        assertCommandSuccess(new AddCommand(validPerson), model,
                AddCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_duplicatePersonSameFieldsSameCapitalisation_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);
        assertCommandFailure(new AddCommand(personInList), model,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonSameFieldsDifferentCapitalisation_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);

        String personNameUppercase = personInList.getName().fullName.toUpperCase();

        Optional<String> personAddressUppercaseFound = personInList
                .getAddress()
                .map(a -> a.value.toUpperCase());
        assert personAddressUppercaseFound.isPresent()
                : "Person chosen from typical AddressBook has empty address";
        String personAddressUppercase = personAddressUppercaseFound.get();

        // capitalisation of name and address is different, but these are
        // still duplicates as equality of name and address is case-insensitive
        Person personInListDifferentCase = new PersonBuilder(personInList)
                .withName(personNameUppercase)
                .withAddress(personAddressUppercase)
                .build();
        assertCommandFailure(new AddCommand(personInListDifferentCase), model,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonDifferentId_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);

        // different id, but still considered as a duplicate
        // since the two sets of identity fields are identical
        Person duplicatedPerson = new PersonBuilder(personInList).withId(2).build();
        assertCommandFailure(new AddCommand(duplicatedPerson), model,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonDifferentIdDifferentCapitalisation_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);

        String personNameUppercase = personInList.getName().fullName.toUpperCase();

        Optional<String> personAddressUppercaseFound = personInList
                .getAddress()
                .map(a -> a.value.toUpperCase());
        assert personAddressUppercaseFound.isPresent()
                : "Person chosen from typical AddressBook has empty address";
        String personAddressUppercase = personAddressUppercaseFound.get();

        // different id, but still considered as a duplicate
        // since the two sets of identity fields are identical (case-insensitive)
        Person personInListDifferentIdDifferentCase = new PersonBuilder(personInList)
                .withId(2)
                .withName(personNameUppercase)
                .withAddress(personAddressUppercase)
                .build();
        assertCommandFailure(new AddCommand(personInListDifferentIdDifferentCase), model,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonDifferentTagsSameCapitalisation_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);

        // different tags, but still considered as a duplicate
        // since the two sets of identity fields are identical
        Person duplicatedPerson = new PersonBuilder(personInList).withTags(VALID_TAG_PARENT).build();
        assertCommandFailure(new AddCommand(duplicatedPerson), model,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonDifferentTagsDifferentCapitalisation_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);

        String personNameUppercase = personInList.getName().fullName.toUpperCase();

        Optional<String> personAddressUppercaseFound = personInList
                .getAddress()
                .map(a -> a.value.toUpperCase());
        assert personAddressUppercaseFound.isPresent()
                : "Person chosen from typical AddressBook has empty address";
        String personAddressUppercase = personAddressUppercaseFound.get();

        // different tags, but still considered as a duplicate
        // since the two sets of identity fields are identical (case-insensitive)
        Person personInListDifferentTagsDifferentCase = new PersonBuilder(personInList)
                .withName(personNameUppercase)
                .withAddress(personAddressUppercase)
                .withTags(VALID_TAG_PARENT)
                .build();
        assertCommandFailure(new AddCommand(personInListDifferentTagsDifferentCase), model,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonDifferentLinkSameCapitalisation_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);

        // different link, but still considered as a duplicate
        // since the two sets of identity fields are identical
        Person duplicatedPerson = new PersonBuilder(personInList)
                .withMeetingLink("https://zoom.com/123")
                .build();
        assertCommandFailure(new AddCommand(duplicatedPerson), model,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonDifferentLinkDifferentCapitalisation_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);

        String personNameUppercase = personInList.getName().fullName.toUpperCase();

        Optional<String> personAddressUppercaseFound = personInList
                .getAddress()
                .map(a -> a.value.toUpperCase());
        assert personAddressUppercaseFound.isPresent()
                : "Person chosen from typical AddressBook has empty address";
        String personAddressUppercase = personAddressUppercaseFound.get();

        // different link, but still considered as a duplicate
        // since the two sets of identity fields are identical (case-insensitive)
        Person personInListDifferentLinkDifferentCase = new PersonBuilder(personInList)
                .withName(personNameUppercase)
                .withAddress(personAddressUppercase)
                .withMeetingLink("https://zoom.com/123")
                .build();
        assertCommandFailure(new AddCommand(personInListDifferentLinkDifferentCase), model,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonDifferentRemarkSameCapitalisation_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);

        // different remark, but still considered as a duplicate
        // since the two sets of identity fields are identical
        Person duplicatedPerson = new PersonBuilder(personInList)
                .withRemark("Not much to say here")
                .build();
        assertCommandFailure(new AddCommand(duplicatedPerson), model,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonDifferentRemarkDifferentCapitalisation_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);

        String personNameUppercase = personInList.getName().fullName.toUpperCase();

        Optional<String> personAddressUppercaseFound = personInList
                .getAddress()
                .map(a -> a.value.toUpperCase());
        assert personAddressUppercaseFound.isPresent()
                : "Person chosen from typical AddressBook has empty address";
        String personAddressUppercase = personAddressUppercaseFound.get();

        // different remark, but still considered as a duplicate
        // since the two sets of identity fields are identical (case-insensitive)
        Person personInListDifferentRemarkDifferentCase = new PersonBuilder(personInList)
                .withName(personNameUppercase)
                .withAddress(personAddressUppercase)
                .withRemark("Not much to say here")
                .build();
        assertCommandFailure(new AddCommand(personInListDifferentRemarkDifferentCase), model,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonDifferentTimeSameCapitalisation_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);

        // different time, but still considered as a duplicate
        // since the two sets of identity fields are identical
        Person duplicatedPerson = new PersonBuilder(personInList)
                .withTime("Wednesday 16:00")
                .build();
        assertCommandFailure(new AddCommand(duplicatedPerson), model,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonDifferentTimeDifferentCapitalisation_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);

        String personNameUppercase = personInList.getName().fullName.toUpperCase();

        Optional<String> personAddressUppercaseFound = personInList
                .getAddress()
                .map(a -> a.value.toUpperCase());
        assert personAddressUppercaseFound.isPresent()
                : "Person chosen from typical AddressBook has empty address";
        String personAddressUppercase = personAddressUppercaseFound.get();

        // different time, but still considered as a duplicate
        // since the two sets of identity fields are identical (case-insensitive)
        Person personInListDifferentRemarkDifferentCase = new PersonBuilder(personInList)
                .withName(personNameUppercase)
                .withAddress(personAddressUppercase)
                .withTime("Wednesday 16:00")
                .build();
        assertCommandFailure(new AddCommand(personInListDifferentRemarkDifferentCase), model,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }
}
