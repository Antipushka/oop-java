import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Dmitriy Antipin
 */
public class Test {
    public static void main(String[] args) throws DublicateAccountNumberException {

        DebitAccount account0 = new DebitAccount("9", LocalDate.now());
        DebitAccount account1 = new DebitAccount("8", LocalDate.now());
        DebitAccount account2 = new DebitAccount("7", LocalDate.now());
        DebitAccount account3 = new DebitAccount("6", LocalDate.now());
        DebitAccount account4 = new DebitAccount("5", LocalDate.now());
        DebitAccount account5 = new DebitAccount("4", LocalDate.now());
        DebitAccount account6 = new DebitAccount("3", LocalDate.now());
        DebitAccount account7 = new DebitAccount("2", LocalDate.now());
        DebitAccount account8 = new DebitAccount("1", LocalDate.now());

        Account[] accounts = { account0, account1, account2 };

        Individual individual = new Individual(accounts);

        Individual individual1 = new Individual(3);
        individual1.add(account3);
        individual1.add(account4);
        individual1.add(account5);

        Individual individual2 = new Individual();
        individual2.add(account6);
        individual2.add(account7);
        individual2.add(account8);

        System.out.println("Individual: ");
        printAccounts(individual.toArray());

        System.out.println();

        System.out.println("Individual1: ");
        printAccounts(individual1.toArray());

        System.out.println();

        System.out.println("Individual2: ");
        printAccounts(individual2.toArray());

        removeTest(individual);

        setTest(individual);

        sortTest(individual2);

        totalBalanceTest(individual2);

        Individual[] individuals = { individual, individual1 };

        AccountManager accountManager = new AccountManager(individuals);
        accountManager.addIndividual(individual2);

        printTotalBalances(accountManager.sortedByBalanceClients());
    }

    public static void printAccounts(Account[] accounts) {
        for (int i = 0; i < accounts.length; i++) {
            System.out.println("Number: " + accounts[i].getNumber() + " Balance: " + accounts[i].getBalance());
        }
    }

    public static void removeTest(Individual individual) {
        individual.remove("8");
        System.out.println();
        System.out.println("Remove: ");
        printAccounts(individual.toArray());
    }

    public static void setTest(Individual individual) throws DublicateAccountNumberException {
        individual.setAccount(new DebitAccount("123", LocalDate.now()), 1);
        System.out.println();
        System.out.println("Set: ");
        printAccounts(individual.toArray());
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

    public static  void printTotalBalances(List<Client> clients) {
        System.out.println();
        for (int i = 0; i < clients.size(); i++) {
            System.out.println("Total balance " + i +": " + clients.get(i).totalBalance());
        }
    }
}
