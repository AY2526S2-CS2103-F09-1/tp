package seedu.address.model.person;

import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a Person's id in the address book.
 * Guarantees: immutable.
 */
public class Id implements Comparable<Id> {
    public static final String MESSAGE_CONSTRAINTS =
            "Ids should not be blank, and must be a positive integer.";
    private static final int SMALLEST_VALUE = 1;
    public final int value;

    /**
     * Creates a new {@code Id} with the smallest possible value.
     */
    private Id() {
        this(SMALLEST_VALUE);
    }

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
        // increment by 1 to avoid duplicated ids
        return currentMaxId.map(maxId -> new Id(maxId.value + 1))
                .orElse(new Id());
    }

    /**
     * Returns true if a given int is a valid id.
     */
    public static boolean isValidId(int test) {
        return test >= SMALLEST_VALUE;
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
