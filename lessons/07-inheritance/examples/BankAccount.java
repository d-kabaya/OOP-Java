/**
 * Lesson 7: the superclass for all bank accounts.
 */
public class BankAccount {

    private double balance;
    private long accountNumber;
    private String accountName;

    public BankAccount(long accountNumber, String accountName, double initial) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.balance = initial;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }

    // Overrides Object's toString: printing an account now shows something useful
    @Override
    public String toString() {
        return getClass().getSimpleName() + " of " + accountName + " (" + accountNumber + "), balance " + balance;
    }

    // Overrides Object's equals: two accounts are equal if they have the same account number.
    // The parameter MUST be Object, or this would be an overload, not an override.
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BankAccount) {
            BankAccount other = (BankAccount) obj;
            return other.accountNumber == this.accountNumber;
        } else {
            return false;
        }
    }
}
