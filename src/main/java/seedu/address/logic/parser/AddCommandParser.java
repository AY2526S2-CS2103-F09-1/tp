package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_LINK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Id;
import seedu.address.model.person.MeetingLink;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Time;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {
    private final Optional<Id> currentMaxId;

    /**
     * Creates a new {@code AddCommandParser}.
     *
     * @param currentMaxId The maximum {@code Id} in the address book, at the time of
     *                     {@code AddCommandParser} initialization. If the address book
     *                     is empty, this will be {@code Optional.empty}. Used to determine
     *                     the {@code Id} of the new contact to be added.
     */
    public AddCommandParser(Optional<Id> currentMaxId) {
        assert currentMaxId != null;
        this.currentMaxId = currentMaxId;
    }

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME,
                PREFIX_PHONE, PREFIX_ADDRESS,
                PREFIX_TIME, PREFIX_TAG,
                PREFIX_REMARK, PREFIX_MEETING_LINK);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_ADDRESS, PREFIX_TIME,
                PREFIX_REMARK, PREFIX_MEETING_LINK);

        // find the new id for the new person to be added,
        // based on the maximum id that is saved in the address book currently
        Id id = Id.fromCurrentMaxId(currentMaxId);

        Optional<String> nameToBeParsed = argMultimap.getValue(PREFIX_NAME);
        assert nameToBeParsed.isPresent() : "The name to be parsed is still empty,"
                + "even after checking for whether the name prefix was present in the input.";
        Name name = ParserUtil.parseName(nameToBeParsed.get());

        Optional<Phone> phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE));

        Optional<Address> address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS));

        Optional<Time> time = ParserUtil.parseTime(argMultimap.getValue(PREFIX_TIME));

        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Optional<Remark> remark = ParserUtil.parseRemark(argMultimap.getValue(PREFIX_REMARK));

        Optional<MeetingLink> meetingLink = ParserUtil.parseMeetingLink(argMultimap.getValue(PREFIX_MEETING_LINK));

        // none of the parsed fields should be null
        assert name != null : "Parsed name should not be null";
        assert phone != null : "Parsed phone should not be null";
        assert address != null : "Parsed address should not be null";
        assert time != null : "Parsed time should not be null";
        assert tagList != null : "Parsed tags should not be null";
        assert remark != null : "Parsed remark should not be null";
        assert meetingLink != null : "Parsed meeting link should not be null";

        Person person = new Person(id, name, phone, address, time, tagList, remark, meetingLink);
        assert person != null : "Parsed person should not be null";

        return new AddCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
