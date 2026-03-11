package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.tag.Tag;

/**
 * Tags a contact with a category.
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Tags the person identified by the index number "
            + "used in the displayed person list with a category.\n"
            + "Parameters: /index INDEX /tag CATEGORY\n"
            + "Example: " + COMMAND_WORD + " /index 1 /tag Student";

    public static final String MESSAGE_NOT_YET_AVAILABLE =
            "Tag command execution will be added in the next increment.";

    private final Index targetIndex;
    private final Tag categoryTag;

    /**
     * Creates a TagCommand to tag the specified {@code Person}.
     */
    public TagCommand(Index targetIndex, Tag categoryTag) {
        requireNonNull(targetIndex);
        requireNonNull(categoryTag);
        this.targetIndex = targetIndex;
        this.categoryTag = categoryTag;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        throw new CommandException(MESSAGE_NOT_YET_AVAILABLE);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof TagCommand)) {
            return false;
        }

        TagCommand otherTagCommand = (TagCommand) other;
        return targetIndex.equals(otherTagCommand.targetIndex)
                && categoryTag.equals(otherTagCommand.categoryTag);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("categoryTag", categoryTag)
                .toString();
    }
}
