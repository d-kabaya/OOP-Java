/**
 * Lesson 6: a circle, implementing Shape. It also has a method of its own: grow.
 */
public class Circle implements Shape {

    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    public double getArea() {
        return Math.PI * radius * radius;
    }

    public double getOutline() {
        return 2 * Math.PI * radius;
    }

    // Not in the Shape interface
    public void grow(double r) {
        radius += r;
    }
}
