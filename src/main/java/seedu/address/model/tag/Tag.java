package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag {

    public static final String MESSAGE_CONSTRAINTS = "Tag names should be alphanumeric";
    public static final String VALIDATION_REGEX = "\\p{Alnum}+";
    private static final Map<String, String> SUPPORTED_TAG_NAMES = createSupportedTagNames();
    public static final String MESSAGE_TAG_CONSTRAINTS =
            "Tag must be one of the following values: "
                    + String.join(", ", SUPPORTED_TAG_NAMES.values()) + ".";

    public final String tagName;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) {
        requireNonNull(tagName);
        checkArgument(isValidTagName(tagName), MESSAGE_CONSTRAINTS);
        this.tagName = tagName;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        requireNonNull(test);
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns the normalized tag name for a valid tag.
     */
    public static String getNormalizedTagName(String test) {
        requireNonNull(test);
        return SUPPORTED_TAG_NAMES.get(test.toLowerCase());
    }

    private static Map<String, String> createSupportedTagNames() {
        Map<String, String> supportedTagNames = new LinkedHashMap<>();
        supportedTagNames.put("student", "Student");
        supportedTagNames.put("parent", "Parent");
        supportedTagNames.put("tutor", "Tutor");
        supportedTagNames.put("freshman", "Freshman");
        supportedTagNames.put("sophomore", "Sophomore");
        supportedTagNames.put("junior", "Junior");
        supportedTagNames.put("senior", "Senior");
        supportedTagNames.put("graduate", "Graduate");
        return Collections.unmodifiableMap(supportedTagNames);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Tag)) {
            return false;
        }

        Tag otherTag = (Tag) other;
        return tagName.equals(otherTag.tagName);
    }

    @Override
    public int hashCode() {
        return tagName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + tagName + ']';
    }

}
