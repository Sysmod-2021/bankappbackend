import model.Account;
import model.Bank;
import model.Currency;
import model.Customer;
import org.fulib.FulibTools;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class IbanTests {
    @Test
    public void IbanGet() throws Bank.CustomerExistsException, Bank.AccountExistsException {
        // Init data
        Bank bank = new Bank();
        Customer customer = bank.createCustomer("Bob", "Jackson", "haha@tt.ee", "secret", 0.0, Currency.EUR);
        Account account = customer.getAccount();
        String accountIban = account.getIban();

        // Assert IBAN
        assertTrue(accountIban.startsWith("EE"));

        FulibTools.objectDiagrams().dumpSVG("docs/objects/iban_1.svg", bank);
    }
}
