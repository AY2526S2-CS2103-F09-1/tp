package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Address;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
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
        sb.append(PREFIX_REMARK + person.getRemark().get().value + " ");
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        if (descriptor.isPhoneChanged()) {
            Runnable appendClearedPhone = () -> appendClearedEditDetail(sb, PREFIX_PHONE);
            descriptor.getPhone().ifPresentOrElse(
                    phone -> appendPhoneEditDetail(sb, phone), appendClearedPhone);
        }
        if (descriptor.isAddressChanged()) {
            Runnable appendClearedAddress = () -> appendClearedEditDetail(sb, PREFIX_ADDRESS);
            descriptor.getAddress().ifPresentOrElse(
                    address -> appendAddressEditDetail(sb, address), appendClearedAddress);
        }
        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG).append(" ");
            } else {
                tags.forEach(s -> sb.append(PREFIX_TAG).append(s.tagName).append(" "));
            }
        }
        descriptor.getRemark().ifPresent(remark -> sb.append(PREFIX_REMARK).append(remark.value));
        return sb.toString();
    }

    private static void appendPhoneEditDetail(StringBuilder sb, Phone phone) {
        sb.append(PREFIX_PHONE).append(phone.value).append(" ");
    }

    private static void appendAddressEditDetail(StringBuilder sb, Address address) {
        sb.append(PREFIX_ADDRESS).append(address.value).append(" ");
    }

    private static void appendClearedEditDetail(StringBuilder sb, Prefix prefix) {
        sb.append(prefix).append(" ");
    }
}
