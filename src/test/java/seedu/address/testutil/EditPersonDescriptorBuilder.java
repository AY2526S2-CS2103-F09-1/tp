package seedu.address.testutil;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.Address;
import seedu.address.model.person.MeetingLink;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Time;
import seedu.address.model.tag.Tag;

/**
 * A utility class to help with building EditPersonDescriptor objects.
 */
public class EditPersonDescriptorBuilder {

    private EditPersonDescriptor descriptor;

    public EditPersonDescriptorBuilder() {
        descriptor = new EditPersonDescriptor();
    }

    public EditPersonDescriptorBuilder(EditPersonDescriptor descriptor) {
        this.descriptor = new EditPersonDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPersonDescriptor} with fields containing {@code person}'s details
     */
    public EditPersonDescriptorBuilder(Person person) {
        descriptor = new EditPersonDescriptor();
        descriptor.setName(person.getName());
        descriptor.setPhone(person.getPhone());
        descriptor.setAddress(person.getAddress());
        descriptor.setTime(person.getTime());
        descriptor.setTags(person.getTags());
        descriptor.setRemark(person.getRemark());
        descriptor.setMeetingLink(person.getMeetingLink());
    }

    /**
     * Sets the {@code Name} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(Optional.of(new Phone(phone)));
        return this;
    }

    /**
     * Clears the {@code Phone} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withoutPhone() {
        descriptor.setPhone(Optional.empty());
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(Optional.of(new Address(address)));
        return this;
    }

    /**
     * Clears the {@code Address} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withoutAddress() {
        descriptor.setAddress(Optional.empty());
        return this;
    }

    /**
     * Sets the {@code Time} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withTime(String time) {
        descriptor.setTime(Optional.of(new Time(time)));
        return this;
    }

    /**
     * Clears the {@code Time} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withoutTime() {
        descriptor.setTime(Optional.empty());
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditPersonDescriptor}
     * that we are building.
     */
    public EditPersonDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code tagsToDelete}
     * of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withTagsToDelete(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTagsToDelete(tagSet);
        return this;
    }

    /**
     * Sets the {@code Remark} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withRemark(String remark) {
        descriptor.setRemark(Optional.of(new Remark(remark)));
        return this;
    }

    /**
     * Sets the {@code MeetingLink} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withMeetingLink(String meetingLink) {
        descriptor.setMeetingLink(Optional.of(new MeetingLink(meetingLink)));
        return this;
    }

    /**
     * Clears the {@code MeetingLink} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withoutMeetingLink() {
        descriptor.setMeetingLink(Optional.empty());
        return this;
    }

    public EditPersonDescriptor build() {
        return descriptor;
    }
}
