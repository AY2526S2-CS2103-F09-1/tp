package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Id;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {
    private static final String TAG_RESET_VALUE = "";

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_ADDRESS, PREFIX_TAG, PREFIX_REMARK);

        Id id;

        try {
            id = ParserUtil.parseId(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_ADDRESS, PREFIX_REMARK);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        populateNameIfExists(argMultimap, editPersonDescriptor);
        populatePhoneIfExists(argMultimap, editPersonDescriptor);
        populateAddressIfExists(argMultimap, editPersonDescriptor);
        populateTagsIfExists(argMultimap, editPersonDescriptor);
        populateRemarkIfExists(argMultimap, editPersonDescriptor);

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(id, editPersonDescriptor);
    }

    private void populateRemarkIfExists(ArgumentMultimap argMultimap, EditPersonDescriptor editPersonDescriptor)
            throws ParseException {
        if (argMultimap.getValue(PREFIX_REMARK).isPresent()) {
            Optional<Remark> optionalRemark = ParserUtil.parseRemark(argMultimap.getValue(PREFIX_REMARK));
            editPersonDescriptor.setRemark(optionalRemark);
        }
    }

    private void populateTagsIfExists(ArgumentMultimap argMultimap, EditPersonDescriptor editPersonDescriptor)
            throws ParseException {
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editPersonDescriptor::setTags);
    }

    private void populateAddressIfExists(ArgumentMultimap argMultimap, EditPersonDescriptor editPersonDescriptor)
            throws ParseException {
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editPersonDescriptor.setAddress(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
        }
    }

    private void populatePhoneIfExists(ArgumentMultimap argMultimap, EditPersonDescriptor editPersonDescriptor)
            throws ParseException {
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            Optional<Phone> optionalPhone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE));
            editPersonDescriptor.setPhone(optionalPhone);
        }
    }

    private void populateNameIfExists(ArgumentMultimap argMultimap, EditPersonDescriptor editPersonDescriptor)
            throws ParseException {
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        requireNonNull(tags);

        if (tags.isEmpty()) {
            return Optional.empty();
        }

        // A bare t/ clears all tags, but it cannot be combined with category values.
        if (isTagReset(tags)) {
            return Optional.of(Collections.emptySet());
        }
        if (containsTagResetValue(tags)) {
            throw new ParseException(EditCommand.MESSAGE_INVALID_TAG_RESET);
        }

        return Optional.of(ParserUtil.parseTags(tags));
    }

    private static boolean isTagReset(Collection<String> tags) {
        return tags.size() == 1 && containsTagResetValue(tags);
    }

    private static boolean containsTagResetValue(Collection<String> tags) {
        return tags.contains(TAG_RESET_VALUE);
    }

}
