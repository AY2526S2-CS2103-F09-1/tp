package seedu.address.logic;

import static seedu.address.logic.parser.CliSyntax.PREFIX_MODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_DELETE;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonContainsKeywordsPredicate.MatchMode;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_ID = "ID %1$d is not found in the address book.";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MATCH_MODE_AND_KEYWORD = MatchMode.AND.name().toLowerCase();
    public static final String MATCH_MODE_OR_KEYWORD = MatchMode.OR.name().toLowerCase();
    public static final String MESSAGE_CANNOT_USE_MODE =
            "The " + PREFIX_MODE + " prefix is not allowed in this command.";
    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the contact list.";
    public static final String MESSAGE_INVALID_TAG_RESET =
            "The tag reset prefix " + PREFIX_TAG + " cannot be combined with tag values or "
                    + PREFIX_TAG_DELETE + " values.";
    public static final String MESSAGE_CONFLICTING_TAG_EDITS =
            "A tag cannot be both added and deleted in the same command.";
    public static final String MESSAGE_INVALID_MODE =
            "Mode must be '" + MATCH_MODE_AND_KEYWORD + "' or '" + MATCH_MODE_OR_KEYWORD + "'. "
                    + "Use " + PREFIX_MODE + MATCH_MODE_AND_KEYWORD + " or "
                    + PREFIX_MODE + MATCH_MODE_OR_KEYWORD + ". If omitted, the default is "
                    + PREFIX_MODE + MATCH_MODE_OR_KEYWORD + ".";
    private static final String EMPTY_STRING = "";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone().map(x -> x.toString()).orElse(EMPTY_STRING))
                .append("; Address: ")
                .append(person.getAddress().map(address -> address.toString()).orElse(EMPTY_STRING))
                .append("; Tags: ");
        person.getTags().forEach(builder::append);
        builder.append("; Remark: ")
                .append(person.getRemark().map(x -> x.toString()).orElse(EMPTY_STRING))
                .append("; Meeting Link: ")
                .append(person.getMeetingLink().map(x -> x.toString()).orElse(EMPTY_STRING));
        return builder.toString();
    }

}
