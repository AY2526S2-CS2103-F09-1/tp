package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_LINK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.Set;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Id;
import seedu.address.model.person.Person;

/**
 * Copies a field of a person identified by their unique ID to the clipboard.
 */
public class CopyCommand extends Command {

    public static final String COMMAND_WORD = "copy";

    public static final String EMPTY_STRING = "";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Copies a field of the person identified by their ID to the clipboard.\n"
            + "Parameters: ID (must be a positive integer) FIELD ("
            + PREFIX_NAME + ", " + PREFIX_PHONE + ", " + PREFIX_ADDRESS + ", " + PREFIX_MEETING_LINK + ")\n"
            + "Example:\n"
            + "\t" + COMMAND_WORD + " 1 " + PREFIX_PHONE + "\n";

    public static final String MESSAGE_COPY_SUCCESS = "Copied %s's %s to clipboard!";

    public static final String MESSAGE_INVALID_FIELD = "Invalid field. The valid fields include: "
            + PREFIX_NAME + ", " + PREFIX_PHONE + ", " + PREFIX_ADDRESS + ", " + PREFIX_MEETING_LINK;

    public static final String MESSAGE_MISSING_FIELD = "Please specify a field to copy. Valid fields: "
            + PREFIX_NAME + ", " + PREFIX_PHONE + ", " + PREFIX_ADDRESS + ", " + PREFIX_MEETING_LINK;

    public static final String MESSAGE_EMPTY_FIELD_VALUE = "There is no %s to copy for this contact.";

    private static final Set<String> VALID_FIELDS = Set.of(
            PREFIX_NAME.getPrefix(), PREFIX_PHONE.getPrefix(), PREFIX_ADDRESS.getPrefix(),
            PREFIX_MEETING_LINK.getPrefix());

    private final Id targetId;
    private final String field;
    /**
     * @param targetId of the person in the address book to copy from
     * @param field the field to copy
     */
    public CopyCommand(Id targetId, String field) {
        // Defensive Programming
        requireNonNull(targetId);
        requireNonNull(field);
        if (!VALID_FIELDS.contains(field)) {
            throw new IllegalArgumentException("Invalid field: " + field);
        }
        this.targetId = targetId;
        this.field = field;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person personToCopy = model.findPersonById(targetId)
                .orElseThrow(() -> new CommandException(String.format(Messages.MESSAGE_INVALID_PERSON_ID,
                targetId.getValue())));
        String fieldLabel = getFieldLabel();
        String valueToCopy = getValidFieldValue(personToCopy, fieldLabel);

        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(valueToCopy);
        clipboard.setContent(content);

        return new CommandResult(String.format(MESSAGE_COPY_SUCCESS, personToCopy.getName(), fieldLabel));
    }

    private String getValidFieldValue(Person person, String fieldLabel) throws CommandException {
        String value = getFieldValue(person);
        if (value.equals(EMPTY_STRING)) {
            throw new CommandException(String.format(MESSAGE_EMPTY_FIELD_VALUE, fieldLabel));
        }
        return value;
    }

    private String getFieldValue(Person person) {
        if (field.equals(PREFIX_NAME.getPrefix())) {
            return person.getName().fullName;
        } else if (field.equals(PREFIX_PHONE.getPrefix())) {
            return person.getPhone().map(p -> p.value).orElse(EMPTY_STRING);
        } else if (field.equals(PREFIX_ADDRESS.getPrefix())) {
            return person.getAddress().map(a -> a.value).orElse(EMPTY_STRING);
        } else if (field.equals(PREFIX_MEETING_LINK.getPrefix())) {
            return person.getMeetingLink().map(m -> m.value).orElse(EMPTY_STRING);
        } else {
            assert false : "Unexpected invalid field: " + field;
            return EMPTY_STRING;
        }
    }

    private String getFieldLabel() {
        if (field.equals(PREFIX_NAME.getPrefix())) {
            return "name";
        } else if (field.equals(PREFIX_PHONE.getPrefix())) {
            return "phone number";
        } else if (field.equals(PREFIX_ADDRESS.getPrefix())) {
            return "address";
        } else if (field.equals(PREFIX_MEETING_LINK.getPrefix())) {
            return "meeting link";
        } else {
            assert false : "Unexpected invalid field: " + field;
            return EMPTY_STRING;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof CopyCommand)) {
            return false;
        }
        CopyCommand otherCopyCommand = (CopyCommand) other;
        return targetId.equals(otherCopyCommand.targetId)
                && field.equals(otherCopyCommand.field);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetId", targetId)
                .add("field", field)
                .toString();
    }
}
