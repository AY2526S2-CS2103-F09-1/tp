package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "del";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Alright, the contact below has been removed "
            + "from your list!";

    public static final String MESSAGE_INVALID_INDEX = "That contact does not exist, "
            + "please enter a valid index between 1 and %1$d";

    public static final String MESSAGE_SINGLE_CONTACT_ONLY = "The contact list size is only 1";

    public static final String MESSAGE_EMPTY_CONTACT_LIST = "The contact list is currently empty.";

    public static final String MESSAGE_TOO_MANY_ARGUMENTS = "Too many arguments\n%s";

    private final Index targetIndex;

    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            String invalidIndexMessage = getInvalidIndexMessage(lastShownList.size());
            throw new CommandException(invalidIndexMessage);
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
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
        return targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }

    private String getInvalidIndexMessage(int listSize) {
        if (listSize == 0) {
            return MESSAGE_EMPTY_CONTACT_LIST;
        }
        if (listSize == 1) {
            return MESSAGE_SINGLE_CONTACT_ONLY;
        }
        return String.format(MESSAGE_INVALID_INDEX, listSize);
    }
}
