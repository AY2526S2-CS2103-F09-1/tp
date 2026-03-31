package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
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

    // Defensive programming and abstraction
    private static List<String> getSanitizedKeywords(List<String> keywords) {
        requireNonNull(keywords);
        List<String> sanitizedKeywords = new ArrayList<>(keywords);
        sanitizedKeywords.removeIf(String::isBlank);
        return sanitizedKeywords;
    }

    /**
     * Parses the given {@code String} of arguments in the context of the
     * FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_ADDRESS,
            PREFIX_TAG, PREFIX_REMARK);

        boolean hasPrefixes = arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE,
            PREFIX_TAG, PREFIX_REMARK);

        // Reject unprefixed input and any unexpected preamble before the first prefix.
        if (!hasPrefixes || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        List<String> nameKeywords = getSanitizedKeywords(argMultimap.getAllValues(PREFIX_NAME));
        List<String> addressKeywords = getSanitizedKeywords(argMultimap.getAllValues(PREFIX_ADDRESS));
        List<String> phoneKeywords = getSanitizedKeywords(argMultimap.getAllValues(PREFIX_PHONE));
        List<String> tagKeywords = getSanitizedKeywords(argMultimap.getAllValues(PREFIX_TAG));
        List<String> remarkKeywords = getSanitizedKeywords(argMultimap.getAllValues(PREFIX_REMARK));

        // Ensure there are no blank strings in Keywords
        assert nameKeywords.stream().noneMatch(String::isBlank);
        assert addressKeywords.stream().noneMatch(String::isBlank);
        assert phoneKeywords.stream().noneMatch(String::isBlank);
        assert tagKeywords.stream().noneMatch(String::isBlank);
        assert remarkKeywords.stream().noneMatch(String::isBlank);

        if (nameKeywords.isEmpty() && addressKeywords.isEmpty()
                && phoneKeywords.isEmpty() && tagKeywords.isEmpty() && remarkKeywords.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return new FindCommand(new PersonContainsKeywordsPredicate(
                nameKeywords,
                addressKeywords,
                phoneKeywords,
                tagKeywords,
                remarkKeywords,
                PersonContainsKeywordsPredicate.MatchMode.OR
        ));
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
