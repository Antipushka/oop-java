import java.util.NoSuchElementException;
import java.util.Objects;
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
            addAccount(accounts[i]);
        }
        return true;
    }

    @Override
    public boolean addAccount(Account account) throws DublicateAccountNumberException {
        Objects.requireNonNull(account);
        throwExceptionIfHasAccount(account.getNumber());
        this.tail.next = new Node(account, this.head);
        this.size++;
        return true;
    }

    @Override
    public boolean addAccount(Account account, int index) throws DublicateAccountNumberException {
        Objects.requireNonNull(account);
        checkIndex(index);
        if (index == this.size) {
            addAccount(account);
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
    public Account getAccount(int index) {
        return getNode(index).account;
    }

    @Override
    public Account getAccount(String number) {
        Objects.requireNonNull(number);
        checkNumber(number);
        for (Node node = this.head.next; node != this.tail.next; node = node.next) {
            if (checkNumber(node.account, number)) {
                return node.account;
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
        for (Node node = this.head.next; node != this.tail.next; node = node.next) {
            if (checkNumber(node.account, number)) {
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
    public Account removeAccount(int index) {
        checkIndex(index);
        Node node = getNode(index - 1);
        Account account = node.next.account;
        node.next = node.next.next;
        this.size--;
        return account;
    }

    @Override
    public Account removeAccount(String number) {
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
    public Account[] getAccounts() {
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
        Node node = this.head.next;
        Account[] sortedAccounts = new Account[this.size];
        int j;
        for (int i = 0; i < this.size; i++) {
            j = i;
            while (j > 0 && sortedAccounts[j - 1].getBalance() > node.account.getBalance()) {
                sortedAccounts[j] = sortedAccounts[j - 1];
                j--;
            }
            sortedAccounts[j] = node.account;
            node = node.next;
        }
        return sortedAccounts;
    }

    @Override
    public double totalBalance() {
        double totalBalance = 0;
        for (Node node = this.head.next; node != this.head; node = node.next) {
            totalBalance += node.account.getBalance();
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
    public Account[] getCredits() {
        Account[] credits = new Account[countCredits()];
        int i = 0;
        for (Node node = this.head.next; node != this.head; node = node.next) {
            if (node.account instanceof Credit) {
                credits[i++] = node.account;
            }
        }
        return credits;
    }

    public int countCredits() {
        int quantity = 0;
        Node node = this.head.next;
        while (node != this.head) {
            if (node.account instanceof Credit) {
                quantity++;
            }
            node = node.next;
        }
        return quantity;
    }

    @Override
    public boolean removeAccount(Account account) {
        Objects.requireNonNull(account);
        for (Node node = this.head.next; node != this.head; node = node.next) {
            if (node.next.account.equals(account)) {
                node.next = node.next.next;
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
        Account account;
        double totalDebt = 0;
        for (Node node = this.head.next; node != this.head; node = node.next) {
            account = node.account;
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
}
