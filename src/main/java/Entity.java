import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Dmitriy Antipin
 */
public class Entity implements Client, Cloneable {

    private static final String DEFAULT_TITLE = "";
    private static final int DEFAULT_SIZE = 0;
    private  static final int DEFAULT_CREDIT_SCORE = 0;

    private String title;
    private Node head;
    private Node tail;
    private int creditScore;
    private int size;

    public Entity() {
        this(DEFAULT_TITLE);
    }

    public Entity(String title) {
        this.title = title;
        this.head = new Node();
        this.tail = this.head;
        this.head.next = this.tail;
        this.size = DEFAULT_SIZE;
        this.creditScore = DEFAULT_CREDIT_SCORE;
    }

    public Entity(String title, Account[] accounts) throws DublicateAccountNumberException {
        this(title);
        addAll(accounts);
        this.creditScore = DEFAULT_CREDIT_SCORE;
    }

    public boolean addAll(Account[] accounts) throws DublicateAccountNumberException {
        Objects.requireNonNull(accounts);
        for (int i = 0; i < accounts.length; i++) {
            add(accounts[i]);
        }
        return true;
    }

    @Override
    public boolean add(Account account) {
        Objects.requireNonNull(account);
        this.tail.next = new Node(account, this.head);
        this.size++;
        return true;
    }

    @Override
    public boolean add(Account account, int index) throws DublicateAccountNumberException {
        Objects.requireNonNull(account);
        checkIndex(index);
        if (index == this.size) {
            add(account);
        } else {
            Node node = getNode(index - 1);
            Node shifted = node.next;
            node.next = new Node(account);
            node.next.next = shifted;
            this.size++;
        }
        return true;
    }

    private Node getNode(int index) {
        Node node = this.head.next;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node;
    }

    @Override
    public Account get(int index) {
        return getNode(index).account;
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
    public Account setAccount(Account account, int index) throws DublicateAccountNumberException {
        Objects.requireNonNull(account);
        throwExceptionIfHasAccount(account.getNumber());
        checkIndex(index);
        Node node = getNode(index);
        Account replaced = node.account;
        node.account = account;
        return replaced;
    }

    @Override
    public Account remove(int index) {
        checkIndex(index);
        Node node = getNode(index - 1);
        Account account = node.next.account;
        node.next = node.next.next;
        this.size--;
        return account;
    }

    @Override
    public Account remove(String number) {
        Objects.requireNonNull(number);
        checkNumber(number);
        for (Node node = this.head; node.next != this.head; node = node.next) {
            if (checkNumber(node.next.account, number)) {
                Account removed = node.next.account;
                node.next = node.next.next;
                this.size--;
                return removed;
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
        Node node = this.head.next;
        Account[] accounts = new Account[this.size];
        for (int i = 0; i < this.size; i++) {
            accounts[i] = node.account;
            node = node.next;
        }
        return accounts;
    }

    @Override
    public Account[] sortedAccountsByBalance() {
        ArrayList<Account> sortedAccounts = new ArrayList<>(this);
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

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @Override
    public boolean remove(Object account) {
        Objects.requireNonNull(account);
        for (Node node = this.head.next; node != this.head; node = node.next) {
            if (node.next.account.equals(account)) {
                node.next = node.next.next;
                this.size--;
                return true;
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public int indexOf(Account account) {
        Objects.requireNonNull(account);
        Node node = this.head.next;
        for (int i = 0; i < this.size; i++) {
            if (node.account.equals(account)) {
                return i;
            }
            node = node.next;
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
    public boolean containsAll(Collection<?> c) {
        for (Object object : c) {
            if (!contains(object)) {
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
        for (Account account : this) {
            remove(account);
        }
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
        StringBuilder sb = new StringBuilder("Client\n title: ")
                .append(this.title).append('\n')
                .append("creditScore: ").append(this.creditScore).append('\n');
        for (Node node = this.head.next; node != this.head; node = node.next) {
            sb.append(node.account.toString()).append('\n');
        }
        sb.append("total: ").append(totalBalance());
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        boolean equals = this.creditScore == entity.creditScore &&
                this.size == entity.size &&
                this.title.equals(entity.title) &&
                this.head.equals(entity.head) &&
                this.tail.equals(entity.tail);
        if (equals) {
            Node thisNode = this.head.next;
            Node thatNode = entity.head.next;
            while (thisNode != this.head) {
                if (!thisNode.account.equals(thatNode.account)) {
                    return false;
                }
                thisNode = thisNode.next;
                thatNode = thatNode.next;
            }
        }
        return equals;
    }

    @Override
    public int hashCode() {
        int result = this.title.hashCode() ^ this.creditScore ^ this.size;
        for (Node node = this.head.next; node != this.head; node = node.next) {
            result ^= node.account.hashCode();
        }
        return result;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private static class Node {

        Account account;
        Node next;

        private Node() {

        }

        private Node(Account account) {
            this.account = account;
        }

        private Node(Account account, Node next) {
            this.account = account;
            this.next = next;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return this.account.equals(node.account) &&
                    this.next.equals(node.next);
        }

        @Override
        public int hashCode() {
            return Objects.hash(account, next);
        }
    }

    private class AccountIterator implements Iterator<Account> {

        private Node node = head.next;

        @Override
        public boolean hasNext() {
            return node != head;
        }

        @Override
        public Account next() {
            if (hasNext()) {
                Account account = node.account;
                node = node.next;
                return account;
            }
            throw new NoSuchElementException();
        }
    }
}
