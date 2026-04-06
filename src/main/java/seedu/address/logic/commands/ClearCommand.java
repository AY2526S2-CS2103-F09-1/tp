package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";
    public static final String MESSAGE_CONFIRMATION =
            "Warning! All your contacts will be cleared. If you intend to do so, please type `"
            + COMMAND_WORD + "` again.";

    private final boolean isConfirmed;

    public ClearCommand() {
        this(false);
    }

    public ClearCommand(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (isConfirmed) {
            model.setAddressBook(new AddressBook());
            return new CommandResult(MESSAGE_SUCCESS);
        }
        return new CommandResult(MESSAGE_CONFIRMATION);
    }
}
