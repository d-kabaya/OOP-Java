/**
 * Lesson 7: an abstract class. You can't create a Drawing, only its subclasses.
 * It shares data (x, y), a constructor and a working method (move),
 * and leaves draw() for each subclass to write.
 */
public abstract class Drawing {

    protected int x;     // protected: subclasses can use them directly
    protected int y;

    public Drawing(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public abstract String draw();
}
