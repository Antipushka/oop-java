import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.util.Objects;

/**
 * @author Dmitriy Antipin
 */
public abstract class AbstractAccount implements Account, Cloneable {

    private static final String DEFAULT_NUMBER = "";
    private static final double DEFAULT_BALANCE = 0;

    private String number;
    private double balance;
    private LocalDate creationDate;
    private LocalDate expirationDate;

    protected AbstractAccount() {
        this(DEFAULT_NUMBER, LocalDate.now());
    }

    protected AbstractAccount(String number, LocalDate expirationDate) {
        this(number, DEFAULT_BALANCE, LocalDate.now(), expirationDate);
    }

    protected AbstractAccount(String number, double balance, LocalDate creationDate, LocalDate expirationDate) {
        checkDates(creationDate, expirationDate);
        setNumber(number);
        this.balance = balance;
        setCreationDate(creationDate);
        this.expirationDate = expirationDate;
    }

    private void checkDates(LocalDate creationDate, LocalDate expirationDate) {
        if (expirationDate.isBefore(creationDate)) {
            throw new IllegalArgumentException("A expiration date can't be earlier than a creation date");
        }
    }

    private void checkCreationDate(LocalDate creationDate) {
        if (creationDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A creation date can't be later than now date");
        }
    }

    @Override
    public String getNumber() {
        return this.number;
    }

    @Override
    public void setNumber(String number) {
        this.number = Objects.requireNonNull(number, "A number can't has null reference");
    }

    @Override
    public double getBalance() {
        return this.balance;
    }

    @Override
    public void setBalance(double balance) {
        this.balance = balance;;
    }

    @Override
    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    @Override
    public void setCreationDate(LocalDate creationDate) {
        checkCreationDate(creationDate);
        this.creationDate = Objects.requireNonNull(creationDate,
                "A creation date can't has null reference");
    }

    @Override
    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    @Override
    public void setExpirationDate(LocalDate expirationDate) {
        Objects.requireNonNull(expirationDate,
                "A expiration date can't has null reference");
        checkDates(this.creationDate, expirationDate);
        this.expirationDate = expirationDate;
    }

    @Override
    public int monthsQuantityBeforeExpiration() {
        int quantity = expirationDate.getMonthValue() - LocalDate.now().getMonthValue();
        if (LocalDate.now().getDayOfMonth() < 25) {
            quantity++;
        }
        return quantity;
    }

    @Override
    public int compareTo(Account o) {
        return Double.compare(this.balance, o.getBalance());
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
                Objects.equals(this.number, that.number) &&
                this.creationDate.equals(that.creationDate) &&
                this.expirationDate.equals(that.expirationDate);
    }

    @Override
    public int hashCode() {
        return (int) (this.number.hashCode() *
                this.balance *
                this.creationDate.hashCode() *
                this.expirationDate.hashCode());
    }
}
