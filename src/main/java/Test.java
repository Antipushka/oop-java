/**
 * @author Dmitriy Antipin
 */
public class Test {

    public static void main(String[] args) {

        Account account = new Account("9", 9);
        Account account1 = new Account("8", 8);
        Account account2 = new Account("7", 7);
        Account account3 = new Account("6", 6);
        Account account4 = new Account("5", 5);
        Account account5 = new Account("4", 4);
        Account account6 = new Account("3", 3);
        Account account7 = new Account("2", 2);
        Account account8 = new Account("1", 1);

        Account[] accounts = { account, account1, account2 };

        Individual individual = new Individual(accounts);

        Individual individual1 = new Individual(3);
        individual1.addAccount(account3);
        individual1.addAccount(account4);
        individual1.addAccount(account5);

        Individual individual2 = new Individual();
        individual2.addAccount(account6);
        individual2.addAccount(account7);
        individual2.addAccount(account8);

        System.out.println("Individual: ");
        printAccounts(individual.getAccounts());

        System.out.println();

        System.out.println("Individual1: ");
        printAccounts(individual1.getAccounts());

        System.out.println();

        System.out.println("Individual2: ");
        printAccounts(individual2.getAccounts());

        removeTest(individual);

        setTest(individual);

        sortTest(individual2);

        totalBalanceTest(individual2);

        Individual[] individuals = { individual, individual1 };

        AccountManager accountManager = new AccountManager(individuals);
        accountManager.addIndividual(individual2);

        printTotalBalances(accountManager.sortedByBalanceIndividuals());
    }

    public static void printAccounts(Account[] accounts) {
        for (int i = 0; i < accounts.length; i++) {
            System.out.println("Number: " + accounts[i].getNumber() + " Balance: " + accounts[i].getBalance());
        }
    }

    public static void removeTest(Individual individual) {
        individual.removeAccountByNumber("8");
        System.out.println();
        System.out.println("Remove: ");
        printAccounts(individual.getAccounts());
    }

    public static void setTest(Individual individual) {
        individual.setAccount(new Account("123", 123), 1);
        System.out.println();
        System.out.println("Set: ");
        printAccounts(individual.getAccounts());
    }

    public static void sortTest(Individual individual) {
        System.out.println();
        System.out.println("Sort: ");
        printAccounts(individual.sortedAccountsByBalance());
    }

    public static void totalBalanceTest(Individual individual) {
        System.out.println();
        System.out.println("Total balance: " + individual.totalBalance());
    }

    public static  void printTotalBalances(Individual[] individuals) {
        System.out.println();
        for (int i = 0; i < individuals.length; i++) {
            System.out.println("Total balance " + i +": " + individuals[i].totalBalance());
        }
    }
}
