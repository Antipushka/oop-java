import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Dmitriy Antipin
 */
public class AccountTest {

    @Test
    public void setNumber() {
        DebitAccount account = new DebitAccount();
        account.setNumber("Number");
        assertEquals("Number", account.getNumber());
    }

    @Test
    public void setBalance() {
        DebitAccount account = new DebitAccount();
        account.setBalance(100);
        assertEquals(100, account.getBalance(), 0);
    }

    @Test
    public void constructorWithArgs() {
        DebitAccount account = new DebitAccount("1234", 10);
        assertEquals("1234", account.getNumber());
        assertEquals(10, account.getBalance(), 0);
    }

    @Test
    public void constructorWithoutArgs() {
        DebitAccount account = new DebitAccount();
        assertEquals("", account.getNumber());
        assertEquals(0, account.getBalance(), 0);
    }
}