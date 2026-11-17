/**
 * Lesson 7: another concrete subclass of Drawing.
 */
public class Tree extends Drawing {

    private int height;

    public Tree(int x, int y, int height) {
        super(x, y);
        this.height = height;
    }

    @Override
    public String draw() {
        return "Tree at (" + x + ", " + y + "), " + height + " tall";
    }
}
