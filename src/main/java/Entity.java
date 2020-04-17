/**
 * @author Dmitriy Antipin
 */
public class Entity implements Client {

    private static final String DEFAULT_TITLE = "";
    private static final int DEFAULT_SIZE = 0;

    private String title;
    private Node head;
    private Node tail;
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
    }

    public Entity(String title, Account[] accounts) {
        this(title);
        addAll(accounts);
    }

    public boolean addAll(Account[] accounts) {
        for (int i = 0; i < accounts.length; i++) {
            addAccount(accounts[i]);
        }
        return true;
    }

    @Override
    public boolean addAccount(Account account) {
        this.tail.next = new Node(account, this.head);
        this.size++;
        return true;
    }

    @Override
    public boolean addAccount(Account account, int index) {
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
        for (Node node = this.head.next; node != this.tail.next; node = node.next) {
            if (checkNumber(node.account, number)) {
                return node.account;
            }
        }
        return null;
    }

    private boolean checkNumber(Account account, String number) {
        return account.getNumber().equals(number);
    }

    @Override
    public boolean hasAccount(String number) {
        for (Node node = this.head.next; node != this.tail.next; node = node.next) {
            if (checkNumber(node.account, number)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Account setAccount(Account account, int index) {
        Node node = getNode(index);
        Account replaced = node.account;
        node.account = account;
        return replaced;
    }

    @Override
    public Account removeAccount(int index) {
        Node node = getNode(index - 1);
        Account account = node.next.account;
        node.next = node.next.next;
        this.size--;
        return account;
    }

    @Override
    public Account removeAccount(String number) {
        for (Node node = this.head; node.next != this.head; node = node.next) {
            if (checkNumber(node.next.account, number)) {
                Account removed = node.next.account;
                node.next = node.next.next;
                this.size--;
                return removed;
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
    }
}
