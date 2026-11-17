/**
 * Lesson 10: a race condition, and two ways to fix it.
 *
 * Compile: javac *.java
 * Run:     java RaceConditionDemo
 */
public class RaceConditionDemo {

    public static final int TIMES = 100_000;   // per thread, so the race is easy to see

    // Runs the same task on two threads at once, and waits for both to finish
    public static void runTwice(Runnable task) throws InterruptedException {
        Thread a = new Thread(task);
        Thread b = new Thread(task);
        a.start();
        b.start();
        a.join();
        b.join();
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Each test: 2 threads x " + TIMES + " increments, expected " + (2 * TIMES));

        // ---------- 1. No synchronisation: increments get lost ----------
        Counter unsafe = new Counter();
        runTwice(() -> {
            for (int i = 0; i < TIMES; i++) {
                unsafe.increment();
            }
        });
        System.out.println("Unsynchronized:       " + unsafe.getCount());

        // ---------- 2. Fix 1: a synchronized method ----------
        Counter safeMethod = new Counter();
        runTwice(() -> {
            for (int i = 0; i < TIMES; i++) {
                safeMethod.incrementSafely();
            }
        });
        System.out.println("Synchronized method:  " + safeMethod.getCount());

        // ---------- 3. Fix 2: a synchronized block (Counter's increment is unchanged) ----------
        Counter safeBlock = new Counter();
        runTwice(() -> {
            for (int i = 0; i < TIMES; i++) {
                synchronized (safeBlock) {      // take safeBlock's lock
                    safeBlock.increment();
                    safeBlock.notifyAll();      // tell waiting threads the lock is about to be free
                }                               // lock released here
            }
        });
        System.out.println("Synchronized block:   " + safeBlock.getCount());
    }
}
