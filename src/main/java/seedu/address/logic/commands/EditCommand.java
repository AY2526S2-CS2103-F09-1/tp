package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_LINK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Id;
import seedu.address.model.person.MeetingLink;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person with "
            + "the specified ID. "
            + "Existing values will be overwritten by the input values, except tags which are appended.\n"
            + "Parameters: ID (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "[" + PREFIX_REMARK + "REMARK] "
            + "[" + PREFIX_MEETING_LINK + "MEETING_LINK]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_TAG + "Student " + PREFIX_REMARK + "needs additional practices\n"
            + "To clear all existing tags, use " + COMMAND_WORD + " 1 " + PREFIX_TAG + "\n"
            + "To clear meeting link, use " + COMMAND_WORD + " 1 " + PREFIX_MEETING_LINK;

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the contact list.";
    public static final String MESSAGE_INVALID_TAG_RESET =
            "The tag reset prefix t/ cannot be combined with tag values.";

    private final Id id;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param id of the person in the address book to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Id id, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(id);
        requireNonNull(editPersonDescriptor);

        this.id = id;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new CommandException(MESSAGE_NOT_EDITED);
        }

        Person personToEdit = model.findPersonById(id)
                .orElseThrow(() -> new CommandException(String.format(Messages.MESSAGE_INVALID_PERSON_ID,
                        id.getValue())));

        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}. Provided tags are appended to the
     * existing tags, unless the edited tag set is empty, which clears all tags.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(personToEdit);
        requireNonNull(editPersonDescriptor);

        Id personId = personToEdit.getId();
        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Optional<Phone> updatedPhone = editPersonDescriptor.isPhoneChanged()
                ? editPersonDescriptor.getPhone()
                : personToEdit.getPhone();
        Optional<Address> updatedAddress = editPersonDescriptor.isAddressChanged()
                ? editPersonDescriptor.getAddress()
                : personToEdit.getAddress();
        Set<Tag> updatedTags = createUpdatedTags(personToEdit.getTags(), editPersonDescriptor);
        Optional<Remark> updatedRemark = editPersonDescriptor.isRemarkChanged()
                ? editPersonDescriptor.getRemark()
                : personToEdit.getRemark();
        Optional<MeetingLink> updatedMeetingLink = editPersonDescriptor.isMeetingLinkChanged()
                ? editPersonDescriptor.getMeetingLink()
                : personToEdit.getMeetingLink();

        return new Person(personId, updatedName, updatedPhone, updatedAddress, updatedTags, updatedRemark,
                updatedMeetingLink);
    }

    private static Set<Tag> createUpdatedTags(Set<Tag> existingTags, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(existingTags);
        requireNonNull(editPersonDescriptor);

        return editPersonDescriptor.getTags()
                .map(tagsToApply -> mergeTags(existingTags, tagsToApply))
                .orElse(existingTags);
    }

    private static Set<Tag> mergeTags(Set<Tag> existingTags, Set<Tag> tagsToApply) {
        requireNonNull(existingTags);
        requireNonNull(tagsToApply);

        if (tagsToApply.isEmpty()) {
            return Collections.emptySet();
        }

        Set<Tag> combinedTags = new HashSet<>(existingTags);
        combinedTags.addAll(tagsToApply);
        return combinedTags;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return id.equals(otherEditCommand.id)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("id", id)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person, except tags which will be appended.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Optional<Phone> phone;
        private boolean phoneChanged;
        private Optional<Address> address;
        private boolean addressChanged;
        private Set<Tag> tags;
        private Optional<Remark> remark;
        private boolean remarkChanged;
        private Optional<MeetingLink> meetingLink;
        private boolean meetingLinkChanged;

        /**
         * Creates an empty descriptor with no edited fields.
         */
        public EditPersonDescriptor() {
            this.phoneChanged = false;
            this.phone = Optional.empty();
            this.addressChanged = false;
            this.address = Optional.empty();
            this.remarkChanged = false;
            this.remark = Optional.empty();
            this.meetingLinkChanged = false;
            this.meetingLink = Optional.empty();
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            assert toCopy != null;

            setName(toCopy.name);
            setPhone(toCopy.phone, toCopy.phoneChanged);
            setAddress(toCopy.address, toCopy.addressChanged);
            setTags(toCopy.tags);
            setRemark(toCopy.remark, toCopy.remarkChanged);
            setMeetingLink(toCopy.meetingLink, toCopy.meetingLinkChanged);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, tags)
                    || phoneChanged
                    || addressChanged
                    || remarkChanged
                    || meetingLinkChanged;
        }

        /**
         * Sets the edited name.
         */
        public void setName(Name name) {
            this.name = name;
        }

        /**
         * Returns the edited name if it was provided.
         */
        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        /**
         * Sets the edited phone value.
         * For public use.
         */
        public void setPhone(Optional<Phone> phone) {
            Optional.ofNullable(phone)
                .ifPresentOrElse(p -> setPhone(p, true), () ->
                        setPhone(Optional.empty(), false));
        }

        /**
         * Sets edited phone value and explicit edit state.
         * For private use.
         */
        private void setPhone(Optional<Phone> phone, boolean phoneChanged) {
            requireNonNull(phone);
            requireNonNull(phoneChanged);

            this.phone = phone;
            this.phoneChanged = phoneChanged;
        }

        /**
         * Returns true if phone field was explicitly edited by the user.
         */
        public boolean isPhoneChanged() {
            return phoneChanged;
        }

        /**
         * Returns the edited phone if it was provided.
         */
        public Optional<Phone> getPhone() {
            // if phone null return Optional.empty
            // else return Optional<Phone>
            return Optional.ofNullable(phone)
                .flatMap(phone -> phone);
        }

        /**
         * Sets the edited address.
         */
        public void setAddress(Optional<Address> address) {
            Optional.ofNullable(address)
                    .ifPresentOrElse(a -> setAddress(a, true), () ->
                            setAddress(Optional.empty(), false));
        }

        /**
         * Sets edited address value and explicit edit state.
         * For private use.
         */
        private void setAddress(Optional<Address> address, boolean addressChanged) {
            requireNonNull(address);
            this.address = address;
            this.addressChanged = addressChanged;
        }

        /**
         * Returns true if address field was explicitly edited by the user.
         */
        public boolean isAddressChanged() {
            return addressChanged;
        }

        /**
         * Returns the edited address if it was provided.
         */
        public Optional<Address> getAddress() {
            return Optional.ofNullable(address)
                    .flatMap(address -> address);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        /**
         * Sets the edited remark value.
         * For public use.
         */
        public void setRemark(Optional<Remark> remark) {
            Optional.ofNullable(remark)
                    .ifPresentOrElse(r -> setRemark(r, true), () -> setRemark(Optional.empty(), false));
        }

        /**
         * Sets edited remark value and explicit edit state.
         * For private use.
         */
        private void setRemark(Optional<Remark> remark, boolean remarkChanged) {
            requireNonNull(remark);
            requireNonNull(remarkChanged);

            this.remark = remark;
            this.remarkChanged = remarkChanged;
        }

        /**
         * Returns true if remark field was explicitly edited by the user.
         */
        public boolean isRemarkChanged() {
            return remarkChanged;
        }

        /**
         * Returns the edited remark if it was provided.
         */
        public Optional<Remark> getRemark() {
            // if remark null return Optional.empty
            // else return Optional<Remark>
            return Optional.ofNullable(remark)
                    .flatMap(remark -> remark);
        }

        /**
         * Sets the edited meeting link value.
         * For public use.
         */
        public void setMeetingLink(Optional<MeetingLink> meetingLink) {
            Optional.ofNullable(meetingLink)
                    .ifPresentOrElse(l -> setMeetingLink(l, true), () -> setMeetingLink(Optional.empty(), false));
        }

        /**
         * Sets edited meeting link value and change the edit state.
         * For private use.
         */
        private void setMeetingLink(Optional<MeetingLink> meetingLink, boolean meetingLinkChanged) {
            requireNonNull(meetingLink);
            this.meetingLink = meetingLink;
            this.meetingLinkChanged = meetingLinkChanged;
        }

        /**
         * Returns true if meeting link field was edited by the user.
         */
        public boolean isMeetingLinkChanged() {
            return meetingLinkChanged;
        }

        /**
         * Returns the edited meeting link if it was provided.
         */
        public Optional<MeetingLink> getMeetingLink() {
            return Optional.ofNullable(meetingLink).flatMap(l -> l);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && phoneChanged == otherEditPersonDescriptor.phoneChanged
                    && Objects.equals(address, otherEditPersonDescriptor.address)
                    && addressChanged == otherEditPersonDescriptor.addressChanged
                    && Objects.equals(tags, otherEditPersonDescriptor.tags)
                    && Objects.equals(remark, otherEditPersonDescriptor.remark)
                    && remarkChanged == otherEditPersonDescriptor.remarkChanged
                    && Objects.equals(meetingLink, otherEditPersonDescriptor.meetingLink)
                    && meetingLinkChanged == otherEditPersonDescriptor.meetingLinkChanged;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("address", address)
                    .add("tags", tags)
                    .add("remark", remark)
                    .toString();
        }
    }
}
