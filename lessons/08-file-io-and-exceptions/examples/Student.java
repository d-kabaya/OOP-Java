/**
 * Lesson 8: a student with a mark. The constructor validates and throws.
 */
public class Student {

    public static final int MIN_MARK = 0;
    public static final int MAX_MARK = 100;
    public static final int PASS_MARK = 40;

    private String name;
    private int mark;

    public Student(String name, int mark) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (mark < MIN_MARK || mark > MAX_MARK) {
            throw new IllegalArgumentException("Mark must be 0 to 100: " + mark);
        }
        this.name = name;
        this.mark = mark;
    }

    public boolean hasPassed() {
        return mark >= PASS_MARK;
    }

    // One line of CSV, in the same format as the input file
    public String toCsv() {
        return name + "," + mark;
    }

    public String toString() {
        return name + " (" + mark + ")";
    }
}
