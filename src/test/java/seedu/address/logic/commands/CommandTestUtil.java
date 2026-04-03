package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.person.Id;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.model.person.PersonContainsKeywordsPredicate.MatchMode;
import seedu.address.testutil.EditPersonDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_ADDRESS_EMPTY_STRING = " " + PREFIX_ADDRESS; // empty string
    public static final String VALID_TAG_STUDENT = "Student";
    public static final String VALID_TAG_PARENT = "Parent";
    public static final String VALID_TAG_TUTOR = "Tutor";

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_ADDRESS_AMY = "Block 312, Amy Street 1";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3";
    public static final String VALID_TIME_AMY = "Monday 18:00";
    public static final String VALID_TIME_BOB = "Tuesday 09:30";
    public static final String VALID_TIME_DURATION_BOB = "Wednesday 09:30 - 10:30";
    public static final String VALID_REMARK_AMY = "first student";
    public static final String VALID_REMARK_BOB = "2nd student";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;
    public static final String TIME_DESC_AMY = " " + PREFIX_TIME + VALID_TIME_AMY;
    public static final String TIME_DESC_BOB = " " + PREFIX_TIME + VALID_TIME_BOB;
    public static final String TIME_DESC_DURATION_BOB = " " + PREFIX_TIME + VALID_TIME_DURATION_BOB;
    public static final String REMARK_DESC_AMY = " " + PREFIX_REMARK + VALID_REMARK_AMY;
    public static final String REMARK_DESC_BOB = " " + PREFIX_REMARK + VALID_REMARK_BOB;
    public static final String TAG_DESC_STUDENT = " " + PREFIX_TAG + VALID_TAG_STUDENT;
    public static final String TAG_DESC_PARENT = " " + PREFIX_TAG + VALID_TAG_PARENT;
    public static final String TAG_DESC_TUTOR = " " + PREFIX_TAG + VALID_TAG_TUTOR;
    public static final String INVALID_UNLISTED_TAG_DESC = " " + PREFIX_TAG + "Friend";

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    // public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS;
    public static final String INVALID_TIME_DESC = " " + PREFIX_TIME + "Monday 25:00";
    public static final String INVALID_TIME_DURATION_DESC = " " + PREFIX_TIME + "Monday 18:00 - 17:30";
    public static final String INVALID_TIME_NO_DAY_DESC = " " + PREFIX_TIME + "1800";
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditPersonDescriptor DESC_AMY;
    public static final EditCommand.EditPersonDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withAddress(VALID_ADDRESS_AMY)
                .withTags(VALID_TAG_STUDENT).build();
        DESC_BOB = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withAddress(VALID_ADDRESS_BOB)
                .withTags(VALID_TAG_PARENT).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult}
     * <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to
     * {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered person list and selected person in
     * {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        List<Person> expectedFilteredList = new ArrayList<>(actualModel.getFilteredPersonList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedAddressBook, actualModel.getAddressBook());
        assertEquals(expectedFilteredList, actualModel.getFilteredPersonList());
    }

    /**
     * Updates {@code model}'s filtered list to show only the person with the given
     * {@code targetId} in the
     * {@code model}'s address book.
     */
    public static void showPersonWithId(Model model, Id targetId) {
        assertTrue(model.findPersonById(targetId).isPresent());

        Optional<Person> personFound = model.findPersonById(targetId);
        assertTrue(personFound.isPresent());

        Person person = personFound.get();
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateFilteredPersonList(new PersonContainsKeywordsPredicate(Arrays.asList(splitName[0]),
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), MatchMode.OR));
        assertEquals(1, model.getFilteredPersonList().size());
    }

}
