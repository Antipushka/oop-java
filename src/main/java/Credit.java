import java.time.LocalDate;

/**
 * @author Dmitriy Antipin
 */
public interface Credit {

    double getAnnualPercentageRate();

    void setAnnualPercentageRate(double annualPercentageRate);

    double nextPaymentValue();

    LocalDate nextPaymentDate();
}
