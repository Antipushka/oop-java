/**
 * @author Dmitriy Antipin
 */
public interface Client extends Iterable<Account>, Comparable<Client> {

    String NUMBER_REGEX_PATTERN = "4[045]\\d{3}810\\d{4}[1-9]\\d{6}[1-9]";

    boolean addAccount(Account account) throws DublicateAccountNumberException;

    boolean addAccount(Account account, int index) throws DublicateAccountNumberException;

    Account getAccount(int index);

    Account getAccount(String number);

    boolean hasAccount(String number);

    Account setAccount(Account account, int index) throws DublicateAccountNumberException;

    Account removeAccount(int index);

    Account removeAccount(String number);

    boolean removeAccount(Account account);

    int indexOf(Account account);

    double totalDebt();

    int size();

    Account[] getAccounts();

    Account[] sortedAccountsByBalance();

    double totalBalance();

    int getCreditScores();

    void increaseCreditScores(int value);

    Account[] getCredits();

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
