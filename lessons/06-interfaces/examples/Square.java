/**
 * Lesson 6: a square, implementing Shape.
 */
public class Square implements Shape {

    private double width;

    public Square(double width) {
        this.width = width;
    }

    public double getArea() {
        return width * width;
    }

    public double getOutline() {
        return 4 * width;
    }
}
