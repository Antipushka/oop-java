import java.text.ParseException;

/**
 * @author Dmitriy Antipin
 */
public class Individual implements Client {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int DEFAULT_SIZE = 0;
    private static final int DEFAULT_CREDIT_SCORE = 0;

    private Account[] accounts;
    private int size;
    private int creditScore;

    public Individual() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public Individual(int initialCapacity) {
        this.accounts = new Account[initialCapacity];
        this.size = DEFAULT_SIZE;
        this.creditScore = DEFAULT_CREDIT_SCORE;
    }

    public Individual(Account[] accounts) {
        this.accounts = new DebitAccount[accounts.length];
        System.arraycopy(accounts, 0, this.accounts, 0, accounts.length);
        this.size = accounts.length;
        this.creditScore = DEFAULT_CREDIT_SCORE;
    }

    @Override
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

    @Override
    public boolean addAccount(Account account, int index) {
        ensureCapacity();
        //todo а если это место пусто, надо ли двигать?
        if (this.accounts[index] != null) {
            shiftRight(index);
            this.size++;
        }
        this.accounts[index] = account;
        return true;
    }

    private void shiftRight(int startIndex) {
        System.arraycopy(this.accounts, startIndex, this.accounts, startIndex + 1, this.size - startIndex);
    }

    @Override
    public Account getAccount(int index) {
        return this.accounts[index];
    }

    @Override
    public Account setAccount(Account account, int index) {
        Account replaceable = this.accounts[index];
        this.accounts[index] = account;
        return replaceable;
    }

    @Override
    public Account getAccount(String number) {
        for (int i = 0; i < this.size; i++) {
            if (checkNumber(this.accounts[i], number)) {
                return this.accounts[i];
            }
        }
        return null;
    }

    private boolean checkNumber(Account account, String number) {
        return account.getNumber().equals(number);
    }

    @Override
    public boolean hasAccount(String number) {
        for (int i = 0; i < this.size; i++) {
            //todo и тут
            if (checkNumber(this.accounts[i], number)) {
                return true;
            }
        }
        return false;
    }

    //todo удаление сделать с универсальным названием removeAccount
    @Override
    public Account removeAccount(int index) {
        Account removable = this.accounts[index];
        shiftLeft(index);
        this.accounts[--this.size] = null;
        return removable;
    }

    private void shiftLeft(int startIndex) {
        //todo опять почему не arraycopy?
        System.arraycopy(this.accounts, startIndex + 1, this.accounts, startIndex, this.size - 1 - startIndex);
    }

    @Override
    public Account removeAccount(String number) {
        for (int i = 0; i < this.size; i++) {
            if (checkNumber(this.accounts[i], number)) {
                Account removable = this.accounts[i];
                shiftLeft(i);
                this.accounts[--this.size] = null;
                return removable;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Account[] getAccounts() {
        int j = 0;
        Account[] accounts = new DebitAccount[this.size];
        //todo зачем копировать, если ничего не меняется? могут ли здесь быть null'ы?
        for (int i = 0; i < this.size; i++) {
            if (this.accounts != null) {
                accounts[j++] = this.accounts[i];
            }
        }
        return accounts;
    }

    @Override
    public Account[] sortedAccountsByBalance() {
        Account[] sortedAccounts = new DebitAccount[this.size];
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

    @Override
    public double totalBalance() {
        double totalBalance = 0;
        for (int i = 0; i < this.size; i++) {
            totalBalance += this.accounts[i].getBalance();
        }
        return totalBalance;
    }

    @Override
    public int getCreditScores() {
        return this.creditScore;
    }

    @Override
    public void increaseCreditScores(int value) {
        this.creditScore += value;
    }

    @Override
    public Account[] getCredits() {
        Account[] credits = new Account[countCredits()];
        int j = 0;
        for (int i = 0; i < this.size; i++) {
            if (this.accounts[i] instanceof Credit) {
                credits[j++] = this.accounts[i];
            }
        }
        return credits;
    }

    public int countCredits() {
        int quantity = 0;
        for (int i = 0; i < this.size; i++) {
            if (this.accounts[i] instanceof Credit) {
                quantity++;
            }
        }
        return quantity;
    }
}
