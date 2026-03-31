package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARK_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_PARENT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_STUDENT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_TUTOR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {
    public static final Person ALICE = new PersonBuilder().withId(1)
            .withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111")
            .withPhone("94351253")
            .withTags(VALID_TAG_STUDENT).withoutRemark().build();
    public static final Person BENSON = new PersonBuilder().withId(2)
            .withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withPhone("98765432")
            .withTags(VALID_TAG_PARENT).withoutRemark().build();
    public static final Person CARL = new PersonBuilder().withId(3)
            .withName("Carl Kurz").withPhone("95352563")
            .withAddress("wall street").withTags(VALID_TAG_PARENT)
            .withoutRemark().build();
    public static final Person DANIEL = new PersonBuilder().withId(4)
            .withName("Daniel Meier").withPhone("87652533")
            .withAddress("10th street").withTags(VALID_TAG_STUDENT)
            .withoutRemark().build();
    public static final Person ELLE = new PersonBuilder().withId(5)
            .withName("Elle Meyer").withPhone("94822247")
            .withAddress("michegan ave").withTags(VALID_TAG_TUTOR)
            .withoutRemark().build();
    public static final Person FIONA = new PersonBuilder().withId(6)
            .withName("Fiona Kunz").withPhone("93724277")
            .withAddress("little tokyo").withTags(VALID_TAG_PARENT)
            .withoutRemark().build();
    public static final Person GEORGE = new PersonBuilder().withId(7)
            .withName("George Best").withPhone("94812442")
            .withAddress("4th street").withTags(VALID_TAG_TUTOR)
            .withoutRemark().build();
    public static final Person JIM = new PersonBuilder().withId(10)
            .withName("Jim Wilson").withPhone("92311245")
            .withAddress("10th street").withRemark("New student")
            .build();

    // Used for test cases in JsonAddressBookStorage
    // These persons are manually added to an existing address book
    // consisting of a list of the persons above
    public static final Person HOON = new PersonBuilder().withId(8)
            .withName("Hoon Meier").withPhone("84821424")
            .withAddress("little india").withTags(VALID_TAG_PARENT).build();
    public static final Person IDA = new PersonBuilder().withId(9)
            .withName("Ida Mueller").withPhone("84821131")
            .withAddress("chicago ave").withTags(VALID_TAG_PARENT).build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withId(1)
            .withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withAddress(VALID_ADDRESS_AMY).withTags(VALID_TAG_STUDENT).build();
    public static final Person BOB = new PersonBuilder().withId(1)
            .withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_PARENT)
            .withRemark(VALID_REMARK_BOB).build();

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
