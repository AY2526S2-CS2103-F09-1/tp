package seedu.address.ui;

import java.util.Comparator;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    private static final String MESSAGE_MISSING_PHONE_NUMBER = "No phone number provided";
    private static final String MESSAGE_MISSING_ADDRESS = "No address provided";
    private static final String MESSAGE_MISSING_REMARK = "No remark provided";

    private static final String CSS_CLASS_MISSING_FIELD = "missing-field";

    private static final String PHONE_ICON = "\uD83D\uDCDE";
    private static final String ADDRESS_ICON = "\uD83C\uDFE0";
    private static final String REMARK_ICON = "\uD83D\uDCDD";

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
    private Label index;
    @FXML
    private Label nameWithId;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private FlowPane tags;
    @FXML
    private Label remark;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;

        // to produce a numbered list
        renderIndex(displayedIndex, index);

        renderNameWithId(person, nameWithId);
        renderPhone(person, phone);
        renderAddress(person, address);
        renderTags(person);
        renderRemark(person, remark);
    }

    private void renderRemark(Person person, Label remarkLabel) {
        Optional<Remark> remark = person.getRemark();

        // if the Remark inside is not empty, set text
        // else just show an empty string
        remark.ifPresentOrElse(r -> remarkLabel.setText(REMARK_ICON + " " + r.value), () -> {
            remarkLabel.setText(REMARK_ICON + " " + MESSAGE_MISSING_REMARK);
            addCssClass(remarkLabel, CSS_CLASS_MISSING_FIELD);
        });
    }

    private void renderTags(Person person) {
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    private void renderIndex(int displayedIndex, Label indexLabel) {
        indexLabel.setText(displayedIndex + ". ");
    }

    private void renderNameWithId(Person person, Label nameWithIdLabel) {
        String fullName = person.getName().fullName;
        int idValue = person.getId().getValue();
        nameWithIdLabel.setText(fullName + " (ID: " + idValue + ")");
    }

    private void renderPhone(Person person, Label phoneLabel) {
        Optional<Phone> phone = person.getPhone();

        // if the Phone inside is not empty, set text
        // else inform user the Phone is missing
        phone.ifPresentOrElse(p -> phoneLabel.setText(PHONE_ICON + " " + p.value), () -> {
            phoneLabel.setText(PHONE_ICON + " " + MESSAGE_MISSING_PHONE_NUMBER);
            addCssClass(phoneLabel, CSS_CLASS_MISSING_FIELD);
        });
    }

    private void renderAddress(Person person, Label addressLabel) {
        person.getAddress().ifPresentOrElse(address -> renderPresentAddress(address.value, addressLabel), () ->
                renderMissingAddress(addressLabel));
    }

    private void renderPresentAddress(String addressValue, Label addressLabel) {
        addressLabel.setText(ADDRESS_ICON + " " + addressValue);
    }

    private void renderMissingAddress(Label addressLabel) {
        addressLabel.setText(ADDRESS_ICON + " " + MESSAGE_MISSING_ADDRESS);
        addCssClass(addressLabel, CSS_CLASS_MISSING_FIELD);
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
