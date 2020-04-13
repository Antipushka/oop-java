/**
 * @author Dmitriy Antipin
 */
public class AccountManager {

    private static final int DEFAULT_SIZE = 0;

    private Individual[] individuals;
    private int size;

    public AccountManager(int initialCapacity) {
        this.individuals = new Individual[initialCapacity];
        this.size = DEFAULT_SIZE;
    }

    public AccountManager(Individual[] individuals) {
        this.individuals = new Individual[individuals.length];
        System.arraycopy(individuals, 0, this.individuals, 0, individuals.length);
        this.size = individuals.length;
    }

    public boolean addIndividual(Individual individual) {
        ensureCapacity();
        this.individuals[this.size++] = individual;
        return true;
    }

    public boolean addIndividual(Individual individual, int index) {
        ensureCapacity();
        shiftRight(index);
        this.individuals[index] = individual;
        this.size++;
        return true;
    }

    private void ensureCapacity() {
        if (this.size == individuals.length) {
            Individual[] newIndividuals = new Individual[this.size * 2];
            System.arraycopy(this.individuals, 0, newIndividuals, 0, this.size);
            this.individuals = newIndividuals;
        }
    }

    private void shiftRight(int startIndex) {
        for (int i = this.size; i > startIndex; i--) {
            this.individuals[i] = this.individuals[i - 1];
        }
    }

    public Individual getIndividualAt(int index) {
        return this.individuals[index];
    }

    public Individual setIndividual(Individual individual, int index) {
        Individual replaceable = this.individuals[index];
        this.individuals[index] = individual;
        return replaceable;
    }

    public Individual removeIndividualAt(int index) {
        Individual removable = this.individuals[index];
        shiftLeft(index);
        this.size--;
        return removable;
    }

    private void shiftLeft(int startIndex) {
        for (int i = startIndex; i < this.size - 1; i++) {
            this.individuals[i] = this.individuals[i + 1];
        }
    }

    public int size() {
        return this.size;
    }

    public Individual[] getIndividuals() {
        Individual[] individuals = new Individual[this.size];
        System.arraycopy(this.individuals, 0, individuals, 0, this.size);
        return individuals;
    }

    public Individual[] sortedByBalanceIndividuals() {
        Individual[] sortedIndividuals = new Individual[this.size];
        int j;
        double totalBalance;
        for (int i = 0; i < this.size; i++) {
            j = i;
            totalBalance = this.individuals[i].totalBalance();
            while (j > 0 && sortedIndividuals[j - 1].totalBalance() > totalBalance) {
                sortedIndividuals[j] = sortedIndividuals[j - 1];
                j--;
            }
            sortedIndividuals[j] = this.individuals[i];
        }
        return sortedIndividuals;
    }

    public Account getAccountByNumber(String accountNumber) {
        int i = 0;
        Account account = null;
        while (account == null && i < this.size) {
            account = this.individuals[i++].getAccountByNumber(accountNumber);
        }
        return account;
    }

    public Account removeAccountByNumber(String accountNumber) {
        int i = 0;
        Account account = null;
        while (account == null && i < this.size) {
            account = this.individuals[i++].removeAccountByNumber(accountNumber);
        }
        return account;
    }

    public Account setAccount(String accountNumber, Account account) {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.individuals[i].size(); j++) {
                if (this.individuals[i].getAccountAt(j).getNumber().equals(accountNumber)) {
                    return this.individuals[i].setAccount(account, j);
                }
            }
        }
        return null;
    }
}
