
/**
 * @author Dmitriy Antipin
 */
public class DebitAccount extends AbstractAccount implements Cloneable {

    public DebitAccount() {
        super();
    }

    public DebitAccount(String number, double balance) {
        super(number, balance);
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
