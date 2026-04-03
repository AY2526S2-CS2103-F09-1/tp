package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class PersonCardTest {
    private static final String CSS_CLASS_MISSING_FIELD = "missing-field";

    private static boolean isFxToolkitInitialized;

    @BeforeAll
    public static void setUpFxToolkit() throws InterruptedException {
        if (isFxToolkitInitialized) {
            return;
        }

        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
        isFxToolkitInitialized = true;
    }

    @Test
    public void constructor_personWithTime_rendersTimeAndExistingFields() throws Exception {
        Person person = new PersonBuilder()
                .withId(2)
                .withName("Bart Bass")
                .withPhone("91234567")
                .withAddress("1 Upper East Side")
                .withTime("Monday 18:00")
                .withTags("Student", "Tutor")
                .withRemark("Bring notes")
                .build();

        PersonCard personCard = createPersonCard(person, 3);

        assertEquals("3. ", getLabelText(personCard, "index"));
        assertEquals("Bart Bass (ID: 2)", getLabelText(personCard, "nameWithId"));
        assertEquals("\uD83D\uDCDE 91234567", getLabelText(personCard, "phone"));
        assertEquals("\uD83C\uDFE0 1 Upper East Side", getLabelText(personCard, "address"));
        assertEquals("\u23F0 Monday 18:00", getLabelText(personCard, "time"));
        assertEquals("\uD83D\uDCDD Bring notes", getLabelText(personCard, "remark"));
        assertFalse(hasCssClass(personCard, "time", CSS_CLASS_MISSING_FIELD));
        assertEquals(2, getTagsPane(personCard).getChildren().size());
    }

    @Test
    public void constructor_personWithTimeDuration_rendersNormalizedDuration() throws Exception {
        Person person = new PersonBuilder()
                .withId(4)
                .withName("Blair Waldorf")
                .withPhone("98765432")
                .withAddress("3 Upper East Side")
                .withTime("sunday 1800 - 1930")
                .withRemark("Confirm venue")
                .build();

        PersonCard personCard = createPersonCard(person, 2);

        assertEquals("\u23F0 Sunday 18:00 - 19:30", getLabelText(personCard, "time"));
        assertFalse(hasCssClass(personCard, "time", CSS_CLASS_MISSING_FIELD));
    }

    @Test
    public void constructor_personWithoutOptionalFields_rendersMissingIndicators() throws Exception {
        Person person = new PersonBuilder()
                .withId(5)
                .withName("Nate Archibald")
                .withoutPhone()
                .withoutAddress()
                .withoutTime()
                .withoutRemark()
                .build();

        PersonCard personCard = createPersonCard(person, 1);

        assertEquals("\u23F0 No time provided", getLabelText(personCard, "time"));
        assertTrue(hasCssClass(personCard, "time", CSS_CLASS_MISSING_FIELD));
        assertEquals("\uD83D\uDCDE No phone number provided", getLabelText(personCard, "phone"));
        assertEquals("\uD83C\uDFE0 No address provided", getLabelText(personCard, "address"));
        assertEquals("\uD83D\uDCDD No remark provided", getLabelText(personCard, "remark"));
    }

    private PersonCard createPersonCard(Person person, int displayedIndex)
            throws InterruptedException, ExecutionException {
        FutureTask<PersonCard> task = new FutureTask<>(() -> new PersonCard(person, displayedIndex));
        Platform.runLater(task);
        return task.get();
    }

    private String getLabelText(PersonCard personCard, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        Label label = (Label) getFieldValue(personCard, fieldName);
        return label.getText();
    }

    private boolean hasCssClass(PersonCard personCard, String fieldName, String cssClass)
            throws NoSuchFieldException, IllegalAccessException {
        Label label = (Label) getFieldValue(personCard, fieldName);
        return label.getStyleClass().contains(cssClass);
    }

    private FlowPane getTagsPane(PersonCard personCard) throws NoSuchFieldException, IllegalAccessException {
        return (FlowPane) getFieldValue(personCard, "tags");
    }

    private Object getFieldValue(PersonCard personCard, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = PersonCard.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(personCard);
    }
}
