/**
 * @author Dmitriy Antipin
 */
public class Test {

    public static void main(String[] args) {

        DebitAccount account = new DebitAccount("9", 9);
        DebitAccount account1 = new DebitAccount("8", 8);
        DebitAccount account2 = new DebitAccount("7", 7);
        DebitAccount account3 = new DebitAccount("6", 6);
        DebitAccount account4 = new DebitAccount("5", 5);
        DebitAccount account5 = new DebitAccount("4", 4);
        DebitAccount account6 = new DebitAccount("3", 3);
        DebitAccount account7 = new DebitAccount("2", 2);
        DebitAccount account8 = new DebitAccount("1", 1);

        DebitAccount[] accounts = { account, account1, account2 };

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

    public static void printAccounts(DebitAccount[] accounts) {
        for (int i = 0; i < accounts.length; i++) {
            System.out.println("Number: " + accounts[i].getNumber() + " Balance: " + accounts[i].getBalance());
        }
    }

    public static void removeTest(Individual individual) {
        individual.removeAccount("8");
        System.out.println();
        System.out.println("Remove: ");
        printAccounts(individual.getAccounts());
    }

    public static void setTest(Individual individual) {
        individual.setAccount(new DebitAccount("123", 123), 1);
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
