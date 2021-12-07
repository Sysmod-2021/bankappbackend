import model.Administrator;
import model.Bank;
import model.Currency;
import model.Customer;
import org.fulib.FulibTools;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

// Full model.Bank specification is available at
// https://docs.google.com/document/d/1AjevAXSdgcHC6yfwMc1QbsvFRuz3pfRowHIOu4XMXEU/edit#

// FR #1. model.Account creation
public class AccountTests {
    static final String ADMIN_EMAIL = "admin@bank.ee";

    @Test
    public void testCreateAdministrator() throws Bank.AdministratorExistsException {
        Bank bank = new Bank();
        bank.createAdministrator("Admin", "Alice", ADMIN_EMAIL, "secure_p@ssw0|2d"); // NOTE: Factory pattern
        assertEquals(ADMIN_EMAIL, bank.getAdministrators().get(0).getEmail());

        FulibTools.objectDiagrams().dumpSVG("docs/objects/account_tests_1.svg", bank);
    }

    @Test
    public void testCreateAdministratorFailure() throws Bank.AdministratorExistsException {
        Bank bank = new Bank();
        bank.createAdministrator("Alice", "Brown", ADMIN_EMAIL, "secure_p@ssw0|2d");
        assertThrows(Bank.AdministratorExistsException.class, () -> {
            bank.createAdministrator("Alice", "Peterson", ADMIN_EMAIL, "I-forgot");
        });

        FulibTools.objectDiagrams().dumpSVG("docs/objects/account_tests_2.svg", bank);
    }

    @Test
    public void testCreateCustomer() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.CustomerDoesNotExistException {
        Bank bank = new Bank();
        Double initial_balance = 100.0;
        // NOTE: Factory pattern
        // bank.createCustomer creates a customer and an account
        bank.createCustomer("johntest", "Doe", "johntest@doe.ee", "pass1234", initial_balance, Currency.EUR);
        assertEquals("johntest@doe.ee", bank.getCustomerByEmail("johntest@doe.ee").getEmail());

        FulibTools.objectDiagrams().dumpSVG("docs/objects/account_tests_3.svg", bank);
    }
}
