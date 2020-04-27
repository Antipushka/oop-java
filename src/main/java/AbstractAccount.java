import java.util.Objects;

/**
 * @author Dmitriy Antipin
 */
public abstract class AbstractAccount implements Account, Cloneable {

    private static final String DEFAULT_NUMBER = "";
    private static final double DEFAULT_BALANCE = 0;

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

    @Override
    public String toString() {
        return String.format("number: %s balance: %f", number, balance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractAccount that = (AbstractAccount) o;
        return Double.compare(that.balance, this.balance) == 0 &&
                Objects.equals(this.number, that.number);
    }

    @Override
    public int hashCode() {
        return (int) (this.number.hashCode() * this.balance);
    }
}
