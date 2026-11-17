import java.util.ArrayList;

/**
 * Lesson 5: the CashRegister from the lecture, showing the standard patterns for object data.
 */
public class CashRegister {

    // ---------- Constant ----------
    public static final double MAX_SINGLE_AMOUNT = 10_000;

    // ---------- Class (static) variable: ONE value shared by all registers ----------
    private static int registersCreated = 0;

    // ---------- Instance variables: every register has its own ----------
    private double money;                       // keeping a total
    private int sales;                          // counting events
    private ArrayList<String> transactions;     // collecting values

    public CashRegister(double initialAmount) {
        if (initialAmount < 0) {
            throw new IllegalArgumentException("Initial amount cannot be negative: " + initialAmount);
        }
        money = initialAmount;
        sales = 0;
        transactions = new ArrayList<String>();
        registersCreated++;
    }

    // Mutator: validates, then increases the total, counts the sale and records it
    public void add(double d) {
        checkAmount(d);
        money += d;
        sales++;
        transactions.add("Added " + d + ". Total now: " + money);
    }

    // Mutator: validates, then decreases the total
    public void remove(double d) {
        checkAmount(d);
        if (d > money) {
            throw new IllegalArgumentException("Not enough money in the till to remove " + d);
        }
        money -= d;
        transactions.add("Removed " + d + ". Total now: " + money);
    }

    // Private helper: the validation rule is written only once
    private void checkAmount(double d) {
        if (d <= 0 || d > MAX_SINGLE_AMOUNT) {
            throw new IllegalArgumentException("Amount must be between 0 and " + MAX_SINGLE_AMOUNT + ": " + d);
        }
    }

    // ---------- Accessors: they never change anything ----------
    public double getMoney() {
        return money;
    }

    public int getSales() {
        return sales;
    }

    // Returns a COPY, so callers can't change the real list
    public ArrayList<String> getTransactions() {
        return new ArrayList<String>(transactions);
    }

    // Static accessor for the static variable
    public static int getRegistersCreated() {
        return registersCreated;
    }
}
