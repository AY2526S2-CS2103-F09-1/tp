package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_MODE;
import static seedu.address.logic.Messages.MESSAGE_MORE_THAN_ONE_MODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.model.person.PersonContainsKeywordsPredicate.MatchMode;

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
                PREFIX_TAG, PREFIX_MODE);

        boolean hasPrefixes = arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_TAG);

        // Reject unprefixed input and any unexpected preamble before the first prefix.
        if (!hasPrefixes || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        List<String> nameKeywords = getSanitizedKeywords(argMultimap.getAllValues(PREFIX_NAME));
        List<String> addressKeywords = getSanitizedKeywords(argMultimap.getAllValues(PREFIX_ADDRESS));
        List<String> phoneKeywords = getSanitizedKeywords(argMultimap.getAllValues(PREFIX_PHONE));
        List<String> tagKeywords = getSanitizedKeywords(argMultimap.getAllValues(PREFIX_TAG));
        List<String> modeKeywords = getSanitizedKeywords(argMultimap.getAllValues(PREFIX_MODE));

        MatchMode modeKeyword;
        // By default, modeKeyword is OR
        if (modeKeywords.size() > 1) {
            throw new ParseException(String.format(MESSAGE_MORE_THAN_ONE_MODE, FindCommand.MESSAGE_USAGE));
        } else if (modeKeywords.isEmpty()) {
            modeKeyword = MatchMode.OR;
        } else if (modeKeywords.get(0).equalsIgnoreCase("and")) {
            modeKeyword = MatchMode.AND;
        } else if (modeKeywords.get(0).equalsIgnoreCase("or")) {
            modeKeyword = MatchMode.OR;
        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_MODE, FindCommand.MESSAGE_USAGE));
        }

        // Ensure there are no blank strings in Keywords
        assert nameKeywords.stream().noneMatch(String::isBlank);
        assert addressKeywords.stream().noneMatch(String::isBlank);
        assert phoneKeywords.stream().noneMatch(String::isBlank);
        assert tagKeywords.stream().noneMatch(String::isBlank);

        if (nameKeywords.isEmpty() && addressKeywords.isEmpty()
                && phoneKeywords.isEmpty() && tagKeywords.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return new FindCommand(new PersonContainsKeywordsPredicate(
                nameKeywords,
                addressKeywords,
                phoneKeywords,
                tagKeywords,
                modeKeyword
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
