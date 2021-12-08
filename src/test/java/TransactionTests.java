import org.junit.Test;

import model.*;
import model.Currency;
import model.Transaction.Status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

    //	Scenario 1: model.Customer A initiates money transfer to a friend (model.Customer B)
    //	Given model.Customer A is registered in the system
    //	And his user account is approved
    //	And a bank account has been assigned to his user accounts
    //	And model.Customer A is logged in
    //	Then model.Customer A can create a new transaction
    //	And can specify the beneficiary
    //	And can specify the amount of money to be transferred from model.Customer A to model.Customer B
    //	And can specify the description of the transaction
    //	And can confirm the transaction
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

        // [!] model.Currency as Enum in model.Transaction, mismatch type with java.util.model.Currency in model.Account

        FulibTools.objectDiagrams().dumpSVG("docs/objects/transaction_1.svg", transfer);
    }


    //	Scenario 2: model.Customer A transfers money to a friend (model.Customer B) successfully
    //	Given model.Customer A is registered and has a bank account
    //	And he created a transaction and confirmed it
    //	And model.Customer B is registered and has a bank account
    //	Then the transaction is executed
    //	And money has been charged from the model.Customer's A bank account
    //	And the model.Customer' B bank account balanced has been increased accordingly
    //	And the model.Customer B now can see the updated balance on his profile page
    @Test
    public void shouldTransferMoneySuccessfully() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AccountDoesNotExistException, Bank.TransactionRestrictionException {
        Bank bank = new Bank();
        Double balance = 100.0;
        Double amount = 25.0;

        Customer customer = bank.createCustomer("johntest", "Doe", "johntest@doe.ee", "pass1234", balance, Currency.EUR);
        Customer beneficiary = bank.createCustomer("Bob", "Jackson", "bobbytest@tt.ee", "secret", 0.0, Currency.EUR);

        Transaction transfer = bank.createTransaction(customer.getAccount(), beneficiary.getAccount(), Currency.EUR, amount, TRANS_DESC);
        transfer.execute();

        assertEquals(75.0, customer.getAccount().getBalance(), 0.0);
        assertEquals(25.0, beneficiary.getAccount().getBalance(), 0.0);

        FulibTools.objectDiagrams().dumpSVG("docs/objects/transaction_2.svg", transfer);
    }

    //	Scenario 3: model.Customer A transfers money to a friend (model.Customer B) unsuccessfully
    //	Given model.Customer A is registered and has a bank account
    //	And he created a transaction
    //	And model.Customer B has not been found in the bank system
    //	Then the transaction is rejected
    //	And model.Customer A can see the status of the transaction on a page with transactions in his user profile
    @Test
    public void shouldTransferMoneyUnsuccessfully_WhenBeneficiaryNotExists() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AccountDoesNotExistException {
        Bank bank = new Bank();
        Bank internationalBank = new Bank();
        Double balance = 100.0;
        Double amount = 25.0;

        Customer customer = bank.createCustomer("johntest", "Doe", "johntest@doe.ee", "pass1234", balance, Currency.EUR);
        Customer beneficiary = internationalBank.createCustomer("Bob", "Jackson", "bobbytest@tt.ee", "secret", 0.0, Currency.EUR);

        assertThrows(Bank.AccountDoesNotExistException.class, () -> {
            Transaction transfer = new Transaction(bank, customer.getAccount(), beneficiary.getAccount(), Currency.EUR, amount, TRANS_DESC);
            transfer.execute();
        });

        FulibTools.objectDiagrams().dumpSVG("docs/objects/transaction_3.svg", bank, internationalBank);
    }

    //	Scenario 4: A bank administrator charges a customer some fee
    //	Given the bank admin has an admin account
    //	And he is logged in
    //	And the customer, who will be charged with a fee, exists in the system
    //	And he has a bank account assigned
    //	Then the bank admin can create a transaction to charge the customer's bank account
    //	And can specify an amount to charge
    //	And description of the transaction
    @Test
    public void shouldChargeCustomerFeeSuccessfully() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AccountDoesNotExistException, Bank.TransactionRestrictionException {
        Bank bank = new Bank();
        Double bankBalance = bank.getBankAccount().getBalance();

        Double balance = 100.0;
        Double fee = 10.0;

        Customer customer = bank.createCustomer("johntest", "Doe", "johntest@doe.ee", "pass1234", balance, Currency.EUR);
        Transaction transfer = bank.createTransaction(customer.getAccount(), bank.getBankAccount(), Currency.EUR, fee, "Fee");
        transfer.execute();

        assertEquals("", transfer.getRejectionDescription());
        assertEquals(bankBalance + fee, bank.getBankAccount().getBalance(), 0.0);
        assertEquals(90.0, customer.getAccount().getBalance(), 0.0);

        FulibTools.objectDiagrams().dumpSVG("docs/objects/transaction_4.svg", transfer);
    }

    // FR #2. Scenario 5
    @Test
    public void shouldChargeCustomerFeeSuccessfully_WhenBalanceNotEnough() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AccountDoesNotExistException, Bank.TransactionRestrictionException {
        Bank bank = new Bank();
        Account bankAccount = bank.getBankAccount();
        Double bankBalance = bankAccount.getBalance();

        Double balance = 10.0;
        Double fee = 50.0;

        Customer customer = bank.createCustomer("johntest", "Doe", "johntest@doe.ee", "pass1234", balance, Currency.EUR);
        Transaction transfer = bank.createTransaction(customer.getAccount(), bankAccount, Currency.EUR, fee, "Expensive fee");
        transfer.execute();

        assertEquals("Not enough money on the source account", transfer.getRejectionDescription());
        assertEquals(bankBalance, bankAccount.getBalance(), 0.0);
        assertEquals(balance, customer.getAccount().getBalance(), 0.0);

        FulibTools.objectDiagrams().dumpSVG("docs/objects/transaction_5.svg", transfer);
    }

    //	Scenario 6: A bank administrator revokes a transaction
    //	Given an existing and logged in customer has created a transaction
    //	And the transaction has been successfully executed
    //	And it appeared that the transaction is incorrect
    //	Then the bank admin revokes the transaction
    //	And money are withdrawn from the receiver's bank account
    //	And money are returned to the sender
    //	And an additional trace about revoked account is created
   @Test
   public void shouldRevokeTransactionSuccessfully() throws Exception {
        // Arrange
        Bank bank = new Bank();
        Administrator admin = bank.createAdministrator("Admin", "Alice", "admin@bank.ee", "secure_p@ssw0|2d");

        Double balanceOfCustomer = 200.0;
        Double balanceOfBeneficiary = 300.0;
        Double amount = 50.0;
        String administratorId = admin.getId();

        Customer customer = bank.createCustomer("johntest", "Doe", "johntest@doe.ee", "pass1234", balanceOfCustomer, Currency.EUR);
        Customer beneficiary = bank.createCustomer("Bob", "Jackson", "bobbytest@tt.ee", "secret", balanceOfBeneficiary, Currency.EUR);
        Transaction transaction = bank.getAdministrator(administratorId).createTransactionTwoCustomers(
            customer.getAccount().getId(),
            beneficiary.getAccount().getId(),
            amount, TRANS_DESC);
        
        // Act
        bank.getAdministrator(administratorId).revokeTransaction(transaction.getId(), REVOKE_DESC);

        // Assert
        assertEquals(balanceOfCustomer, customer.getAccount().getBalance(), 0.0);
        assertEquals(balanceOfBeneficiary, beneficiary.getAccount().getBalance(), 0.0);

        List<Trace> traces = bank.getTraces();
        assertEquals(2, traces.size());
        // TODO: add detailed asserts for verifying the traces correctness

        FulibTools.objectDiagrams().dumpSVG("docs/objects/transaction_6.svg", bank);
   }
   
   @Test
   public void shouldDenyTransactionRevocation_alreadyRevoked() throws Exception {
        // Arrange
        Bank bank = new Bank();
        Administrator admin = bank.createAdministrator("Admin", "Alice", "admin@bank.ee", "secure_p@ssw0|2d");

        Double balanceOfCustomer = 200.0;
        Double balanceOfBeneficiary = 300.0;
        Double amount = 50.0;
        String administratorId = admin.getId();

        Customer customer = bank.createCustomer("johntest", "Doe", "johntest@doe.ee", "pass1234", balanceOfCustomer, Currency.EUR);
        Customer beneficiary = bank.createCustomer("Bob", "Jackson", "bobbytest@tt.ee", "secret", balanceOfBeneficiary, Currency.EUR);
        Transaction transaction = bank.getAdministrator(administratorId).createTransactionTwoCustomers(
            customer.getAccount().getId(),
            beneficiary.getAccount().getId(),
            amount, TRANS_DESC);
        
        String transactionId = transaction.getId();
        
        // Act
        bank.getAdministrator(administratorId).revokeTransaction(transactionId, REVOKE_DESC);

        // Assert
        assertEquals(balanceOfCustomer, customer.getAccount().getBalance(), 0.0);
        assertEquals(balanceOfBeneficiary, beneficiary.getAccount().getBalance(), 0.0);

        // Execute revocation again, must throw exception
        assertEquals(transaction.getStatus(), Status.REVOKED);
        Throwable transactionAlreadyRevoked = assertThrows(Bank.TransactionCanNotBeRevoked.class, () -> {
        	bank.getAdministrator(administratorId).revokeTransaction(transactionId, "Test if transaction has been revoked");
        });

        assertEquals("Denied to revoke, transaction status is not EXECUTED", transactionAlreadyRevoked.getMessage());

        FulibTools.objectDiagrams().dumpSVG("docs/objects/transaction_7.svg", bank);
   }

   @Test
   public void testUnsuccessfulTransaction_differentCurrencies() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AccountDoesNotExistException, Bank.TransactionRestrictionException {
	    Bank bank = new Bank();
	    Double balance = 100.0;
	    Double amount = 25.0;
	
	    Customer customer = bank.createCustomer("johntest", "Doe", "johntest@doe.ee", "pass1234", balance, Currency.EUR);
	    Customer beneficiary = bank.createCustomer("Bob", "Jackson", "bobbytest@tt.ee", "secret", 0.0, Currency.USD);
	
	    Transaction transfer = bank.createTransaction(customer.getAccount(), beneficiary.getAccount(), Currency.EUR, amount, TRANS_DESC);
	    transfer.execute();
	
	    assertFalse(customer.getAccount().getCurrency().equals(beneficiary.getAccount().getCurrency()));
	    assertEquals(transfer.getStatus(), Status.ABORTED);
	    assertEquals(transfer.getRejectionDescription(), "Currency mismatch detected");
	
	    FulibTools.objectDiagrams().dumpSVG("docs/objects/transaction_8.svg", transfer);
   }
}
