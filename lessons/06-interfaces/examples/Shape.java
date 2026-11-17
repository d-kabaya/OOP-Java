/**
 * Lesson 6: an interface with abstract, static and default methods.
 */
public interface Shape {

    // Abstract methods: every Shape must implement these
    double getArea();
    double getOutline();

    // Static method: called on the interface name, e.g. Shape.getLargest(a, b)
    static Shape getLargest(Shape s1, Shape s2) {
        if (s1.getArea() > s2.getArea()) {
            return s1;
        } else {
            return s2;
        }
    }

    // Default method: every Shape gets this automatically
    default void printArea() {
        System.out.println(getClass().getSimpleName() + " area is " + getArea());
    }
}
