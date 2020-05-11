/**
 * @author Dmitriy Antipin
 */
public class InvalidAccountNumberException extends RuntimeException {

    public InvalidAccountNumberException() {
        super();
    }

    public InvalidAccountNumberException(String message) {
        super(message);
    }
}
