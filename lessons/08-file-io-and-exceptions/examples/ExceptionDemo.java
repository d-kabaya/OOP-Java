/**
 * Lesson 8: throwing, catching and finally.
 *
 * Compile: javac *.java
 * Run:     java ExceptionDemo
 */
public class ExceptionDemo {

    public static void main(String[] args) {
        Wallet w = new Wallet(20);

        // ---------- Each problem gets its own specific exception ----------
        double[] attempts = {5, -3, 50, 10};
        for (double amount : attempts) {
            try {
                w.spend(amount);
                System.out.println("Spent " + amount + ", left: " + w.getMoney());
            } catch (InsufficientFundsException e) {
                System.out.println("Not enough money: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Bad amount: " + e.getMessage());
            } finally {
                System.out.println("  (finally always runs)");
            }
        }

        // ---------- Unchecked exceptions from Java itself ----------
        try {
            int[] a = new int[3];
            a[3] = 1;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        try {
            Integer.parseInt("12a");
        } catch (NumberFormatException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        // ---------- An uncaught exception stops the program ----------
        System.out.println("Now spending 1000 with no try/catch...");
        w.spend(1000);
        System.out.println("This line is never printed");
    }
}
