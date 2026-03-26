package seedu.address.model.person;

import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Optional;

/**
 * Represents a Person's id in the address book.
 * Guarantees: immutable.
 */
public class Id implements Comparable<Id> {
    public static final String MESSAGE_CONSTRAINTS =
            "IDs should not be blank, and must be a positive integer.";
    public static final String OVERFLOW_MESSAGE =
            "The provided ID is already of the maximum possible value, "
            + "and will lead to an overflow.";

    private static final int SMALLEST_VALUE = 1;
    private static final int LARGEST_VALUE = Integer.MAX_VALUE;
    private final int value;

    private Id(int value) {
        checkArgument(isValidId(value), MESSAGE_CONSTRAINTS);
        this.value = value;
    }

    /**
     * Creates a new {@code Id} with the specified value.
     */
    public static Id of(int value) {
        return new Id(value);
    }

    /**
     * Creates a new {@code Id} using the maximum id that is saved in the address book currently.
     */
    public static Id fromCurrentMaxId(Optional<Id> currentMaxId) {
        // for when the current address book is empty
        // and there is no max id
        int emptyMaxIdValue = SMALLEST_VALUE - 1;

        Optional<Integer> currentMaxIdValue = currentMaxId
                // get current id value
                .map(maxId -> maxId.value)
                .or(() -> Optional.of(emptyMaxIdValue));

        return currentMaxIdValue
                // if current id cannot be incremented further, we need to
                // throw an exception to prevent overflow
                .filter(idValue -> idValue < LARGEST_VALUE)
                // increment by 1 to avoid duplicated ids
                .map(idValue -> new Id(idValue + 1))
                .orElseThrow(() -> new IllegalArgumentException(OVERFLOW_MESSAGE));
    }

    /**
     * Returns true if a given int is a valid id.
     */
    public static boolean isValidId(int test) {
        return test >= SMALLEST_VALUE;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Id)) {
            return false;
        }

        Id otherId = (Id) other;
        return this.value == otherId.value;
    }

    @Override
    public int compareTo(Id other) {
        return this.value - other.value;
    }

    @Override
    public int hashCode() {
        return this.value;
    }

}
