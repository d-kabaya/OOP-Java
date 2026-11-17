/**
 * Lesson 10: creating, starting and joining threads.
 * The order of the output can change from run to run: that's expected.
 *
 * Compile: javac *.java
 * Run:     java ThreadDemo
 */
public class ThreadDemo {

    public static void main(String[] args) throws InterruptedException {   // join() throws a checked exception
        // ---------- Way 1: extend Thread ----------
        Thread t1 = new MyThread("MyThread 1");
        Thread t2 = new MyThread("MyThread 2");
        System.out.println("t1 state before start: " + t1.getState());     // NEW

        t1.start();          // both now run at the same time, so their output interleaves
        t2.start();

        t1.join();           // main waits until t1 finishes
        t2.join();           // ...and t2
        System.out.println("t1 state after join:   " + t1.getState());     // TERMINATED

        // ---------- Way 2: a Runnable, written as a lambda ----------
        Runnable task = () -> System.out.println("Hello from " + Thread.currentThread().getName());
        Thread t3 = new Thread(task, "Lambda thread");
        t3.start();
        t3.join();

        // ---------- start() vs run() ----------
        Thread t4 = new Thread(task, "Never started");
        t4.run();            // NOT a new thread: runs in main, like a normal method call
    }
}
