package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Id;
import seedu.address.model.person.Person;

/**
 * Deletes a person with a given id from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "del";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person with the specified ID.\n"
            + "Parameters: ID (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Alright, the contact below has been removed "
            + "from your list!";

    public static final String MESSAGE_INVALID_ID = "That contact does not exist, "
            + "please enter a valid ID.";

    public static final String MESSAGE_SINGLE_CONTACT_ONLY = "The contact list size is only 1";

    public static final String MESSAGE_EMPTY_CONTACT_LIST = "The contact list is currently empty.";

    public static final String MESSAGE_TOO_MANY_ARGUMENTS = "Too many arguments\n%s";

    private final Id targetId;

    public DeleteCommand(Id targetId) {
        this.targetId = targetId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person personToDelete = model.findPersonById(targetId)
                .orElseThrow(() -> new CommandException(String.format(Messages.MESSAGE_INVALID_PERSON_ID,
                        targetId.getValue())));
        model.deletePerson(personToDelete);

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
        return targetId.equals(otherDeleteCommand.targetId);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetId", targetId)
                .toString();
    }

    private String getInvalidIdMessage(int listSize) {
        if (listSize == 0) {
            return MESSAGE_EMPTY_CONTACT_LIST;
        }
        if (listSize == 1) {
            return MESSAGE_SINGLE_CONTACT_ONLY;
        }
        return String.format(MESSAGE_INVALID_ID, listSize);
    }
}
