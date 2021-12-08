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
        Administrator admin = bank.createAdministrator("Admin", "Alice", ADMIN_EMAIL, "secure_p@ssw0|2d"); // NOTE: Factory pattern
        assertEquals(ADMIN_EMAIL, bank.getAdministrators().get(0).getEmail());

        FulibTools.objectDiagrams().dumpSVG("docs/objects/account_tests_1.svg", bank);
    }

    @Test
    public void testCreateAdministratorFailure() throws Bank.AdministratorExistsException {
        Bank bank = new Bank();
        Administrator admin1 = bank.createAdministrator("Alice", "Brown", ADMIN_EMAIL, "secure_p@ssw0|2d");
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
        Customer customer = bank.createCustomer("johntest", "Doe", "johntest@doe.ee", "pass1234", initial_balance, Currency.EUR);
        assertEquals("johntest@doe.ee", bank.getCustomerByEmail("johntest@doe.ee").getEmail());

        FulibTools.objectDiagrams().dumpSVG("docs/objects/account_tests_3.svg", bank);
    }

    @Test
    public void testCreateCustomerAlreadyExisting() throws Bank.CustomerExistsException, Bank.AccountExistsException {
        String existing_email = "johntest@doe.ee";

        Bank bank = new Bank();
        bank.createCustomer("johntest", "Doe", existing_email, "pass1234", 100.00, Currency.EUR);
        
        Throwable existing = assertThrows(Bank.CustomerExistsException.class, () -> {
                    bank.createCustomer("Johnny", "Dude", existing_email, "I-forgot", 500.00, Currency.EUR);
                });
        
        assertEquals("Customer exists: " + existing_email, existing.getMessage());

        FulibTools.objectDiagrams().dumpSVG("docs/objects/account_tests_4.svg", bank);
    }

    @Test
    public void testLoginWithWrongPassword() throws Bank.CustomerExistsException, Bank.AccountExistsException {
        Bank bank = new Bank();
        Customer customer = bank.createCustomer("johntest", "Doe", "johndoe@yopmail.com", "p@$$w0rd", 100.00, Currency.EUR);
        
        Throwable badCredentials = assertThrows(Exception.class, () -> {
            bank.authenticate("johndoe@yopmail.com", "password1");
        });

        assertEquals("Wrong Credentials", badCredentials.getMessage());

        FulibTools.objectDiagrams().dumpSVG("docs/objects/account_tests_5.svg", bank);
    }

    @Test
    public void testLoginWithNotExistingEmail() throws Bank.CustomerExistsException, Bank.AccountExistsException {
    	String not_existing_email = "johndoe_abc@yopmail.com";

        Bank bank = new Bank();
        Customer customer = bank.createCustomer("johntest", "Doe", "johndoe@yopmail.com", "p@$$w0rd", 100.00, Currency.EUR);

        Throwable badCredentials = assertThrows(Bank.CustomerDoesNotExistException.class, () -> {
            bank.authenticate(not_existing_email, "p@$$w0rd");
        });

        assertEquals("Customer" + not_existing_email + "does not exist in the bank", badCredentials.getMessage());

        FulibTools.objectDiagrams().dumpSVG("docs/objects/account_tests_6.svg", bank);
    }
}
