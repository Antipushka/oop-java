import java.util.Collection;

/**
 * @author Dmitriy Antipin
 */
public interface Client extends Iterable<Account>, Comparable<Client>, Collection<Account> {

    String NUMBER_REGEX_PATTERN = "4[045]\\d{3}810\\d{4}[1-9]\\d{6}[1-9]";

    boolean add(Account account);

    boolean add(Account account, int index) throws DublicateAccountNumberException;

    Account get(int index);

    Account get(String number);

    boolean hasAccount(String number);

    Account setAccount(Account account, int index) throws DublicateAccountNumberException;

    Account remove(int index);

    Account remove(String number);

    int indexOf(Account account);

    double totalDebt();

    Account[] sortedAccountsByBalance();

    double totalBalance();

    int getCreditScores();

    void increaseCreditScores(int value);

    Collection<Account> getCredits();

    int countCredits();

    default ClientStatus getStatus() {
        if (getCreditScores() <= ClientStatus.BAD.getCreditScoreBound()) {
            return ClientStatus.BAD;
        } else if (getCreditScores() >= ClientStatus.RISKY.getCreditScoreBound() &&
                getCreditScores() < ClientStatus.GOOD.getCreditScoreBound()) {
            return ClientStatus.RISKY;
        } else if (getCreditScores() >= ClientStatus.GOOD.getCreditScoreBound() &&
                getCreditScores() < ClientStatus.GOLD.getCreditScoreBound()) {
            return ClientStatus.GOLD;
        } else {
            return ClientStatus.PLATINUM;
        }
    }

    @Override
    default int compareTo(Client o) {
        return Double.compare(totalBalance(), o.totalBalance());
    }
}
