package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    private static final String MESSAGE_MISSING_PHONE_NUMBER = "No phone number provided";
    private static final String MESSAGE_MISSING_ADDRESS = "No address provided";

    private static final String CSS_CLASS_MISSING_FIELD = "missing-field";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;

        renderId(displayedIndex, id);
        renderName(person, name);
        renderPhone(person, phone);
        renderAddress(person, address);

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    private void renderId(int displayedIndex, Label idLabel) {
        idLabel.setText(displayedIndex + ". ");
    }

    private void renderName(Person person, Label nameLabel) {
        nameLabel.setText(person.getName().fullName);
    }

    private void renderPhone(Person person, Label phoneLabel) {
        String phoneNumber = person.getPhone().value;

        if (phoneNumber.isEmpty()) {
            phoneLabel.setText(MESSAGE_MISSING_PHONE_NUMBER);
            addCssClass(phoneLabel, CSS_CLASS_MISSING_FIELD);
        } else {
            phoneLabel.setText(phoneNumber);
        }
    }

    private void renderAddress(Person person, Label addressLabel) {
        String address = person.getAddress().value;

        if (address.isEmpty()) {
            addressLabel.setText(MESSAGE_MISSING_ADDRESS);
            addCssClass(addressLabel, CSS_CLASS_MISSING_FIELD);
        } else {
            addressLabel.setText(address);
        }
    }

    /**
     * Adds CSS class for a particular {@code Label} object.
     *
     * @param label The label to add the CSS class to.
     * @param cssClass The CSS class to add.
     */
    private void addCssClass(Label label, String cssClass) {
        label.getStyleClass().add(cssClass);
    }
}
