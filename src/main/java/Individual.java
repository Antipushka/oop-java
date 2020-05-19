import java.util.*;
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
    public boolean add(Account account) {
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
    public boolean add(Account account, int index) throws DublicateAccountNumberException {
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
    public Account get(int index) {
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
    public Account get(String number) {
        Objects.requireNonNull(number);
        checkNumber(number);
        for (Account account : this) {
            if (checkNumber(account, number)) {
                return account;
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
        for (Account account : this) {
            if (checkNumber(account, number)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Account remove(int index) {
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
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (Account account : this) {
            if (account.equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        int i = 0;
        for (Account account : this) {
            a[i++] = (T) account;
        }
        return a;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < this.size; i++) {
            if (this.accounts[i].equals(o)) {
                remove(i);
                this.size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object object : c) {
            if (!contains(c)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Account> c) {
        for (Account account : c) {
            add(account);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object object : c) {
            remove(object);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        for (Account account : this) {
            if (!c.contains(account)) {
                remove(account);
            }
        }
        return true;
    }

    @Override
    public void clear() {
        for (int i = this.size; i >= 0; i--) {
            this.accounts[i] = null;
            this.size--;
        }
    }

    @Override
    public Account remove(String number) {
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
    public Account[] toArray() {
        int j = 0;
        Account[] accounts = new DebitAccount[this.size];
        for (Account account : this) {
            if (this.accounts != null) {
                accounts[j++] = account;
            }
        }
        return accounts;
    }

    @Override
    public Account[] sortedAccountsByBalance() {
        Account[] accounts = toArray();
        List<Account> sortedAccounts = new ArrayList<Account>(Arrays.asList(accounts));
        Collections.sort(sortedAccounts);
        return sortedAccounts;
    }

    @Override
    public double totalBalance() {
        double totalBalance = 0;
        for (Account account : this) {
            totalBalance += account.getBalance();
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
    public Collection<Account> getCredits() {
        LinkedList<Account> credits = new LinkedList<>();
        for (Account account : this) {
            if (account instanceof Credit) {
                credits.add(account);
            }
        }
        return credits;
    }

    public int countCredits() {
        int quantity = 0;
        for (Account account : this) {
            if (account instanceof Credit) {
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

    public boolean remove(Account account) {
        Objects.requireNonNull(account);
        for (int i = 0; i < this.size; i++) {
            if (this.accounts[i].equals(account)) {
                remove(i);
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
        double totalDebt = 0;
        for (Account account : this) {
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
    public Iterator<Account> iterator() {
        return new AccountIterator();
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

    private class AccountIterator implements Iterator<Account> {

        private int i = 0;

        @Override
        public boolean hasNext() {
            return i < size;
        }

        @Override
        public Account next() {
            if (i < size) {
                return accounts[i++];
            }
            throw new NoSuchElementException();
        }
    }
}
