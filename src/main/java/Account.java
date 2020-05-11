import java.time.LocalDate;

/**
 * @author Dmitriy Antipin
 */
public interface Account {

    String getNumber();

    void setNumber(String number);

    double getBalance();

    void setBalance(double balance);

    LocalDate getCreationDate();

    void setCreationDate(LocalDate creationDate);

    LocalDate getExpirationDate();

    void setExpirationDate(LocalDate expirationDate);

    int monthsQuantityBeforeExpiration();
}
