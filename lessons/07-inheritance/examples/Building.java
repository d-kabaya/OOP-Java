/**
 * Lesson 7: a concrete subclass of the abstract Drawing.
 */
public class Building extends Drawing {

    private int width;
    private int height;

    public Building(int x, int y, int width, int height) {
        super(x, y);
        this.width = width;
        this.height = height;
    }

    @Override
    public String draw() {
        return "Building at (" + x + ", " + y + "), " + width + " wide and " + height + " tall";
    }
}
