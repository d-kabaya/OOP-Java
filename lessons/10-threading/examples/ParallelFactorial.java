/**
 * Lesson 10: "the management". Splits n! across two threads, then combines the results.
 *
 * Compile: javac *.java
 * Run:     java ParallelFactorial
 */
public class ParallelFactorial {

    public static void main(String[] args) throws InterruptedException {
        int n = 20;                                   // 20! is the largest factorial that fits in a long

        // Thread 1: 1 x ... x (n/2 - 1).  Thread 2: (n/2) x ... x n
        FactorialTask lower = new FactorialTask(1, n / 2 - 1);
        FactorialTask upper = new FactorialTask(n / 2, n);

        Thread t1 = new Thread(lower, "Thread 1");
        Thread t2 = new Thread(upper, "Thread 2");
        t1.start();
        t2.start();

        t1.join();                                    // wait for BOTH halves before combining
        t2.join();

        long parallel = lower.getResult() * upper.getResult();

        // Check against a normal single-threaded loop
        long expected = 1;
        for (int i = 1; i <= n; i++) {
            expected *= i;
        }
        System.out.println(n + "! with two threads = " + parallel);
        System.out.println(n + "! with one loop    = " + expected);
        System.out.println("Same answer? " + (parallel == expected));
    }
}
