package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Id;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
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
     * Parses a {@code String tag} into a supported category {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid or not a supported category.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        String normalizedCategoryTagName = Tag.getNormalizedCategoryTagName(trimmedTag);
        if (normalizedCategoryTagName == null) {
            throw new ParseException(Tag.MESSAGE_TAG_CONSTRAINTS);
        }

        return new Tag(normalizedCategoryTagName);
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

}
