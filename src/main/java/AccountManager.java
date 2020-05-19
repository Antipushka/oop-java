import java.util.*;

/**
 * @author Dmitriy Antipin
 */

public class AccountManager implements Iterable<Client> {

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
        Objects.requireNonNull(client);
        ensureCapacity();
        this.clients[this.size++] = client;
        return true;
    }

    public boolean addIndividual(Client client, int index) {
        Objects.requireNonNull(client);
        checkIndex(index);
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
        checkIndex(index);
        return this.clients[index];
    }

    public Client setIndividual(Individual individual, int index) {
        Objects.requireNonNull(individual);
        checkIndex(index);
        Client replaceable = this.clients[index];
        this.clients[index] = individual;
        return replaceable;
    }

    public Client removeClient(int index) {
        checkIndex(index);
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
        for (Client client : this) {
            if (client != null) {
                clients[j++] = client;
            }
        }
        return clients;
    }

    public List<Client> sortedByBalanceClients() {
        List<Client> sortedClients = new ArrayList<>(this.size);
        for (Client client : this) {
            sortedClients.add(client);
        }
        Collections.sort(sortedClients);
        return sortedClients;
    }

    public Account getAccount(String accountNumber) {
        Objects.requireNonNull(accountNumber);
        int i = 0;
        Account account = null;
        while (account == null && i < this.size) {
            account = this.clients[i++].get(accountNumber);
        }
        return account;
    }

    public Account removeAccount(String accountNumber) {
        Objects.requireNonNull(accountNumber);
        int i = 0;
        Account account = null;
        while (account == null && i < this.size) {
            account = this.clients[i++].remove(accountNumber);
        }
        return account;
    }

    public Account setAccount(String accountNumber, DebitAccount account) throws DublicateAccountNumberException {
        Objects.requireNonNull(accountNumber);
        Objects.requireNonNull(account);
        int j = 0;
        for (Client client : this) {
            for (Account a : client) {
                if (a.getNumber().equals(accountNumber)) {
                    return client.setAccount(account, j);
                }
                j++;
            }
            j = 0;
        }
        return null;
    }

    public Set<Client> getDebtors() {
        Set<Client> debtors = new HashSet<>(countDebtors());
        for (Client client : this) {
            if (client.countCredits() > 0) {
                debtors.add(client);
            }
        }
        return debtors;
    }

    public Set<Client> getWickedDebtors() {
        Set<Client> wickedDebtors = new HashSet<>(countWickedDebtors());
        for (Client client : this) {
            if (client.countCredits() > 0 &&
                    client.getStatus() == ClientStatus.BAD) {
                wickedDebtors.add(client);
            }
        }
        return wickedDebtors;
    }

    private int countDebtors() {
        int quantity = 0;
        for (Client client : this) {
            if (client.countCredits() > 0) {
                quantity++;
            }
        }
        return quantity;
    }

    private int countWickedDebtors() {
        int quantity = 0;
        for (Client client : this) {
            if (client.countCredits() > 0 &&
                    client.getStatus() == ClientStatus.BAD) {
                quantity++;
            }
        }
        return quantity;
    }

    public boolean removeClient(Client client) {
        Objects.requireNonNull(client);
        for (int i = 0; i < this.size; i++) {
            if (this.clients[i].equals(client)) {
                removeClient(i);
                return true;
            }
        }
        return false;
    }

    public int indexOf(Client client) {
        Objects.requireNonNull(client);
        for (int i = 0; i < this.size; i++) {
            if (this.clients[i].equals(client)) {
                return i;
            }
        }
        return -1;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
    }


    @Override
    public Iterator<Client> iterator() {
        return new ClientIterator();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.size; i++) {
            stringBuilder.append(this.clients[i].toString()).append('\n');
        }
        return stringBuilder.toString();
    }

    private class ClientIterator implements Iterator<Client> {

        private int i = 0;

        @Override
        public boolean hasNext() {
            return i < size;
        }

        @Override
        public Client next() {
            if (hasNext()) {
                return clients[i++];
            }
            throw new NoSuchElementException();
        }
    }
}
