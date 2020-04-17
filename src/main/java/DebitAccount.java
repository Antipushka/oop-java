/**
 * @author Dmitriy Antipin
 */
public class DebitAccount implements Account {

    private static final String DEFAULT_NUMBER = "";
    private static final double DEFAULT_BALANCE = 0;

    private String number;
    private double balance;

    public DebitAccount() {
        this(DEFAULT_NUMBER, DEFAULT_BALANCE);
    }

    public DebitAccount(String number, double balance) {
        this.number = number;
        this.balance = balance;
    }

    @Override
    public String getNumber() {
        return number;
    }

    @Override
    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
