/**
 * Lesson 10: a shared counter. One method is safe for threads, the other isn't.
 */
public class Counter {

    private int count = 0;

    // NOT thread-safe: count++ is read, add 1, write. Two threads can interleave these steps.
    public void increment() {
        count++;
    }

    // Thread-safe: only one thread at a time can be inside this method on the same Counter
    public synchronized void incrementSafely() {
        count++;
    }

    public int getCount() {
        return count;
    }
}
