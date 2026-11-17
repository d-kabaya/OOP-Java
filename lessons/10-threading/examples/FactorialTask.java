/**
 * Lesson 10: "the computation". Multiplies all the numbers from start to end (inclusive).
 */
public class FactorialTask implements Runnable {

    private int start;
    private int end;
    private long result;

    public FactorialTask(int start, int end) {
        this.start = start;
        this.end = end;
        this.result = 1;
    }

    @Override
    public void run() {
        for (int i = start; i <= end; i++) {
            result *= i;
        }
        System.out.println(Thread.currentThread().getName() + " multiplied " + start + " to " + end + " = " + result);
    }

    // Only read this after the thread has finished (after join)
    public long getResult() {
        return result;
    }
}
