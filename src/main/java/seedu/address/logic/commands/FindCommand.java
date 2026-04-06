package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MATCH_MODE_AND_KEYWORD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose name, address, phone, or tag contains
 * any of the argument keywords. Keyword matching is case insensitive for name, address, and tag.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Finds persons whose specified fields contain any of the given keywords.\n"
            + "By default, the search uses OR semantics. "
            + "To perform an AND search, specify "
            + PREFIX_MODE + MATCH_MODE_AND_KEYWORD + ".\n"
            + "Parameters: "
            + "[" + PREFIX_MODE + "MODE] "
            + "[" + PREFIX_NAME + "NAME]... "
            + "[" + PREFIX_PHONE + "PHONE]... "
            + "[" + PREFIX_ADDRESS + "ADDRESS]... "
            + "[" + PREFIX_REMARK + "REMARK]... "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example:\n"
            + "\t" + COMMAND_WORD + " " + PREFIX_NAME + "Ali " + PREFIX_NAME + "August "
            + PREFIX_ADDRESS + "119224\n"
            + "\t" + COMMAND_WORD + " " + PREFIX_MODE + MATCH_MODE_AND_KEYWORD + " " + PREFIX_TAG + "Student "
            + PREFIX_NAME + "Clement " + PREFIX_PHONE + "9234\n"
            + "\t" + COMMAND_WORD + " " + PREFIX_REMARK + "new student\n";

    private final PersonContainsKeywordsPredicate predicate;

    /**
     * Creates a {@code FindCommand} with the given predicate.
     *
     * @param predicate Predicate used to filter persons.
     * @throws NullPointerException if {@code predicate} is {@code null}.
     */
    public FindCommand(PersonContainsKeywordsPredicate predicate) {
        // predicate should never be null
        requireNonNull(predicate);
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
