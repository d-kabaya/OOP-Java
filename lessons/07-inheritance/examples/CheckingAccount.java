/**
 * Lesson 7: a checking account charges a fee on every transaction.
 */
public class CheckingAccount extends BankAccount {

    public static final double CHECKING_FEE = 1;

    public CheckingAccount(long accountNumber, String accountName, double initial) {
        super(accountNumber, accountName, initial);   // must be the first line
    }

    public void deductFee() {
        // super.withdraw: BankAccount's version, which charges NO fee.
        // Plain withdraw(...) here would call our override below, which calls deductFee again, forever.
        super.withdraw(CHECKING_FEE);
    }

    @Override
    public void deposit(double amount) {
        super.deposit(amount);
        deductFee();
    }

    @Override
    public void withdraw(double amount) {
        super.withdraw(amount);
        deductFee();
    }
}
