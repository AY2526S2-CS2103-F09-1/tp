package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_ADDRESS,
                PREFIX_TAG);

        boolean hasPrefixes = arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_TAG);

        // Reject unprefixed input and any unexpected preamble before the first prefix.
        if (!hasPrefixes || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        List<String> keywords = new ArrayList<>();
        keywords.addAll(argMultimap.getAllValues(PREFIX_NAME));
        keywords.addAll(argMultimap.getAllValues(PREFIX_ADDRESS));
        keywords.addAll(argMultimap.getAllValues(PREFIX_PHONE));
        keywords.addAll(argMultimap.getAllValues(PREFIX_TAG));

        keywords.removeIf(String::isBlank);

        if (keywords.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return new FindCommand(new PersonContainsKeywordsPredicate(
                keywords,
                arePrefixesPresent(argMultimap, PREFIX_NAME),
                arePrefixesPresent(argMultimap, PREFIX_ADDRESS),
                arePrefixesPresent(argMultimap, PREFIX_PHONE),
                arePrefixesPresent(argMultimap, PREFIX_TAG)));
    }

    /**
     * Returns true if any of the specified prefixes has at least one value in the
     * given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Arrays.stream(prefixes)
                .anyMatch(prefix -> !argumentMultimap.getAllValues(prefix).isEmpty());
    }
}
