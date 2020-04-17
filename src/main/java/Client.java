/**
 * @author Dmitriy Antipin
 */
public interface Client {

    boolean addAccount(Account account);

    boolean addAccount(Account account, int index);

    Account getAccount(int index);

    Account getAccount(String number);

    boolean hasAccount(String number);

    Account setAccount(Account account, int index);

    Account removeAccount(int index);

    Account removeAccount(String number);

    int size();

    Account[] getAccounts();

    Account[] sortedAccountsByBalance();

    double totalBalance();
}
