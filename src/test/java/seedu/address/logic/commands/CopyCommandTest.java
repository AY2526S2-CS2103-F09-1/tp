package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_ID;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.testutil.TypicalIds.ID_FIRST;
import static seedu.address.testutil.TypicalIds.ID_SECOND;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Id;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code CopyCommand}.
 */
public class CopyCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_idNotInAddressBook_throwsCommandException() {
        Id notInAddressBookId = Id.fromCurrentMaxId(model.findMaxId());
        CopyCommand copyCommand = new CopyCommand(notInAddressBookId, PREFIX_PHONE.getPrefix());

        String expectedMessage = String.format(MESSAGE_INVALID_PERSON_ID,
                notInAddressBookId.getValue());

        assertCommandFailure(copyCommand, model, expectedMessage);
    }

    @Test
    public void execute_idNotInEmptyAddressBook_throwsCommandException() {
        Model emptyModel = new ModelManager();
        Id anyId = ID_FIRST;
        CopyCommand copyCommand = new CopyCommand(anyId, PREFIX_PHONE.getPrefix());

        String expectedMessage = String.format(MESSAGE_INVALID_PERSON_ID, anyId.getValue());

        assertCommandFailure(copyCommand, emptyModel, expectedMessage);
    }

    @Test
    public void execute_emptyPhoneField_throwsCommandException() {
        Person personWithEmptyPhone = new PersonBuilder().withId(67).withoutPhone().build();
        AddressBook ab = new AddressBook();
        ab.addPerson(personWithEmptyPhone);
        Model modelWithEmptyPhone = new ModelManager(ab, new UserPrefs());
        Id id = Id.of(67);
        CopyCommand copyCommand = new CopyCommand(id, PREFIX_PHONE.getPrefix());

        String expectedMessage = String.format(CopyCommand.MESSAGE_EMPTY_FIELD_VALUE, "phone number");

        assertCommandFailure(copyCommand, modelWithEmptyPhone, expectedMessage);
    }

    @Test
    public void execute_emptyAddressField_throwsCommandException() {
        Person personWithEmptyAddress = new PersonBuilder().withId(67).withAddress("").build();
        AddressBook ab = new AddressBook();
        ab.addPerson(personWithEmptyAddress);
        Model modelWithEmptyAddress = new ModelManager(ab, new UserPrefs());
        Id id = Id.of(67);
        CopyCommand copyCommand = new CopyCommand(id, PREFIX_ADDRESS.getPrefix());

        String expectedMessage = String.format(CopyCommand.MESSAGE_EMPTY_FIELD_VALUE, "address");

        assertCommandFailure(copyCommand, modelWithEmptyAddress, expectedMessage);
    }

    @Test
    public void equals() {
        CopyCommand copyFirstPhone = new CopyCommand(ID_FIRST, PREFIX_PHONE.getPrefix());
        CopyCommand copySecondPhone = new CopyCommand(ID_SECOND, PREFIX_PHONE.getPrefix());
        CopyCommand copyFirstName = new CopyCommand(ID_FIRST, PREFIX_NAME.getPrefix());

        assertTrue(copyFirstPhone.equals(copyFirstPhone));

        CopyCommand copyFirstPhoneCopy = new CopyCommand(ID_FIRST, PREFIX_PHONE.getPrefix());
        assertTrue(copyFirstPhone.equals(copyFirstPhoneCopy));
        assertFalse(copyFirstPhone.equals(copySecondPhone));
        assertFalse(copyFirstPhone.equals(copyFirstName));
        assertFalse(copyFirstPhone.equals(1));
        assertFalse(copyFirstPhone.equals(null));
    }

    @Test
    public void toStringMethod() {
        CopyCommand copyCommand = new CopyCommand(ID_FIRST, PREFIX_PHONE.getPrefix());
        String expected = CopyCommand.class.getCanonicalName()
                + "{targetId=" + ID_FIRST + ", field=" + PREFIX_PHONE.getPrefix() + "}";
        assertEquals(expected, copyCommand.toString());
    }
}
