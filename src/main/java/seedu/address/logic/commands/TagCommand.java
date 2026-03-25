package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Id;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Tags a contact with a category.
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a categorical tag to the person with the specified ID.\n"
            + "Parameters: ID " + PREFIX_TAG + "CATEGORY\n"
            + "Example:\n"
            + "\t" + COMMAND_WORD + " 1 " + PREFIX_TAG + "Student\n";

    public static final String MESSAGE_TAG_PERSON_SUCCESS =
            "Alright, the tag %1$s has been added to the contact with ID %2$d.";
    public static final String MESSAGE_INVALID_PERSON_ID = "ID %1$d is not found in contact list.";

    private final Id targetId;
    private final Tag categoryTag;

    /**
     * Creates a TagCommand to tag the specified {@code Person}.
     */
    public TagCommand(Id targetId, Tag categoryTag) {
        requireNonNull(targetId);
        requireNonNull(categoryTag);
        this.targetId = targetId;
        this.categoryTag = categoryTag;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person personToTag = model.findPersonById(targetId)
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_INVALID_PERSON_ID,
                        targetId.getValue())));
        Person taggedPerson = createTaggedPerson(personToTag, categoryTag);
        model.setPerson(personToTag, taggedPerson);

        return new CommandResult(String.format(MESSAGE_TAG_PERSON_SUCCESS,
                categoryTag.tagName, targetId.getValue()));
    }

    /**
     * Returns a copy of {@code personToTag} that keeps the existing identity fields
     * and replaces all current tags with the selected category tag.
     */
    private static Person createTaggedPerson(Person personToTag, Tag categoryTag) {
        return new Person(personToTag.getId(), personToTag.getName(), personToTag.getPhone(),
                personToTag.getAddress(), Set.of(categoryTag));
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
        return targetId.equals(otherTagCommand.targetId)
                && categoryTag.equals(otherTagCommand.categoryTag);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetId", targetId)
                .add("categoryTag", categoryTag)
                .toString();
    }
}
