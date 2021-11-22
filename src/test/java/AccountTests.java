import org.fulib.FulibTools;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

// Full Bank specification is available at
// https://docs.google.com/document/d/1AjevAXSdgcHC6yfwMc1QbsvFRuz3pfRowHIOu4XMXEU/edit#

// FR #1. Account creation
public class AccountTests {
    static final String ADMIN_EMAIL = "admin@bank.ee";

    @Test
    public void testCreateAdministrator() {
        Bank bank = new Bank();
        Administrator admin = bank.createAdministrator("Admin", "Alice", ADMIN_EMAIL, "secure_p@ssw0|2d"); // NOTE: Factory pattern
        assertEquals(ADMIN_EMAIL, bank.getAdministrators().get(0).getEmail());

        FulibTools.objectDiagrams().dumpSVG("docs/objects/test/admin_s1.svg", bank);
    }

    @Test
    public void testCreateAdministratorFailure() {
        Bank bank = new Bank();
        Administrator admin1 = bank.createAdministrator("Alice", "Brown", ADMIN_EMAIL, "secure_p@ssw0|2d");
        assertThrows(Bank.InvalidUserException.class, () -> {
            Administrator admin2 = bank.createAdministrator("Alice", "Peterson", ADMIN_EMAIL, "I-forgot");
        });

        FulibTools.objectDiagrams().dumpSVG("docs/objects/test/admin_s2.svg", bank);
    }

    @Test
    public void testCreateCustomer() {
        Bank bank = new Bank();
        Double initial_balance = 100.0;
        // NOTE: Factory pattern
        // bank.createCustomer creates a customer and an account, if balance or currency aren't provided,
        // default values are chosen then
        Customer customer = bank.createCustomer("John", "Doe", "john@doe.ee", "pass1234", initial_balance, Currency.EUR);
        assertEquals("john@doe.ee", bank.getCustomersByEmail("john@doe.ee").getEmail());

        FulibTools.objectDiagrams().dumpSVG("docs/objects/test/admin_s3.svg", bank);
    }
}
