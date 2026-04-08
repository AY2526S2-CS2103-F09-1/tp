package seedu.address.model.person;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {
    // The id will be used to refer to this specific person in user inputs
    private final Id id;

    // Identity fields
    private final Name name;
    private final Optional<Phone> phone;
    private final Optional<Address> address;

    // Data fields
    private final Optional<Time> time;
    private final Set<Tag> tags = new HashSet<>();
    private final Optional<Remark> remark;
    private final Optional<MeetingLink> meetingLink;

    /**
     * Creates a {@code Person} with the provided fields.
     */
    public Person(
        Id id,
        Name name,
        Optional<Phone> phone,
        Optional<Address> address,
        Optional<Time> time,
        Set<Tag> tags,
        Optional<Remark> remark,
        Optional<MeetingLink> meetingLink
    ) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.time = time;
        this.tags.addAll(tags);
        this.remark = remark;
        this.meetingLink = meetingLink;
    }

    public Id getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Optional<Phone> getPhone() {
        return phone;
    }

    public Optional<Address> getAddress() {
        return address;
    }

    public Optional<Time> getTime() {
        return time;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public Optional<Remark> getRemark() {
        return remark;
    }

    public Optional<MeetingLink> getMeetingLink() {
        return meetingLink;
    }

    /**
     * Returns true if both persons have the same identity fields.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        // name and address equality checks are case-insensitive
        return otherPerson != null
                && otherPerson.getName().equals(getName())
                && otherPerson.getPhone().equals(getPhone())
                && otherPerson.getAddress().equals(getAddress());
    }

    /**
     * Checks whether two persons have the same {@code Id}.
     *
     * @param otherPerson other person to check {@code Id} for.
     * @return true if two persons have the same {@code Id}, else false.
     */
    public boolean isSameId(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        if (otherPerson == null) {
            return false;
        }

        Id otherId = otherPerson.getId();
        return otherId.equals(getId());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && address.equals(otherPerson.address)
                && time.equals(otherPerson.time)
                && tags.equals(otherPerson.tags)
                && remark.equals(otherPerson.remark)
                && meetingLink.equals(otherPerson.meetingLink);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, address, time, tags, remark, meetingLink);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("id", id)
                .add("name", name)
                .add("phone", phone)
                .add("address", address)
                .add("time", time)
                .add("tags", tags)
                .add("remark", remark)
                .add("meetingLink", meetingLink)
                .toString();
    }
}
