/**
 * Lesson 8: a custom unchecked exception (it extends RuntimeException).
 */
public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String message) {
        super(message);       // passes the message to the parent class, for getMessage()
    }
}
