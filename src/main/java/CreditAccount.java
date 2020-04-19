/**
 * @author Dmitriy Antipin
 */
public class CreditAccount extends AbstractAccount implements Credit {

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
}
