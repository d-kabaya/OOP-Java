/**
 * Lesson 7: a savings account earns interest.
 */
public class SavingsAccount extends BankAccount {

    public static final double INTEREST_RATE = 0.04;

    public SavingsAccount(long accountNumber, String accountName) {
        super(accountNumber, accountName, 0);   // savings accounts always start at 0
    }

    public void addInterest() {
        // balance is private in BankAccount, so we use the inherited methods instead
        double interest = getBalance() * INTEREST_RATE;
        deposit(interest);
    }
}
