/**
 * Lesson 8: throwing exceptions as soon as a problem is detected.
 */
public class Wallet {

    private double money;

    public Wallet(double money) {
        if (money < 0) {
            throw new IllegalArgumentException("Starting money cannot be negative: " + money);
        }
        this.money = money;
    }

    public void spend(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive: " + amount);
        }
        if (amount > money) {
            throw new InsufficientFundsException("Only " + money + " left, cannot spend " + amount);
        }
        money -= amount;      // only reached if no exception was thrown
    }

    public double getMoney() {
        return money;
    }
}
