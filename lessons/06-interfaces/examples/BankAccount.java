/**
 * Lesson 6: a bank account is measured by its balance.
 */
public class BankAccount implements Measurable {

    private double balance;

    public BankAccount(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    // Required by the Measurable interface: must be public, with a matching signature
    public double getMeasure() {
        return balance;
    }

    public String toString() {
        return "BankAccount with balance " + balance;
    }
}
