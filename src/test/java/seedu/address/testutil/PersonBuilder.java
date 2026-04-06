package seedu.address.testutil;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.person.Id;
import seedu.address.model.person.MeetingLink;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {
    public static final int DEFAULT_ID = 1;
    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_REMARK = "first student";
    public static final String DEFAULT_MEETING_LINK = "https://zoom.com/smt";

    private Id id;
    private Name name;
    private Optional<Phone> phone;
    private Optional<Address> address;
    private Set<Tag> tags;
    private Optional<Remark> remark;
    private Optional<MeetingLink> meetingLink;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        id = Id.of(DEFAULT_ID);
        name = new Name(DEFAULT_NAME);
        phone = Optional.of(new Phone(DEFAULT_PHONE));
        address = Optional.of(new Address(DEFAULT_ADDRESS));
        tags = new HashSet<>();
        remark = Optional.of(new Remark(DEFAULT_REMARK));
        meetingLink = Optional.empty();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        id = personToCopy.getId();
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        address = personToCopy.getAddress();
        tags = new HashSet<>(personToCopy.getTags());
        remark = personToCopy.getRemark();
        meetingLink = personToCopy.getMeetingLink();
    }

    /**
     * Sets the {@code Id} of the {@code Person} that we are building.
     */
    public PersonBuilder withId(int id) {
        this.id = Id.of(id);
        return this;
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = Optional.of(new Address(address));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = Optional.of(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building as empty.
     */
    public PersonBuilder withoutPhone() {
        this.phone = Optional.empty();
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building as empty.
     */
    public PersonBuilder withoutAddress() {
        this.address = Optional.empty();
        return this;
    }

    /**
     * Sets the {@code Remark} of the {@code Person} that we are building.
     */
    public PersonBuilder withRemark(String remarkString) {
        this.remark = Optional.of(new Remark(remarkString));
        return this;
    }

    /**
     * Sets the {@code Remark} of the {@code Person} that we are building as empty.
     */
    public PersonBuilder withoutRemark() {
        this.remark = Optional.empty();
        return this;
    }

    public Person build() {
        return new Person(id, name, phone, address, tags, remark, meetingLink);
    }

}
