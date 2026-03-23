package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

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

    public static final String FIELD_NAME = "/n";
    public static final String FIELD_PHONE = "/p";
    public static final String FIELD_ADDRESS = "/a";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Copies a field of the person identified by their ID to the clipboard.\n"
            + "Parameters: ID (must be a positive integer) FIELD ("
            + FIELD_NAME + ", " + FIELD_PHONE + ", " + FIELD_ADDRESS + ")\n"
            + "Example: " + COMMAND_WORD + " 1 " + FIELD_PHONE;

    public static final String MESSAGE_COPY_SUCCESS = "Copied %s's %s to clipboard!";

    public static final String MESSAGE_INVALID_FIELD = "Invalid field. The valid fields include: "
            + FIELD_NAME + ", " + FIELD_PHONE + ", " + FIELD_ADDRESS;

    public static final String MESSAGE_MISSING_FIELD = "Please specify a field to copy. Valid fields: "
            + FIELD_NAME + ", " + FIELD_PHONE + ", " + FIELD_ADDRESS;

    public static final String MESSAGE_EMPTY_FIELD_VALUE = "There is no %s to copy for this contact.";

    private final Id targetId;
    private final String field;
    /**
     * @param targetId of the person in the address book to copy from
     * @param field the field to copy
     */
    public CopyCommand(Id targetId, String field) {
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
        if (value.equals("")) {
            throw new CommandException(String.format(MESSAGE_EMPTY_FIELD_VALUE, fieldLabel));
        }
        return value;
    }

    private String getFieldValue(Person person) {
        switch (field) {
        case FIELD_NAME:
            return person.getName().fullName;
        case FIELD_PHONE:
            return person.getPhone().value;
        case FIELD_ADDRESS:
            return person.getAddress().value;
        default:
            assert false : "Invalid field: " + field;
            return "";
        }
    }

    private String getFieldLabel() {
        switch (field) {
        case FIELD_NAME:
            return "name";
        case FIELD_PHONE:
            return "phone number";
        case FIELD_ADDRESS:
            return "address";
        default:
            assert false : "Invalid field: " + field;
            return "";
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
