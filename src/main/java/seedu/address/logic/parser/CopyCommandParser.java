package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_LINK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.Set;

import seedu.address.logic.commands.CopyCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Id;

/**
 * Parses input arguments and creates a new CopyCommand object.
 */
public class CopyCommandParser implements Parser<CopyCommand> {

    private static final Set<String> VALID_FIELDS = Set.of(
            PREFIX_NAME.getPrefix(), PREFIX_PHONE.getPrefix(), PREFIX_ADDRESS.getPrefix(), PREFIX_MEETING_LINK.getPrefix());

    /**
     * Parses the given {@code String} of arguments in the context of the CopyCommand
     * and returns a CopyCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public CopyCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopyCommand.MESSAGE_USAGE));
        }

        String[] parts = trimmedArgs.split("\\s+");

        if (parts.length != 2) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopyCommand.MESSAGE_USAGE));
        }

        Id id = ParserUtil.parseId(parts[0]);
        String field = parts[1];

        if (!isValidField(field)) {
            throw new ParseException(CopyCommand.MESSAGE_INVALID_FIELD);
        }

        return new CopyCommand(id, field);
    }

    private boolean isValidField(String field) {
        return VALID_FIELDS.contains(field);
    }
}
