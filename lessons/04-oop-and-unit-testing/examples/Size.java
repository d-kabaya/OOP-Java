/**
 * Lesson 4: an enum with a field, a constructor and a method.
 */
public enum Size {
    EXTRA_SMALL("XS"), SMALL("S"), MEDIUM("M"), LARGE("L");

    private String abbreviation;

    // Enum constructors are always private: you can't create new Sizes with "new"
    Size(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}
