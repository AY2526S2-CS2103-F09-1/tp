package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_LINK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_DELETE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.Set;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Address;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * A utility class for Person.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code person}.
     */
    public static String getAddCommand(Person person) {
        return AddCommand.COMMAND_WORD + " " + getPersonDetails(person);
    }

    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getPersonDetails(Person person) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + person.getName().fullName + " ");

        person.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        person.getAddress().ifPresent(address -> sb.append(PREFIX_ADDRESS).append(address.value).append(" "));
        person.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        person.getRemark().ifPresent(remark -> sb.append(PREFIX_REMARK).append(remark.value).append(" "));
        person.getMeetingLink().ifPresent(meetingLink ->
                sb.append(PREFIX_MEETING_LINK).append(meetingLink.value).append(" "));
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        if (descriptor.isAddressChanged()) {
            Runnable appendClearedAddress = () -> appendClearedEditDetail(sb, PREFIX_ADDRESS);
            descriptor.getAddress().ifPresentOrElse(
                    address -> appendAddressEditDetail(sb, address), appendClearedAddress);
        }
        if (descriptor.isTimeChanged()) {
            Runnable appendClearedTime = () -> appendClearedEditDetail(sb, PREFIX_TIME);
            descriptor.getTime().ifPresentOrElse(
                    time -> appendTimeEditDetail(sb, time.value), appendClearedTime);
        }
        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG).append(" ");
            } else {
                tags.forEach(s -> sb.append(PREFIX_TAG).append(s.tagName).append(" "));
            }
        }
        descriptor.getTagsToDelete().ifPresent(tags ->
                tags.forEach(s -> sb.append(PREFIX_TAG_DELETE).append(s.tagName).append(" ")));
        descriptor.getRemark().ifPresent(remark -> sb.append(PREFIX_REMARK).append(remark.value).append(" "));
        if (descriptor.isMeetingLinkChanged()) {
            Runnable appendClearedMeetingLink = () -> appendClearedEditDetail(sb, PREFIX_MEETING_LINK);
            descriptor.getMeetingLink().ifPresentOrElse(
                    meetingLink -> sb.append(PREFIX_MEETING_LINK).append(meetingLink.value).append(" "),
                    appendClearedMeetingLink);
        }
        return sb.toString();
    }

    private static void appendAddressEditDetail(StringBuilder sb, Address address) {
        sb.append(PREFIX_ADDRESS).append(address.value).append(" ");
    }

    private static void appendClearedEditDetail(StringBuilder sb, Prefix prefix) {
        sb.append(prefix).append(" ");
    }

    private static void appendTimeEditDetail(StringBuilder sb, String timeValue) {
        sb.append(PREFIX_TIME).append(timeValue).append(" ");
    }
}
