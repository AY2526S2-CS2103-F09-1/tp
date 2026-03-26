package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Id;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {
    private final Optional<Id> currentMaxId;

    public AddCommandParser(Optional<Id> currentMaxId) {
        this.currentMaxId = currentMaxId;
    }

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_ADDRESS, PREFIX_TAG);

        // NOTE: removed PREFIX_PHONE and PREFIX_ADDRESS from arePrefixesPresent() method so that
        // user is able to not specify it
        if (!arePrefixesPresent(argMultimap, PREFIX_NAME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_ADDRESS);

        // find the new id for the new person to be added,
        // based on the maximum id that is saved in the address book currently
        Id id = Id.fromCurrentMaxId(currentMaxId);

        Optional<String> nameToBeParsed = argMultimap.getValue(PREFIX_NAME);
        assert nameToBeParsed.isPresent() : "The name to be parsed is still empty,"
                + "even after checking for whether the name prefix was present in the input.";
        Name name = ParserUtil.parseName(nameToBeParsed.get());

        Optional<Phone> phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE));

        Address address = ParserUtil.parseAddress(
            argMultimap.getValue(PREFIX_ADDRESS).orElse(Parser.DEFAULT_EMPTY_STRING)
        );

        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Person person = new Person(id, name, phone, address, tagList);

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
