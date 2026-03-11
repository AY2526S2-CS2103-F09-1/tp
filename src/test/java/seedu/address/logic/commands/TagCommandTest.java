package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.tag.Tag;

/**
 * Contains unit tests for {@code TagCommand}.
 */
public class TagCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_alwaysThrowsUntilNextIncrement() {
        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, new Tag("Student"));

        assertCommandFailure(tagCommand, model, TagCommand.MESSAGE_NOT_YET_AVAILABLE);
    }

    @Test
    public void equals() {
        TagCommand firstCommand = new TagCommand(INDEX_FIRST_PERSON, new Tag("Student"));
        TagCommand firstCommandCopy = new TagCommand(INDEX_FIRST_PERSON, new Tag("Student"));
        TagCommand secondCommand = new TagCommand(INDEX_SECOND_PERSON, new Tag("Tutor"));

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(firstCommandCopy));
        assertFalse(firstCommand.equals(1));
        assertFalse(firstCommand.equals(null));
        assertFalse(firstCommand.equals(secondCommand));
    }

    @Test
    public void toStringMethod() {
        Tag categoryTag = new Tag("Student");
        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, categoryTag);
        String expected = TagCommand.class.getCanonicalName()
                + "{targetIndex=" + INDEX_FIRST_PERSON + ", categoryTag=" + categoryTag + "}";
        assertEquals(expected, tagCommand.toString());
    }
}
