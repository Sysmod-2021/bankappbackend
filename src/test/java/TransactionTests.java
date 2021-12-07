import org.junit.Test;

import model.*;
import model.Currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.*;

import org.fulib.FulibTools;

// Full model.Bank specification is available at
// https://docs.google.com/document/d/1AjevAXSdgcHC6yfwMc1QbsvFRuz3pfRowHIOu4XMXEU/edit#

public class TransactionTests {
    static final String BENEFICIARY_NAME = "Bob Jackson";
    static final String TRANS_DESC = "Ref: concert ticket";
    static final String REJECTION_DESC = "Destination account is invalid";
    static final String REVOKE_DESC = "Made a mistake in the sent amount";

    // FR #2. model.Transaction creation
    @Test
    public void testCreateTransaction() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AdministratorExistsException, Bank.AccountDoesNotExistException, Bank.TransactionRestrictionException {
        Bank bank = new Bank();
        bank.createAdministrator("Admin", "ALice", "admin@bank.ee", "secure_p@ssw0|2d");
        Customer customer1 = bank.createCustomer("johntest", "Doe", "johntest@doe.ee", "pass1234", 100.0, Currency.EUR);
        Customer customer2 = bank.createCustomer("Bob", "Jackson", "bobbytest@tt.ee", "secret", 0.0, Currency.EUR);

        Transaction transfer = bank.createTransaction(customer1.getAccount(), customer2.getAccount(), Currency.EUR, 25.5, TRANS_DESC);
        transfer.execute();

        assertEquals(customer2.getAccount().getBalance(), 25.5, 0.0);

        FulibTools.objectDiagrams().dumpSVG("docs/objects/tranfermoney_objects.svg", transfer);
    }

    // initiate by bank
    @Test
    public void shouldInitiateMoneyTransferSuccessfully() throws Bank.CustomerExistsException, Bank.AccountExistsException {
        Bank bank = new Bank();
        Double balance = 100.0;
        Double amount = 25.0;
        Customer customer = bank.createCustomer("johntest", "Doe", "johntest@doe.ee", "pass1234", balance, Currency.USD);
        Customer beneficiary = bank.createCustomer("Bob", "Jackson", "bobbytest@tt.ee", "secret", 0.0, Currency.USD);

        Transaction transfer = bank.createTransaction(customer.getAccount(), beneficiary.getAccount(), Currency.USD, amount, TRANS_DESC);

        Customer beneficiaryAcc = transfer.getDestination().getOwner();
        assertEquals(BENEFICIARY_NAME, beneficiaryAcc.getFirstName() + " " + beneficiaryAcc.getLastName());
        assertEquals(25f, transfer.getAmount(), 0f);
        assertEquals(TRANS_DESC, transfer.getDescription());
        // assertEquals("", ""); can confirm transaction


        // FAIL
        // - login not implemented
        // - model.Transaction confirm() not implemented

        FulibTools.objectDiagrams().dumpSVG("docs/objects/transaction_1.svg", transfer);
    }


    // Scenario 1: Successful - 2 valid users
    @Test
    public void shouldTransferMoneySuccessfully() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AccountDoesNotExistException, Bank.TransactionRestrictionException, Bank.AdministratorExistsException {
        Bank bank = new Bank();
        Administrator admin = bank.createAdministrator("Admin", "ALice", "admin@bank.ee", "secure_p@ssw0|2d");
        Double balance = 100.0;
        Double amount = 25.0;
        Customer customer = bank.createCustomer("johntest", "Doe", "johntest@doe.ee", "pass1234", balance, Currency.EUR);
        Customer beneficiary = bank.createCustomer("Bob", "Jackson", "bobbytest@tt.ee", "secret", 0.0, Currency.EUR);

        Transaction transfer = admin.createTransactionTwoCustomers(customer.getAccount().getId(), beneficiary.getAccount().getId(), amount, "reference");

        assertEquals(75.0, customer.getAccount().getBalance(), 0.0);
        assertEquals(25.0, beneficiary.getAccount().getBalance(), 0.0);

        FulibTools.objectDiagrams().dumpSVG("docs/objects/transaction_2.svg", transfer);
    }

    // Scenario 4: Unsuccessful - invalid account
    @Test
    public void shouldTransferMoneyUnsuccessfully_WhenBeneficiaryNotExists() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AccountDoesNotExistException, Bank.AdministratorExistsException, Bank.TransactionRestrictionException {
        Bank bank = new Bank();
        Bank internationalBank = new Bank();
        Administrator admin = bank.createAdministrator("Admin", "ALice", "admin@bank.ee", "secure_p@ssw0|2d");
        Double balance = 100.0;
        Double amount = 25.0;
        Customer customer = bank.createCustomer("johntest", "Doe", "johntest@doe.ee", "pass1234", balance, Currency.EUR);
        Customer beneficiary = internationalBank.createCustomer("Bob", "Jackson", "bobbytest@tt.ee", "secret", 0.0, Currency.EUR);

        assertThrows(Bank.AccountDoesNotExistException.class, () -> {
            Transaction transfer = admin.createTransactionTwoCustomers(customer.getAccount().getId(), beneficiary.getAccount().getId(), amount, "reference");
        });

//        assertEquals(Transaction.Status.ABORTED, transfer.getStatus());

        FulibTools.objectDiagrams().dumpSVG("docs/objects/transaction_3.svg", bank, internationalBank);
    }

    // Scenario 2: Successful - user to bank
    @Test
    public void shouldChargeCustomerFeeSuccessfully() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AccountDoesNotExistException, Bank.TransactionRestrictionException, Bank.AdministratorExistsException {
        Bank bank = new Bank();
        Administrator admin = bank.createAdministrator("Admin", "ALice", "admin@bank.ee", "secure_p@ssw0|2d");
        Double bankBalance = bank.getBankAccount().getBalance();
        Double balance = 100.0;
        Double fee = 10.0;
        Customer customer = bank.createCustomer("johntest", "Doe", "johntest@doe.ee", "pass1234", balance, Currency.EUR);

        Transaction transfer = admin.createTransactionToBank(customer.getAccount().getId(), fee, "Fee");

        assertEquals("", transfer.getRejectionDescription());
        assertEquals(bankBalance + fee, bank.getBankAccount().getBalance(), 0.0);
        assertEquals(90.0, customer.getAccount().getBalance(), 0.0);

        FulibTools.objectDiagrams().dumpSVG("docs/objects/transaction_4.svg", transfer);
    }

    // Scenario 5: insufficient funds
    @Test
    public void shouldTransferMoneyUnsuccessfully_WhenBalanceNotEnough() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AccountDoesNotExistException, Bank.TransactionRestrictionException, Bank.AdministratorExistsException {
        Bank bank = new Bank();
        Administrator admin = bank.createAdministrator("Admin", "ALice", "admin@bank.ee", "secure_p@ssw0|2d");
        Account bankAccount = bank.getBankAccount();
        Double bankBalance = bankAccount.getBalance();
        Double balance = 10.0;
        Double fee = 50.0;
        Customer customer = bank.createCustomer("johntest", "Doe", "johntest@doe.ee", "pass1234", balance, Currency.EUR);

        Transaction transfer = admin.createTransactionTwoCustomers(customer.getAccount().getId(), bankAccount.getId(), fee, "Expensive fee");

        assertEquals("Not enough money on the source account", transfer.getRejectionDescription());
        assertEquals(bankBalance, bankAccount.getBalance(), 0.0);
        assertEquals(balance, customer.getAccount().getBalance(), 0.0);
        assertEquals(Transaction.Status.ABORTED, transfer.getStatus());

        FulibTools.objectDiagrams().dumpSVG("docs/objects/transaction_5.svg", transfer);
    }

    // Successful - Revoke
   @Test
   public void shouldRevokeTransactionSuccessfully() throws Exception {
        // Arrange
        Bank bank = new Bank();
        bank.createAdministrator("Admin", "Alice", "admin@bank.ee", "secure_p@ssw0|2d");
        Double balanceOfCustomer = 200.0;
        Double balanceOfBeneficiary = 300.0;
        Double amount = 50.0;
        Customer customer = bank.createCustomer("johntest", "Doe", "johntest@doe.ee", "pass1234", balanceOfCustomer, Currency.EUR);
        Customer beneficiary = bank.createCustomer("Bob", "Jackson", "bobbytest@tt.ee", "secret", balanceOfBeneficiary, Currency.EUR);
        Transaction transaction = bank.getAdministrator().createTransactionTwoCustomers(
            customer.getAccount().getId(),
            beneficiary.getAccount().getId(),
            amount, TRANS_DESC);
        
        // Act
        bank.getAdministrator().revokeTransaction(transaction.getId(), REVOKE_DESC);

        // Assert
        assertEquals(balanceOfCustomer, customer.getAccount().getBalance(), 0.0);
        assertEquals(balanceOfBeneficiary, beneficiary.getAccount().getBalance(), 0.0);

        List<Trace> traces = bank.getTraces();
        assertEquals(2, traces.size());
        // TODO: add detailed asserts for verifying the traces correctness

        FulibTools.objectDiagrams().dumpSVG("docs/objects/transaction_6.svg", bank);
   }
}
