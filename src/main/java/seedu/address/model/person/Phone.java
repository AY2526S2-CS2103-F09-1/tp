package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's phone number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPhone(String)}
 */
public class Phone {

    public static final String MESSAGE_CONSTRAINTS =
            "Phone numbers should only contain numbers, "
            + "start with only digits 6/8/9, "
            + "and it should be exactly 8 digits long.";
    public static final String VALIDATION_REGEX = "[689]\\d{7}";
    private static final String EMPTY_STRING = "";
    public final String value;

    /**
     * Constructs a {@code Phone}.
     *
     * @param phone A valid phone number.
     */
    public Phone(String phone) {
        requireNonNull(phone);
        checkArgument(isValidPhone(phone), MESSAGE_CONSTRAINTS);

        value = phone;
    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidPhone(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Checks if phone is valid or an empty string.
     * Used mainly when parsing from JSON file.
     * As the absent phone numbers will be stored as empty strings in the JSON file,
     * use this method to check validity when reading from a JSON file instead.
     *
     * @param phoneString phone number in string.
     * @return true if phone read from is valid or an empty string else false.
     */
    public static boolean isValidPhoneOrEmptyString(String phoneString) {
        return phoneString.equals(EMPTY_STRING) || phoneString.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Phone)) {
            return false;
        }

        Phone otherPhone = (Phone) other;
        return value.equals(otherPhone.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
