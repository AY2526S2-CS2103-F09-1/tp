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
    private static final Map<String, String> SUPPORTED_CATEGORY_TAGS = createSupportedCategoryTags();
    public static final String MESSAGE_TAG_CONSTRAINTS =
            "Category must be one of the following values: "
                    + String.join(", ", SUPPORTED_CATEGORY_TAGS.values()) + ".";

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
     * Returns the normalized category tag name for a supported category tag.
     */
    public static String getNormalizedCategoryTagName(String test) {
        requireNonNull(test);
        return SUPPORTED_CATEGORY_TAGS.get(test.toLowerCase());
    }

    private static Map<String, String> createSupportedCategoryTags() {
        Map<String, String> supportedCategoryTags = new LinkedHashMap<>();
        supportedCategoryTags.put("student", "Student");
        supportedCategoryTags.put("parent", "Parent");
        supportedCategoryTags.put("tutor", "Tutor");
        return Collections.unmodifiableMap(supportedCategoryTags);
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
