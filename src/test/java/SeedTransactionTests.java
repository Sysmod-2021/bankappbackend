import org.fulib.FulibTools;
import org.junit.Test;

import model.*;
import model.Currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

// FR #4. Seed transaction creation
//
// Full Bank specification is available at
// https://docs.google.com/document/d/1AjevAXSdgcHC6yfwMc1QbsvFRuz3pfRowHIOu4XMXEU/edit#

public class SeedTransactionTests {
    static final String TRANS_DESC = "Seed transaction";
    static final String REJECTION_DESC = "Destination account is invalid";

    //  Scenario 1: Successful seed transaction
    //
    //  Given         an administrator is logged in
    //  When     the administrator creates a new seed transaction
    //  And         enters a valid receiving account and amount
    //  Then        the corresponding account receives the amount
    //  And         the transaction is traced
    @Test
    public void testCreateSeedTransaction() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AdministratorExistsException, Bank.AccountDoesNotExistException {
        Bank bank = new Bank();
        Administrator admin = bank.createAdministrator("Admin", "ALice", "admin@bank.ee", "secure_p@ssw0|2d");
        Customer customer = bank.createCustomer("Bob", "Jackson", "bobby@tt.ee", "secret", 0.0, Currency.EUR);
        Transaction transfer = admin.createSeedTransactionToCustomer(customer.getAccount().getId(), 30.0, Currency.EUR, TRANS_DESC);
        assertEquals(30.0, customer.getAccount().getBalance(), 0.0);

        FulibTools.objectDiagrams().dumpSVG("docs/objects/seed_transfer_1.svg", transfer);
    }

    //  Scenario 2: Unsuccessful seed transaction
    //
    //  Given         an administrator is logged in
    //  When     the administrator creates a new seed transaction
    //  And         enters an invalid receiving account
    //  Then        the transaction is REVOKED
    //  And         nothing changed
    @Test
    public void testCreateSeedTransactionFailure() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AdministratorExistsException, Bank.AccountDoesNotExistException {
        Bank bank = new Bank();
        Administrator admin = bank.createAdministrator("Admin", "Alice", "admin@bank.ee", "secure_p@ssw0|2d");

        Bank otherBank = new Bank();
        Customer customer = otherBank.createCustomer("Bob", "Jackson", "bobby@tt.ee", "secret", 0.0, Currency.EUR);

        assertThrows(Bank.AccountDoesNotExistException.class, () -> {
            Transaction transfer = admin.createSeedTransactionToCustomer(customer.getAccount().getId(), 30.0, Currency.EUR, TRANS_DESC);
        });
        assertEquals(0.0, customer.getAccount().getBalance(), 0.0);

        FulibTools.objectDiagrams().dumpSVG("docs/objects/seed_transfer_2_bank_1.svg", bank);
        FulibTools.objectDiagrams().dumpSVG("docs/objects/seed_transfer_2_bank_2.svg", otherBank);
    }
}
