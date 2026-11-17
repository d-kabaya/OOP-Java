/**
 * Lesson 6: a coin is measured by its value.
 */
public class Coin implements Measurable {

    private String name;
    private double value;

    public Coin(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public double getMeasure() {
        return value;
    }

    public String toString() {
        return name + " coin";
    }
}
