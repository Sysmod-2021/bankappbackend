import org.fulib.FulibTools;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

// FR #11 Transaction restriction by admin
//
// Full Bank specification is available at
// https://docs.google.com/document/d/1AjevAXSdgcHC6yfwMc1QbsvFRuz3pfRowHIOu4XMXEU/edit#

public class TransactionRestrictionTests {
    static final String TRANS_DESC = "Seed transaction";
    static final String REJECTION_DESC = "Destination account is invalid";

    //  Scenario 1: Successful transaction restriction
    //
    //  Given   an administrator is logged in
    //  And     an account needs to be frozen
    //  When    the administrator updates the account status (FROZEN)
    //  Then    the account cannot perform transactions anymore

    @Test
    public void testRestriction() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AdministratorExistsException, Bank.AccountDoesNotExistException {
        // Init data
        Bank bank = new Bank();
        Administrator admin = bank.createAdministrator("Admin", "ALice", "admin@bank.ee", "secure_p@ssw0|2d");
        Customer customer = bank.createCustomer("Bob", "Jackson", "bobby@tt.ee", "secret", 0.0, Currency.EUR);
        Account account = customer.getAccount();
        String accountId = account.getId();

        // Freeze account
        admin.setAccountStatus(accountId, "FROZEN");
        assertEquals("FROZEN", customer.getAccount().getStatus());

        FulibTools.objectDiagrams().dumpSVG("docs/objects/transaction_restriction_1.svg", account);
    }

    //  Scenario 2: Successful transaction un-restriction
    //
    //  Given   an administrator is logged in
    //  And     an account needs to be unfrozen
    //  When    the administrator updates the account status (ACTIVE)
    //  Then    the account can perform transactions again

    @Test
    public void testUndoRestriction() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AdministratorExistsException, Bank.AccountDoesNotExistException {
        // Init data
        Bank bank = new Bank();
        Administrator admin = bank.createAdministrator("Admin", "ALice", "admin@bank.ee", "secure_p@ssw0|2d");
        Customer customer = bank.createCustomer("Bob", "Jackson", "bobby@tt.ee", "secret", 0.0, Currency.EUR);
        Account account = customer.getAccount();
        String accountId = account.getId();

        // Activate account
        admin.setAccountStatus(accountId, "ACTIVE");
        assertEquals("ACTIVE", customer.getAccount().getStatus());

        assertThrows(Bank.AccountDoesNotExistException.class, () -> {
            Transaction transfer = admin.createSeedTransactionToCustomer(customer.getAccount().getId(), 30.0, Currency.EUR, TRANS_DESC);
        });
        assertEquals(0.0, customer.getAccount().getBalance(), 0.0);

        FulibTools.objectDiagrams().dumpSVG("docs/objects/transaction_restriction_2.svg", account);
    }
}
