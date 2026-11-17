/**
 * Lesson 6: works out the average and maximum of ANY Measurable objects.
 * It never needs to know whether it is holding bank accounts, coins or anything else.
 */
public class DataSet {

    private double sum;
    private Measurable max;
    private int count;

    public void add(Measurable x) {
        sum += x.getMeasure();                         // polymorphism: the object's own getMeasure runs
        if (count == 0 || x.getMeasure() > max.getMeasure()) {
            max = x;
        }
        count++;
    }

    public double getAverage() {
        if (count == 0) {
            return 0;
        }
        return sum / count;
    }

    public Measurable getMaximum() {
        return max;
    }
}
