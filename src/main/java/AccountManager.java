/**
 * @author Dmitriy Antipin
 */

//todo комментарии из Individual применимы сюда
public class AccountManager {

    private static final int DEFAULT_SIZE = 0;

    private Client[] clients;
    private int size;

    public AccountManager(int initialCapacity) {
        this.clients = new Client[initialCapacity];
        this.size = DEFAULT_SIZE;
    }

    public AccountManager(Client[] clients) {
        this.clients = new Client[clients.length];
        System.arraycopy(clients, 0, this.clients, 0, clients.length);
        this.size = clients.length;
    }

    public boolean addIndividual(Client client) {
        ensureCapacity();
        this.clients[this.size++] = client;
        return true;
    }

    public boolean addIndividual(Client client, int index) {
        ensureCapacity();
        if (this.clients[index] != null) {
            shiftRight(index);
            this.size++;
        }
        this.clients[index] = client;
        return true;
    }

    private void ensureCapacity() {
        if (this.size == this.clients.length) {
            Client[] grown = new Client[this.size * 2];
            System.arraycopy(this.clients, 0, grown, 0, this.size);
            this.clients = grown;
        }
    }

    private void shiftRight(int startIndex) {
        for (int i = this.size; i > startIndex; i--) {
            this.clients[i] = this.clients[i - 1];
        }
    }

    public Client getIndividual(int index) {
        return this.clients[index];
    }

    public Client setIndividual(Individual individual, int index) {
        Client replaceable = this.clients[index];
        this.clients[index] = individual;
        return replaceable;
    }

    public Client removeIndividual(int index) {
        Client removable = this.clients[index];
        shiftLeft(index);
        this.clients[--this.size] = null;
        return removable;
    }

    private void shiftLeft(int startIndex) {
        for (int i = startIndex; i < this.size - 1; i++) {
            this.clients[i] = this.clients[i + 1];
        }
    }

    public int size() {
        return this.size;
    }

    public Client[] getClients() {
        int j = 0;
        Client[] clients = new Client[this.size];
        for (int i = 0; i < this.size; i++) {
            if (this.clients[i] != null) {
                clients[j++] = this.clients[i];
            }
        }
        return clients;
    }

    public Client[] sortedByBalanceIndividuals() {
        Client[] sortedClients = new Client[this.size];
        int j;
        double totalBalance;
        for (int i = 0; i < this.size; i++) {
            j = i;
            totalBalance = this.clients[i].totalBalance();
            while (j > 0 && sortedClients[j - 1].totalBalance() > totalBalance) {
                sortedClients[j] = sortedClients[j - 1];
                j--;
            }
            sortedClients[j] = this.clients[i];
        }
        return sortedClients;
    }

    public Account getAccount(String accountNumber) {
        int i = 0;
        Account account = null;
        while (account == null && i < this.size) {
            account = this.clients[i++].getAccount(accountNumber);
        }
        return account;
    }

    public Account removeAccount(String accountNumber) {
        int i = 0;
        Account account = null;
        while (account == null && i < this.size) {
            account = this.clients[i++].removeAccount(accountNumber);
        }
        return account;
    }

    public Account setAccount(String accountNumber, DebitAccount account) {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.clients[i].size(); j++) {
                if (this.clients[i].getAccount(j).getNumber().equals(accountNumber)) {
                    return this.clients[i].setAccount(account, j);
                }
            }
        }
        return null;
    }
}
