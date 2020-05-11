import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author Dmitriy Antipin
 */
public class Individual implements Client, Cloneable {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int DEFAULT_SIZE = 0;
    private static final int DEFAULT_CREDIT_SCORE = 0;

    private String name;
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
    public boolean addAccount(Account account) throws DublicateAccountNumberException {
        throwExceptionIfHasAccount(account.getNumber());
        Objects.requireNonNull(account);
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
    public boolean addAccount(Account account, int index) throws DublicateAccountNumberException {
        throwExceptionIfHasAccount(account.getNumber());
        Objects.requireNonNull(account);
        checkIndex(index);
        ensureCapacity();
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
        checkIndex(index);
        return this.accounts[index];
    }

    @Override
    public Account setAccount(Account account, int index) throws DublicateAccountNumberException {
        throwExceptionIfHasAccount(account.getNumber());
        Objects.requireNonNull(account);
        checkIndex(index);
        Account replaceable = this.accounts[index];
        this.accounts[index] = account;
        return replaceable;
    }

    @Override
    public Account getAccount(String number) {
        Objects.requireNonNull(number);
        checkNumber(number);
        for (int i = 0; i < this.size; i++) {
            if (checkNumber(this.accounts[i], number)) {
                return this.accounts[i];
            }
        }
        throw new NoSuchElementException();
    }

    private boolean checkNumber(Account account, String number) {
        return account.getNumber().equals(number);
    }

    @Override
    public boolean hasAccount(String number) {
        Objects.requireNonNull(number);
        checkNumber(number);
        for (int i = 0; i < this.size; i++) {
            if (checkNumber(this.accounts[i], number)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Account removeAccount(int index) {
        checkIndex(index);
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
        Objects.requireNonNull(number);
        checkNumber(number);
        for (int i = 0; i < this.size; i++) {
            if (checkNumber(this.accounts[i], number)) {
                Account removable = this.accounts[i];
                shiftLeft(i);
                this.accounts[--this.size] = null;
                return removable;
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Account[] getAccounts() {
        int j = 0;
        Account[] accounts = new DebitAccount[this.size];
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean removeAccount(Account account) {
        Objects.requireNonNull(account);
        for (int i = 0; i < this.size; i++) {
            if (this.accounts[i].equals(account)) {
                removeAccount(i);
                return true;
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public int indexOf(Account account) {
        Objects.requireNonNull(account);
        for (int i = 0; i < this.size; i++) {
            if (this.accounts[i].equals(account)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public double totalDebt() {
        Account account;
        double totalDebt = 0;
        for (int i = 0; i < this.size; i++) {
            account = this.accounts[i];
            if (account instanceof CreditAccount) {
                totalDebt += (account.getBalance() / 100)
                        * ((CreditAccount) account).getAnnualPercentageRate();
            }
        }
        return totalDebt;
    }

    private void checkNumber(String number) {
        if (!Pattern.compile(Client.NUMBER_REGEX_PATTERN).matcher(number).matches()) {
            throw new InvalidAccountNumberException("Invalid account number");
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void throwExceptionIfHasAccount(String number) throws DublicateAccountNumberException {
        if (hasAccount(number)) {
            throw new DublicateAccountNumberException();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Client\n name: ")
                .append(this.name).append('\n')
                .append("creditScore: ").append(this.creditScore).append('\n');
        for (int i = 0; i < this.size; i++) {
            sb.append(this.accounts[i].toString()).append('\n');
        }
        sb.append("total: ").append(totalBalance());
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Individual that = (Individual) o;
        boolean equals = this.size == that.size &&
                this.creditScore == that.creditScore &&
                this.name.equals(that.name);
        if (equals) {
            for (int i = 0; i < this.size; i++) {
                if (!this.accounts[i].equals(that.accounts[i])) {
                    return false;
                }
            }
        }
        return equals;
    }

    @Override
    public int hashCode() {
        int result = this.name.hashCode() ^ this.size ^ this.creditScore;
        for (int i = 0; i < this.size; i++) {
            result ^= this.accounts.hashCode();
        }
        return result;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
