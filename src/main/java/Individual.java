/**
 * @author Dmitriy Antipin
 */
public class Individual {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int DEFAULT_SIZE = 0;

    private Account[] accounts;
    private int size;

    public Individual() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public Individual(int initialCapacity) {
        this.accounts = new Account[initialCapacity];
        this.size = DEFAULT_SIZE;
    }

    public Individual(Account[] accounts) {
        this.accounts = new Account[accounts.length];
        System.arraycopy(accounts, 0, this.accounts, 0, accounts.length);
        this.size = accounts.length;
    }

    public boolean addAccount(Account account) {
        ensureCapacity();
        this.accounts[size++] = account;
        return true;
    }

    private void ensureCapacity() {
        if (this.size == this.accounts.length) {
            Account[] grown = new Account[this.size * 2];
            System.arraycopy(this.accounts, 0, grown, 0, this.size);
            this.accounts = grown;
        }
    }

    public boolean addAccount(Account account, int index) {
        ensureCapacity();
        shiftRight(index);
        this.accounts[index] = account;
        this.size++;
        return true;
    }

    private void shiftRight(int startIndex) {
        for (int i = this.size; i > startIndex; i--) {
            this.accounts[i] = this.accounts[i - 1];
        }
    }

    public Account getAccountAt(int index) {
        return this.accounts[index];
    }

    public Account setAccount(Account account, int index) {
        Account replaceable = this.accounts[index];
        this.accounts[index] = account;
        return replaceable;
    }

    public Account getAccountByNumber(String number) {
        for (int i = 0; i < this.size; i++) {
            if (this.accounts[i].getNumber().equals(number)) {
                return this.accounts[i];
            }
        }
        return null;
    }

    public boolean hasAccountWithNumber(String number) {
        for (int i = 0; i < this.size; i++) {
            if (this.accounts[i].getNumber().equals(number)) {
                return true;
            }
        }
        return false;
    }

    public Account removeAccountAt(int index) {
        Account removable = this.accounts[index];
        shiftLeft(index);
        this.accounts[--this.size] = null;
        return removable;
    }

    private void shiftLeft(int startIndex) {
        for (int i = startIndex; i < this.size - 1; i++) {
            this.accounts[i] = this.accounts[i + 1];
        }
    }

    public Account removeAccountByNumber(String number) {
        for (int i = 0; i < this.size; i++) {
            if (this.accounts[i].getNumber().equals(number)) {
                Account removable = this.accounts[i];
                shiftLeft(i);
                this.accounts[--this.size] = null;
                return removable;
            }
        }
        return null;
    }

    public int size() {
        return this.size;
    }

    public Account[] getAccounts() {
        Account[] accounts = new Account[this.size];
        System.arraycopy(this.accounts, 0, accounts, 0, this.size);
        return accounts;
    }

    public Account[] sortedAccountsByBalance() {
        Account[] sortedAccounts = new Account[this.size];
        int j;
        for (int i = 0; i < this.size; i++) {
            j = i;
            while (j > 0 && sortedAccounts[j - 1].getBalance() > this.accounts[i].getBalance()) {
                sortedAccounts[j] = sortedAccounts[j - 1];
                j--;
            }
            sortedAccounts[j] = this.accounts[i];
        }
        return sortedAccounts;
    }

    public double totalBalance() {
        double totalBalance = 0;
        for (int i = 0; i < this.size; i++) {
            totalBalance += this.accounts[i].getBalance();
        }
        return totalBalance;
    }
}
