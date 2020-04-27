
/**
 * @author Dmitriy Antipin
 */
public class CreditAccount extends AbstractAccount implements Credit, Cloneable {

    private static final double DEFAULT_ANNUAL_PERCENTAGE_RATE = 30;

    private double annualPercentageRate;

    public CreditAccount() {
        super();
        this.annualPercentageRate = DEFAULT_ANNUAL_PERCENTAGE_RATE;
    }

    public CreditAccount(String number, double balance, double annualPercentageRate) {
        super(number, balance);
        this.annualPercentageRate = annualPercentageRate;
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
