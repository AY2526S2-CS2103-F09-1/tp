package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;

import java.util.Map;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new TagCommand object.
 */
public class TagCommandParser implements Parser<TagCommand> {

    static final String MESSAGE_INVALID_CATEGORY =
            "Category must be one of the following values: Student, Parent, Tutor.";

    private static final Map<String, String> SUPPORTED_CATEGORIES = Map.of(
            "student", "Student",
            "parent", "Parent",
            "tutor", "Tutor");

    /**
     * Parses the given {@code String} of arguments in the context of the TagCommand
     * and returns a TagCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public TagCommand parse(String args) throws ParseException {
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(args, PREFIX_INDEX, PREFIX_CATEGORY_TAG);

        if (!arePrefixesPresent(argumentMultimap, PREFIX_INDEX, PREFIX_CATEGORY_TAG)
                || !argumentMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
        }

        argumentMultimap.verifyNoDuplicatePrefixesFor(PREFIX_INDEX, PREFIX_CATEGORY_TAG);

        Index targetIndex = ParserUtil.parseIndex(argumentMultimap.getValue(PREFIX_INDEX).get());
        Tag categoryTag = parseCategoryTag(argumentMultimap.getValue(PREFIX_CATEGORY_TAG).get());
        return new TagCommand(targetIndex, categoryTag);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    private static Tag parseCategoryTag(String categoryValue) throws ParseException {
        String trimmedCategory = categoryValue.trim();
        String normalizedCategory = SUPPORTED_CATEGORIES.get(trimmedCategory.toLowerCase());

        if (normalizedCategory == null) {
            throw new ParseException(MESSAGE_INVALID_CATEGORY);
        }

        return new Tag(normalizedCategory);
    }
}
