/**
 * Lesson 10: Way 1, extending Thread and overriding run().
 */
public class MyThread extends Thread {

    public MyThread(String name) {
        super(name);                        // Thread's constructor sets the thread's name
    }

    @Override
    public void run() {                     // the work this thread does
        for (int i = 0; i < 3; i++) {
            System.out.println(getName() + ": " + i);
            try {
                sleep(50 + (int) Math.round(Math.random() * 50));   // pause 50 to 100 ms
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
