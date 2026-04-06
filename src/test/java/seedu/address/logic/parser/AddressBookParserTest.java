package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_CANNOT_USE_MODE;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_PARENT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_STUDENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_DELETE;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIds.ID_FIRST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.CopyCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Id;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.model.person.PersonContainsKeywordsPredicate.MatchMode;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();
    private final Optional<Id> currentMaxId = Optional.empty();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person),
                currentMaxId);
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_copy() throws Exception {
        CopyCommand command = (CopyCommand) parser.parseCommand(
                CopyCommand.COMMAND_WORD + " " + ID_FIRST.getValue() + " " + PREFIX_PHONE,
                currentMaxId);
        assertEquals(new CopyCommand(ID_FIRST, PREFIX_PHONE.getPrefix()), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD,
                currentMaxId) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3",
                currentMaxId) instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + ID_FIRST.getValue(),
                currentMaxId);
        ArrayList<Id> ids = new ArrayList<Id>();
        ids.add(ID_FIRST);
        assertEquals(new DeleteCommand(ids), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();

        String userInput = EditCommand.COMMAND_WORD + " " + ID_FIRST.getValue()
                + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor);

        EditCommand command = (EditCommand) parser.parseCommand(userInput,
                currentMaxId);
        assertEquals(new EditCommand(ID_FIRST, descriptor), command);
    }

    @Test
    public void parseCommand_editWithTags() throws Exception {
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withTags(VALID_TAG_STUDENT, VALID_TAG_PARENT).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + ID_FIRST.getValue() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor),
                currentMaxId);
        assertEquals(new EditCommand(ID_FIRST, descriptor), command);
    }

    @Test
    public void parseCommand_editWithDeletedCategories() throws Exception {
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withTagsToDelete(VALID_TAG_STUDENT).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + ID_FIRST.getValue() + " " + PREFIX_TAG_DELETE + VALID_TAG_STUDENT,
                currentMaxId);
        assertEquals(new EditCommand(ID_FIRST, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD,
                currentMaxId) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3",
                currentMaxId) instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " "
                        + PREFIX_NAME + String.join(" " + PREFIX_NAME, keywords),
                currentMaxId);
        assertEquals(new FindCommand(new PersonContainsKeywordsPredicate(
                keywords, Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(), MatchMode.OR)), command);
    }

    @Test
    public void parseCommand_findTag() throws Exception {
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + PREFIX_TAG + "Student",
                currentMaxId);
        assertEquals(new FindCommand(new PersonContainsKeywordsPredicate(
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                Collections.singletonList("Student"), Collections.emptyList(), MatchMode.OR)), command);
    }

    @Test
    public void parseCommand_findAndMode() throws Exception {
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + PREFIX_MODE + "and "
                        + PREFIX_NAME + "Benson " + PREFIX_TAG + "Parent",
                currentMaxId);
        assertEquals(new FindCommand(new PersonContainsKeywordsPredicate(
                Collections.singletonList("Benson"), Collections.emptyList(), Collections.emptyList(),
                Collections.singletonList("Parent"), Collections.emptyList(), MatchMode.AND)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD,
                currentMaxId) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3",
                currentMaxId) instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD,
                currentMaxId) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3",
                currentMaxId) instanceof ListCommand);
    }

    @Test
    public void parseCommand_nonFindCommandWithModePrefix_throwsParseException() {
        assertThrows(ParseException.class,
                MESSAGE_CANNOT_USE_MODE, () -> parser.parseCommand(
                    ClearCommand.COMMAND_WORD + " " + PREFIX_MODE + "and",
                        currentMaxId));

        assertThrows(ParseException.class,
                MESSAGE_CANNOT_USE_MODE, () -> parser.parseCommand(
                    ListCommand.COMMAND_WORD + " " + PREFIX_MODE + "xor",
                        currentMaxId));

        assertThrows(ParseException.class,
                MESSAGE_CANNOT_USE_MODE, () -> parser.parseCommand(
                    HelpCommand.COMMAND_WORD + " " + PREFIX_MODE + "foo",
                        currentMaxId));

        assertThrows(ParseException.class,
                MESSAGE_CANNOT_USE_MODE, () -> parser.parseCommand(
                    ExitCommand.COMMAND_WORD + " " + PREFIX_MODE + "or",
                        currentMaxId));
    }

    @Test
    public void parseCommand_addEndingWithModePrefix_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parseCommand(
                    AddCommand.COMMAND_WORD + " " + PREFIX_NAME + "Ali "
                            + "a/1A Kent Ridge Rd " + PREFIX_MODE + "and",
                    currentMaxId));
    }

    @Test
    public void parseCommand_editWithModePrefix_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parseCommand(
                    EditCommand.COMMAND_WORD + " " + PREFIX_TAG + "student "
                            + PREFIX_MODE + "and",
                    currentMaxId));
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        HelpCommand.MESSAGE_USAGE), () -> parser.parseCommand("", currentMaxId));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class,
                MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand", currentMaxId));
    }

    @Test
    public void parseCommand_unknownCommandWithModePrefix_throwsUnknownCommand() {
        assertThrows(ParseException.class,
                MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand(
                    "fin " + PREFIX_MODE + "and " + PREFIX_NAME + "Amy", currentMaxId));

        assertThrows(ParseException.class,
                MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand(
                    "helpme " + PREFIX_MODE + "foo", currentMaxId));
    }

    @Test
    public void parseCommand_removedTagCommand_throwsParseException() {
        assertThrows(ParseException.class,
                MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("tag 1 t/Student", currentMaxId));
    }
}
