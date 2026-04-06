package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Id;
import seedu.address.model.person.MeetingLink;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";
    private static final String EMPTY_STRING = "";

    private final int id;
    private final String name;
    private final String phone;
    private final String address;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final String remark;
    private final String meetingLink;

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("id") int id,
            @JsonProperty("name") String name,
            @JsonProperty("phone") String phone,
            @JsonProperty("address") String address,
            @JsonProperty("tags") List<JsonAdaptedTag> tags,
            @JsonProperty("remark") String remark,
            @JsonProperty("meetingLink") String meetingLink) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        this.remark = remark;
        this.meetingLink = meetingLink;
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        id = source.getId().getValue();
        name = source.getName().fullName;
        phone = source.getPhone().map(x -> x.value)
            .orElse(EMPTY_STRING);
        address = source.getAddress().map(x -> x.value)
                .orElse(EMPTY_STRING);
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        remark = source.getRemark().map(x -> x.value)
            .orElse(EMPTY_STRING);
        meetingLink = source.getMeetingLink().map(x -> x.value)
            .orElse(EMPTY_STRING);
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final Id modelId = getModelId();

        final Name modelName = getModelName();

        final Optional<Phone> modelPhone = getModelPhone();

        final Optional<Address> modelAddress = getModelAddress();

        final Set<Tag> modelTags = getModelTags();

        final Optional<Remark> modelRemark = getModelRemark();

        final Optional<MeetingLink> modelMeetingLink = getModelMeetingLink();

        return new Person(modelId, modelName, modelPhone, modelAddress, modelTags, modelRemark, modelMeetingLink);
    }

    private void validateMeetingLink() throws IllegalValueException {
        if (this.meetingLink == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, MeetingLink.class.getSimpleName()));
        }
        if (!MeetingLink.isValidMeetingLinkOrEmptyString(this.meetingLink)) {
            throw new IllegalValueException(MeetingLink.MESSAGE_CONSTRAINTS);
        }
    }

    private Optional<MeetingLink> getModelMeetingLink() throws IllegalValueException {
        validateMeetingLink();
        return this.meetingLink.isEmpty()
                ? Optional.empty()
                : Optional.of(new MeetingLink(this.meetingLink));
    }

    private Optional<Remark> getModelRemark() throws IllegalValueException {
        validateRemark();
        final Optional<Remark> modelRemark = this.remark.isEmpty()
            ? Optional.empty()
            : Optional.of(new Remark(this.remark));
        return modelRemark;
    }

    private void validateRemark() throws IllegalValueException {
        if (this.remark == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Remark.class.getSimpleName()));
        }
        if (!Remark.isValidRemarkOrEmptyString(this.remark)) {
            throw new IllegalValueException(Remark.MESSAGE_CONSTRAINTS);
        }
    }

    private Optional<Address> getModelAddress() throws IllegalValueException {
        validateAddress();
        final Optional<Address> modelAddress = this.address.isEmpty()
                ? Optional.empty()
                : Optional.of(new Address(this.address));
        return modelAddress;
    }

    private void validateAddress() throws IllegalValueException {
        if (this.address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddressOrEmptyString(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
    }

    private Optional<Phone> getModelPhone() throws IllegalValueException {
        validatePhone();
        // if empty phone string in JSON, make it optional empty
        final Optional<Phone> modelPhone = this.phone.isEmpty()
            ? Optional.empty()
            : Optional.of(new Phone(this.phone));
        return modelPhone;
    }

    private void validatePhone() throws IllegalValueException {
        if (this.phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhoneOrEmptyString(this.phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
    }

    private Name getModelName() throws IllegalValueException {
        validateName();
        final Name modelName = new Name(this.name);
        return modelName;
    }

    private void validateName() throws IllegalValueException {
        if (this.name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(this.name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
    }

    private Id getModelId() throws IllegalValueException {
        validateId();
        final Id modelId = Id.of(this.id);
        return modelId;
    }

    private void validateId() throws IllegalValueException {
        if (!Id.isValidId(this.id)) {
            throw new IllegalValueException(Id.MESSAGE_CONSTRAINTS);
        }
    }

    private Set<Tag> getModelTags() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : this.tags) {
            personTags.add(tag.toModelType());
        }
        return new HashSet<>(personTags);
    }

}
