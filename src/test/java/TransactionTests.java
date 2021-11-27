import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.fulib.FulibTools;

// Full Bank specification is available at
// https://docs.google.com/document/d/1AjevAXSdgcHC6yfwMc1QbsvFRuz3pfRowHIOu4XMXEU/edit#

public class TransactionTests {
    static final String BENEFICIARY_NAME = "Bob Jackson";
    static final String TRANS_DESC = "Ref: concert ticket";
    static final String REJECTION_DESC = "Destination account is invalid";

    // FR #2. Transaction creation
    @Test
    public void testCreateTransaction() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AdministratorExistsException, Bank.AccountDoesNotExistException {
        Bank bank = new Bank();
        bank.createAdministrator("Admin", "ALice", "admin@bank.ee", "secure_p@ssw0|2d");
        Customer customer1 = bank.createCustomer("John", "Doe", "john@doe.ee", "pass1234", 100.0, Currency.EUR);
        Customer customer2 = bank.createCustomer("Bob", "Jackson", "bobby@tt.ee", "secret", 0.0, Currency.EUR);

        Transaction transfer = bank.createTransaction(customer1.getAccount(), customer2.getAccount(), Currency.EUR, 25.5, TRANS_DESC);
        transfer.execute();

        assertEquals(customer2.getAccount().getBalance(), 25.5, 0.0);

        FulibTools.objectDiagrams().dumpSVG("docs/objects/tranfermoney_objects.svg", transfer);
    }

    //	Scenario 1: Customer A initiates money transfer to a friend (Customer B)
    //	Given Customer A is registered in the system
    //	And his user account is approved
    //	And a bank account has been assigned to his user accounts
    //	And Customer A is logged in
    //	Then Customer A can create a new transaction
    //	And can specify the beneficiary
    //	And can specify the amount of money to be transferred from Customer A to Customer B
    //	And can specify the description of the transaction
    //	And can confirm the transaction
    @Test
    public void shouldInitiateMoneyTransferSuccessfully() throws Bank.CustomerExistsException, Bank.AccountExistsException {
        Bank bank = new Bank();
        Double balance = 100.0;
        Double amount = 25.0;

        Customer customer = bank.createCustomer("John", "Doe", "john@doe.ee", "pass1234", balance, Currency.USD);
        Customer beneficiary = bank.createCustomer("Bob", "Jackson", "bobby@tt.ee", "secret", 0.0, Currency.USD);
        Transaction transfer = bank.createTransaction(customer.getAccount(), beneficiary.getAccount(), Currency.USD, amount, TRANS_DESC);

        Customer beneficiaryAcc = transfer.getDestination().getOwner();
        assertEquals(BENEFICIARY_NAME, beneficiaryAcc.getFirstName() + " " + beneficiaryAcc.getLastName());
        assertEquals(25f, transfer.getAmount(), 0f);
        assertEquals(TRANS_DESC, transfer.getDescription());

        FulibTools.objectDiagrams().dumpSVG("docs/objects/transaction_1.svg", transfer);
    }

    //	Scenario 2: Customer A transfers money to a friend (Customer B) successfully
    //	Given Customer A is registered and has a bank account
    //	And he created a transaction and confirmed it
    //	And Customer B is registered and has a bank account
    //	Then the transaction is executed
    //	And money has been charged from the Customer's A bank account
    //	And the Customer' B bank account balanced has been increased accordingly
    //	And the Customer B now can see the updated balance on his profile page
    @Test
    public void shouldTransferMoneySuccessfully() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AccountDoesNotExistException {
        Bank bank = new Bank();
        Double balance = 100.0;
        Double amount = 25.0;

        Customer customer = bank.createCustomer("John", "Doe", "john@doe.ee", "pass1234", balance, Currency.EUR);
        Customer beneficiary = bank.createCustomer("Bob", "Jackson", "bobby@tt.ee", "secret", 0.0, Currency.EUR);

        Transaction transfer = bank.createTransaction(customer.getAccount(), beneficiary.getAccount(), Currency.EUR, amount, TRANS_DESC);
        transfer.execute();

        assertEquals(75.0, customer.getAccount().getBalance(), 0.0);
        assertEquals(25.0, beneficiary.getAccount().getBalance(), 0.0);

        FulibTools.objectDiagrams().dumpSVG("docs/objects/transaction_2.svg", transfer);
    }

    //	Scenario 3: Customer A transfers money to a friend (Customer B) unsuccessfully
    //	Given Customer A is registered and has a bank account
    //	And he created a transaction
    //	And Customer B has not been found in the bank system
    //	Then the transaction is rejected
    //	And Customer A can see the status of the transaction on a page with transactions in his user profile
    @Test
    public void shouldTransferMoneyUnsuccessfully_WhenBeneficiaryNotExists() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AccountDoesNotExistException {
        Bank bank = new Bank();
        Bank internationalBank = new Bank();
        Double balance = 100.0;
        Double amount = 25.0;

        Customer customer = bank.createCustomer("John", "Doe", "john@doe.ee", "pass1234", balance, Currency.EUR);
        Customer beneficiary = internationalBank.createCustomer("Bob", "Jackson", "bobby@tt.ee", "secret", 0.0, Currency.EUR);

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
    public void shouldChargeCustomerFeeSuccessfully() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AccountDoesNotExistException {
        Bank bank = new Bank();
        Double bankBalance = bank.getBankAccount().getBalance();

        Double balance = 100.0;
        Double fee = 10.0;

        Customer customer = bank.createCustomer("John", "Doe", "john@doe.ee", "pass1234", balance, Currency.EUR);
        Transaction transfer = bank.createTransaction(customer.getAccount(), bank.getBankAccount(), Currency.EUR, fee, "Fee");
        transfer.execute();

        assertEquals("", transfer.getRejectionDescription());
        assertEquals(bankBalance + fee, bank.getBankAccount().getBalance(), 0.0);
        assertEquals(90.0, customer.getAccount().getBalance(), 0.0);

        FulibTools.objectDiagrams().dumpSVG("docs/objects/transaction_4.svg", transfer);
    }

    // FR #2. Scenario 5
    @Test
    public void shouldChargeCustomerFeeSuccessfully_WhenBalanceNotEnough() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AccountDoesNotExistException {
        Bank bank = new Bank();
        Account bankAccount = bank.getBankAccount();
        Double bankBalance = bankAccount.getBalance();

        Double balance = 10.0;
        Double fee = 50.0;

        Customer customer = bank.createCustomer("John", "Doe", "john@doe.ee", "pass1234", balance, Currency.EUR);
        Transaction transfer = bank.createTransaction(customer.getAccount(), bankAccount, Currency.EUR, fee, "Expensive fee");
        transfer.execute();

        assertEquals("Not enough money on the source account", transfer.getRejectionDescription());
        assertEquals(bankBalance, bankAccount.getBalance(), 0.0);
        assertEquals(balance, customer.getAccount().getBalance(), 0.0);

        FulibTools.objectDiagrams().dumpSVG("docs/objects/transaction_5.svg", transfer);
    }

    // TODO: not implemented yet
    //	Scenario 6: A bank administrator revokes a transaction
    //	Given an existing and logged in customer has created a transaction
    //	And the transaction has been successfully executed
    //	And it appeared that the transaction is incorrect
    //	Then the bank admin revokes the transaction
    //	And money are withdrawn from the receiver's bank account
    //	And money are returned to the sender
    //	And an additional trace about revoked account is created
//    @Test
//    public void shouldRevokeTransactionSuccessfully() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AccountDoesNotExistException {
//        Bank bank = new Bank();
//        Double balanceOfCustomer = 200.0;
//        Double balanceOfBeneficiary = 300.0;
//        Double amount = 50.0;
//
//        Customer customer = bank.createCustomer("John", "Doe", "john@doe.ee", "pass1234", balanceOfCustomer, Currency.EUR);
//        Customer beneficiary = bank.createCustomer("Bob", "Jackson", "bobby@tt.ee", "secret", balanceOfBeneficiary, Currency.EUR);
//        Transaction transfer = bank.createTransaction(customer.getAccount(), beneficiary.getAccount(), Currency.EUR, amount, TRANS_DESC);
//        transfer.execute();
//
//        // transferMade.Revoke(); <- Some method like this
//
//        // Assert
//        assertEquals(balanceOfCustomer, customer.getAccount().getBalance(), 0.0);
//        assertEquals(balanceOfBeneficiary, beneficiary.getAccount().getBalance(), 0.0);
//        // trace
//
//        // FAIL
//        // - login not implemented
//        // - Bank accounts not add to the Bank
//        // - Transfer fail; sender = null
//        // - rovoke not implemented
//        // - trace is not create automatically
//
//        // [!] There is no distinguish between transaction by admin or by customer, I don't even need to put admin here
//        // [!] Currency as Enum in Transaction, mismatch type with java.util.Currency in Account
//
//        FulibTools.objectDiagrams().dumpSVG("docs/objects/transaction_6.svg", bank);
//    }

    //	Scenario 7: Seed transaction is created by a bank administrator
    //	Given a bank admin exists and logged in
    //	And there is at least one customer in a system
    //	And he has an assigned balance account
    //	Then the bank admin can create a seed transaction
    //	And specifies the amount of money to be generated
    //	And specifies the recipient of the sum
    //	And specifies a description of the transaction
    @Test
    public void shouldCreateSeedTransactionSuccessfully() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AccountDoesNotExistException {
        Bank bank = new Bank();
        Double balance = 10.0;
        Double amount = 250.0;

        Customer customer = bank.createCustomer("John", "Doe", "john@doe.ee", "pass1234", balance, Currency.EUR);
        Transaction deposit = bank.createTransaction(bank.getBankAccount(), customer.getAccount(), Currency.EUR, amount, "Salary");
        deposit.execute();

        assertEquals("", deposit.getRejectionDescription());
        assertEquals(260.0, customer.getAccount().getBalance(), 0.0);

        FulibTools.objectDiagrams().dumpSVG("docs/objects/transaction_7.svg", deposit);
    }
}

	


