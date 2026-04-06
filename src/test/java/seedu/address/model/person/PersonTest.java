package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARK_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_PARENT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TIME_BOB;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // same name, phone number, address, different tags -> returns true
        assertTrue(ALICE.isSamePerson(new PersonBuilder(ALICE).build()));

        // same name, phone number, address, different tags -> returns true
        Person editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_PARENT).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // same name, phone number, address, different time -> returns true
        editedAlice = new PersonBuilder(ALICE).withTime(VALID_TIME_BOB).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same name, all other attributes different -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_PARENT).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // same phone number, all other attributes different -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_PARENT).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // same address, all other attributes different -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_PARENT).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // different phone number, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // different address, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // address removed, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withoutAddress().build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BOB.isSamePerson(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSamePerson(editedBob));

        // address differs in case, all other attributes same -> returns false
        editedBob = new PersonBuilder(BOB).withAddress(VALID_ADDRESS_BOB.toLowerCase()).build();
        assertFalse(BOB.isSamePerson(editedBob));

        // address has trailing spaces, all other attributes same -> returns false
        String addressWithTrailingSpaces = VALID_ADDRESS_BOB + " ";
        editedBob = new PersonBuilder(BOB).withAddress(addressWithTrailingSpaces).build();
        assertFalse(BOB.isSamePerson(editedBob));
    }

    @Test
    public void isSameId_samePerson_success() {
        assertTrue(ALICE.isSameId(ALICE));
    }

    @Test
    public void isSameId_differentId_success() {
        Person editedAlice = new PersonBuilder(ALICE)
            .withId(2)
            .build();

        assertFalse(editedAlice.isSameId(ALICE));
    }

    @Test
    public void isSameId_otherPersonNull_success() {
        assertFalse(ALICE.isSameId(null));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // missing address -> returns false
        editedAlice = new PersonBuilder(ALICE).withoutAddress().build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_PARENT).build();
        assertFalse(ALICE.equals(editedAlice));

        // different time -> returns false
        editedAlice = new PersonBuilder(ALICE).withTime(VALID_TIME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different remark -> returns false
        editedAlice = new PersonBuilder(ALICE).withRemark(VALID_REMARK_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // has meeting link, original does not -> returns false
        editedAlice = new PersonBuilder(ALICE).withMeetingLink("https://zoom.us/whatisthis").build();
        assertFalse(ALICE.equals(editedAlice));

        // different meeting link values -> returns false
        Person aliceWithLink = new PersonBuilder(ALICE).withMeetingLink("https://zoom.us/woah").build();
        Person aliceWithOtherLink = new PersonBuilder(ALICE).withMeetingLink("https://zoom.us/aiya").build();
        assertFalse(aliceWithLink.equals(aliceWithOtherLink));
    }

    @Test
    public void hashCode_equalPersons_sameHashCode() {
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertEquals(ALICE.hashCode(), aliceCopy.hashCode());
    }

    @Test
    public void hashCode_personWithMeetingLink_differentFromPersonWithout() {
        Person aliceWithLink = new PersonBuilder(ALICE).withMeetingLink("https://zoom.us/randomLink").build();
        assertFalse(ALICE.hashCode() == aliceWithLink.hashCode());
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName()
                + "{id=" + ALICE.getId()
                + ", name=" + ALICE.getName()
                + ", phone=" + ALICE.getPhone()
                + ", address=" + ALICE.getAddress()
                + ", time=" + ALICE.getTime()
                + ", tags=" + ALICE.getTags()
                + ", remark=" + ALICE.getRemark()
                + ", meetingLink=" + ALICE.getMeetingLink() + "}";
        assertEquals(expected, ALICE.toString());
    }
}
