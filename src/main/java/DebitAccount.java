import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * @author Dmitriy Antipin
 */
public class DebitAccount extends AbstractAccount implements Cloneable {

    private static final String NUMBER_REGEX_FORMAT = "40\\d{3}810\\d{4}[1-9]\\d{6}[1-9]";

    public DebitAccount(String number, LocalDate expirationDate) {
        super(number, expirationDate);
    }

    public DebitAccount(String number, double balance, LocalDate creationDate, LocalDate expirationDate) {
        super(number, balance, creationDate, expirationDate);
        checkBalance(balance);
    }

    private void checkBalance(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("The balance can't has negative value");
        }
    }

    @Override
    public void setNumber(String number) {
        checkNumber(number);
        super.setNumber(number);
    }

    private void checkNumber(String number) {
        if (!Pattern.compile(NUMBER_REGEX_FORMAT).matcher(number).matches()) {
            throw new InvalidAccountNumberException("Invalid debit account number");
        }
    }

    @Override
    public String toString() {
        return String.format("Debit account - %s", super.toString());
    }

    @Override
    public int hashCode() {
        return 53 * super.hashCode();
    }
}
