/**
 * Lesson 4: a module with a mark from 0 to 100.
 */
public class Module {

    public static final int MIN_MARK = 0;
    public static final int MAX_MARK = 100;

    private String name;
    private int mark;

    public Module(String name, int mark) {
        if (mark < MIN_MARK || mark > MAX_MARK) {
            throw new IllegalArgumentException("Mark must be between 0 and 100: " + mark);
        }
        this.name = name;
        this.mark = mark;
    }

    public String getName() {
        return name;
    }

    public int getMark() {
        return mark;
    }
}
