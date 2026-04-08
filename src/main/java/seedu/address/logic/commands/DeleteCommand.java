package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashSet;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Id;
import seedu.address.model.person.Person;

/**
 * Deletes one or more persons with given IDs from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "del";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person(s) with the specified ID(s).\n"
            + "Parameters: ID... (each must be a positive integer)\n"
            + "Example:\n"
            + "\t" + COMMAND_WORD + " 1\n"
            + "\t" + COMMAND_WORD + " 1 2 3\n";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Alright, the contact(s) below have been removed "
            + "from your list!";

    public static final String MESSAGE_INVALID_ID = "That contact does not exist, "
            + "please enter a valid ID.";

    public static final String MESSAGE_SINGLE_CONTACT_ONLY = "The contact list size is only 1";

    public static final String MESSAGE_EMPTY_CONTACT_LIST = "The contact list is currently empty.";

    private final ArrayList<Id> targetIds;

    public DeleteCommand(ArrayList<Id> targetIds) {
        this.targetIds = targetIds;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        ArrayList<Person> personsToDelete = new ArrayList<Person>();
        //to remove duplicates
        HashSet<Id> uniqueIdToDelete = new HashSet<Id>(targetIds);
        for (Id id : uniqueIdToDelete) {
            Person person = model.findPersonById(id)
                    .orElseThrow(() -> new CommandException(String.format(Messages.MESSAGE_INVALID_PERSON_ID,
                    id.getValue())));
            personsToDelete.add(person);
        }

        model.deletePersons(personsToDelete);

        return new CommandResult(MESSAGE_DELETE_PERSON_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIds.equals(otherDeleteCommand.targetIds);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIds", targetIds)
                .toString();
    }
}
