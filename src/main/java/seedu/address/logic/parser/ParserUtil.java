package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MATCH_MODE_AND_KEYWORD;
import static seedu.address.logic.Messages.MATCH_MODE_OR_KEYWORD;
import static seedu.address.logic.Messages.MESSAGE_INVALID_MODE;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Id;
import seedu.address.model.person.MeetingLink;
import seedu.address.model.person.Name;
import seedu.address.model.person.PersonContainsKeywordsPredicate.MatchMode;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Time;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    /**
     * Parses {@code idString} into an {@code Id} and returns it.
     * Leading and trailing whitespaces will be trimmed.
     * @throws ParseException if the specified id is invalid (not a positive integer that can be parsed).
     */
    public static Id parseId(String idString) throws ParseException {
        requireNonNull(idString);
        String trimmedId = idString.trim();

        // check if id obtained from the input
        // can be parsed into an int value
        int parsedIdValue;
        try {
            parsedIdValue = Integer.parseInt(trimmedId);
        } catch (NumberFormatException ex) {
            throw new ParseException(Id.MESSAGE_CONSTRAINTS);
        }

        if (!Id.isValidId(parsedIdValue)) {
            throw new ParseException(Id.MESSAGE_CONSTRAINTS);
        }

        return Id.of(parsedIdValue);
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Optional<Phone> parsePhone(Optional<String> phone) throws ParseException {
        requireNonNull(phone);

        // need another check here to see if inside is empty string, if so then give optional empty
        phone = phone.filter(phoneString -> !phoneString.isEmpty());
        if (phone.isEmpty()) {
            return Optional.empty();
        }

        Phone parsedPhone = phone.map(phoneString -> requireNonNull(phoneString))
            .map(phoneString -> phoneString.trim())
            .filter(trimmedPhoneString -> Phone.isValidPhone(trimmedPhoneString))
            .map(trimmedPhoneString -> new Phone(trimmedPhoneString))
            .orElseThrow(() -> new ParseException(Phone.MESSAGE_CONSTRAINTS));

        return Optional.of(parsedPhone);
    }

    /**
     * Parses an optional {@code String address} into an optional {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Optional<Address> parseAddress(Optional<String> address) throws ParseException {
        requireNonNull(address);

        address = address.filter(addressString -> !addressString.isEmpty());
        if (address.isEmpty()) {
            return Optional.empty();
        }

        Address parsedAddress = address.map(addressString -> requireNonNull(addressString))
                .map(addressString -> addressString.trim())
                .filter(Address::isValidAddress)
                .map(Address::new)
                .orElseThrow(() -> new ParseException(Address.MESSAGE_CONSTRAINTS));

        return Optional.of(parsedAddress);
    }

    /**
     * Parses an optional {@code String time} into an optional {@code Time}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code time} is invalid.
     */
    public static Optional<Time> parseTime(Optional<String> time) throws ParseException {
        requireNonNull(time);

        time = time.filter(timeString -> !timeString.isEmpty());
        if (time.isEmpty()) {
            return Optional.empty();
        }

        Time parsedTime = time.map(timeString -> requireNonNull(timeString))
                .map(String::trim)
                .filter(Time::isValidTime)
                .map(Time::new)
                .orElseThrow(() -> new ParseException(Time.MESSAGE_CONSTRAINTS));

        return Optional.of(parsedTime);
    }

    /**
     * Parses an optional {@code String remark} into an optional {@code Remark}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code remark} is invalid.
     */
    public static Optional<Remark> parseRemark(Optional<String> remark) throws ParseException {
        requireNonNull(remark);

        // need another check here to see if inside is empty string, if so then give
        // optional empty
        remark = remark.filter(remarkString -> !remarkString.isEmpty());
        if (remark.isEmpty()) {
            return Optional.empty();
        }

        Remark parsedRemark = remark.map(remarkString -> requireNonNull(remarkString))
                .map(remarkString -> remarkString.trim())
                .filter(trimmedRemarkString -> Remark.isValidRemark(trimmedRemarkString))
                .map(trimmedRemarkString -> new Remark(trimmedRemarkString))
                .orElseThrow(() -> new ParseException(Remark.MESSAGE_CONSTRAINTS));

        return Optional.of(parsedRemark);
    }

    /**
     * Parses a {@code String tag} into a valid {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        String normalizedTagName = Tag.getNormalizedTagName(trimmedTag);
        if (normalizedTagName == null) {
            throw new ParseException(Tag.MESSAGE_TAG_CONSTRAINTS);
        }

        return new Tag(normalizedTagName);
    }

    /**
     * Parses an optional {@code String link} into an optional {@code MeetingLink}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code link} is invalid.
     */
    public static Optional<MeetingLink> parseMeetingLink(Optional<String> link) throws ParseException {
        requireNonNull(link);

        link = link.filter(linkString -> !linkString.isEmpty());
        if (link.isEmpty()) {
            return Optional.empty();
        }

        MeetingLink parsedLink = link.map(linkString -> requireNonNull(linkString))
                .map(linkString -> linkString.trim())
                .filter(MeetingLink::isValidMeetingLink)
                .map(MeetingLink::new)
                .orElseThrow(() -> new ParseException(MeetingLink.MESSAGE_CONSTRAINTS));

        return Optional.of(parsedLink);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String} mode keyword into a {@code MatchMode}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given mode keyword is not {@code "and"} or {@code "or"}
     */
    public static MatchMode parseMatchMode(String stringModeKeyword) throws ParseException {
        String normalizedModeKeyword = stringModeKeyword.trim().toLowerCase();

        if (MATCH_MODE_AND_KEYWORD.equals(normalizedModeKeyword)) {
            return MatchMode.AND;
        }
        if (MATCH_MODE_OR_KEYWORD.equals(normalizedModeKeyword)) {
            return MatchMode.OR;
        }

        throw new ParseException(String.format(MESSAGE_INVALID_MODE, FindCommand.MESSAGE_USAGE));
    }
}
