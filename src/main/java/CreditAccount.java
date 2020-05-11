import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * @author Dmitriy Antipin
 */
public class CreditAccount extends AbstractAccount implements Credit, Cloneable {

    private static final double DEFAULT_ANNUAL_PERCENTAGE_RATE = 30;
    private static final String NUMBER_REGEX_FORMAT = "4[4-5]\\d{3}810\\d{4}[1-9]\\d{6}[1-9]";

    private double annualPercentageRate;

    public CreditAccount() {
        super();
        this.annualPercentageRate = DEFAULT_ANNUAL_PERCENTAGE_RATE;
    }

    public CreditAccount(String number, double balance, double annualPercentageRate,
                         LocalDate creationDate, LocalDate expirationDate) {
        super(number, balance, creationDate, expirationDate);
        this.annualPercentageRate = annualPercentageRate;
    }

    @Override
    public double nextPaymentValue() {
        int countYears = getExpirationDate().getYear() - getCreationDate().getYear();
        return getBalance() * (1 + this.annualPercentageRate * countYears) / monthsQuantityBeforeExpiration();
    }

    @Override
    public LocalDate nextPaymentDate() {
        int countDays = 25 - LocalDate.now().getDayOfMonth();
        if (countDays <= 0) {
            return LocalDate.now().plusMonths(1).plusDays(countDays);
        }
        return LocalDate.now().plusDays(countDays);
    }

    @Override
    public double getAnnualPercentageRate() {
        return this.annualPercentageRate;
    }

    @Override
    public void setAnnualPercentageRate(double annualPercentageRate) {
        this.annualPercentageRate = annualPercentageRate;
    }

    @Override
    public void setNumber(String number) {
        checkNumber(number);
        super.setNumber(number);
    }

    private void checkNumber(String number) {
        if (!Pattern.compile(NUMBER_REGEX_FORMAT).matcher(number).matches()) {
            throw new InvalidAccountNumberException("Invalid credit account number");
        }
    }

    @Override
    public String toString() {
        return String.format("Credit account - %s APR: %f", super.toString(), this.annualPercentageRate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CreditAccount that = (CreditAccount) o;
        return Double.compare(that.annualPercentageRate, this.annualPercentageRate) == 0;
    }

    @Override
    public int hashCode() {
        return (int) (71 * super.hashCode() * this.annualPercentageRate);
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
