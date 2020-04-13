import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Dmitriy Antipin
 */
public class AccountTest {

    @Test
    public void setNumber() {
        Account account = new Account();
        account.setNumber("Number");
        assertEquals("Number", account.getNumber());
    }

    @Test
    public void setBalance() {
        Account account = new Account();
        account.setBalance(100);
        assertEquals(100, account.getBalance(), 0);
    }

    @Test
    public void constructorWithArgs() {
        Account account = new Account("1234", 10);
        assertEquals("1234", account.getNumber());
        assertEquals(10, account.getBalance(), 0);
    }

    @Test
    public void constructorWithoutArgs() {
        Account account = new Account();
        assertEquals("", account.getNumber());
        assertEquals(0, account.getBalance(), 0);
    }
}