/**
 * @author Dmitriy Antipin
 */
public abstract class AbstractAccount implements Account {

    private static final String DEFAULT_NUMBER = "";
    private static double DEFAULT_BALANCE = 0;

    private String number;
    private double balance;

    protected AbstractAccount() {
        this(DEFAULT_NUMBER, DEFAULT_BALANCE);
    }

    protected AbstractAccount(String number, double balance) {
        this.number = number;
        this.balance = balance;
    }

    @Override
    public String getNumber() {
        return this.number;
    }

    @Override
    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public double getBalance() {
        return this.balance;
    }

    @Override
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
