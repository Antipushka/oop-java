/**
 * @author Dmitriy Antipin
 */
public class Account {

    private static final String DEFAULT_NUMBER = "";
    private static final double DEFAULT_BALANCE = 0;

    private String number;
    private double balance;

    public Account() {
        this(DEFAULT_NUMBER, DEFAULT_BALANCE);
    }

    public Account(String number, double balance) {
        this.number = number;
        this.balance = balance;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
