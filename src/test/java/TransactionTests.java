import org.junit.Test;

import static org.junit.Assert.assertEquals;

import org.fulib.FulibTools;

// Full Bank specification is available at
// https://docs.google.com/document/d/1AjevAXSdgcHC6yfwMc1QbsvFRuz3pfRowHIOu4XMXEU/edit#

public class TransactionTests {
    static final String BENEFICIARY_NAME = "Bob Jackson";
    static final String TRANS_DESC = "Ref: concert ticket";
    static final String REJECTION_DESC = "Destination account is invalid";

    // FR #2. Transaction creation
    @Test
    public void testCreateTransaction() {
        Bank bank = new Bank();
        bank.createAdministrator("Admin", "ALice", "admin@bank.ee", "secure_p@ssw0|2d");
        Customer customer1 = bank.createCustomer("John", "Doe", "john@doe.ee", "pass1234", "400000000001", 100.0, Currency.EUR);
        Customer customer2 = bank.createCustomer("Bob", "Jackson", "bobby@tt.ee", "secret", "400000000002", 0.0, Currency.EUR);

        transfer = bank.createTransaction(customer1, customer2, 25.5, Currency.EUR, TRANS_DESC);
        transfer.execute();

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
    public void shouldInitiateMoneyTransferSuccessfully() {
        Bank bank = new Bank();
        Float balance = 100f;
        Float amount = 25f;
        java.util.Currency eur = java.util.Currency.getInstance("EUR");

        Customer customer = new Customer(bank, "John", "Doe", "john@doe.ee", "pass1234", "400000000001");
        Account sourceBankAccount = new Account(bank, customer, eur, balance);
        Customer beneficiary = new Customer(bank, "Bob", "Jackson", "bobby@tt.ee", "secret", "400000000002");
        Account destinationBankAccount = new Account(bank, beneficiary, eur, 0f);

        Transaction transfer = new Transaction(bank, sourceBankAccount, destinationBankAccount, Currency.EUR, amount, TRANS_DESC);

        var beneficiaryAcc = transfer.getDestination().getOwner();
        assertEquals(BENEFICIARY_NAME, beneficiaryAcc.getFirstName() + " " + beneficiaryAcc.getLastName());
        assertEquals(25f, transfer.getAmount(), 0f);
        assertEquals(TRANS_DESC, transfer.getDescription());
        // assertEquals("", ""); can confirm transaction


        // FAIL
        // - login not implemented
        // - Transaction confirm() not implemented

        // [!] Currency as Enum in Transaction, mismatch type with java.util.Currency in Account

        FulibTools.objectDiagrams().dumpSVG("docs/objects/test/trans_s1.svg", transfer);
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
    public void shouldTransferMoneySuccessfully() {
        Bank bank = new Bank();
        Float balance = 100f;
        Float amount = 25f;
        java.util.Currency eur = java.util.Currency.getInstance("EUR");

        Customer customer = new Customer(bank, "John", "Doe", "john@doe.ee", "pass1234", "400000000001");
        Account sourceBankAccount = new Account(bank, customer, eur, balance);
        Customer beneficiary = new Customer(bank, "Bob", "Jackson", "bobby@tt.ee", "secret", "400000000002");
        Account destinationBankAccount = new Account(bank, beneficiary, eur, 0f);

        Transaction transfer = new Transaction(bank, sourceBankAccount, destinationBankAccount, Currency.EUR, amount, TRANS_DESC);
        transfer.execute();

        assertEquals(75f, sourceBankAccount.getBalance(), 0f);
        assertEquals(25f, destinationBankAccount.getBalance(), 0f);
        // assertEquals("", ""); balance on his profile page

        // FAIL
        // - login not implemented
        // - Bank accounts not add to the Bank
        // - Transfer fail; sender = null
        // - profile page not implemented

        // [!] Currency as Enum in Transaction, mismatch type with java.util.Currency in Account

        FulibTools.objectDiagrams().dumpSVG("docs/objects/test/trans_s2.svg", transfer);
    }


    //	Scenario 3: Customer A transfers money to a friend (Customer B) unsuccessfully
    //	Given Customer A is registered and has a bank account
    //	And he created a transaction
    //	And Customer B has not been found in the bank system
    //	Then the transaction is rejected
    //	And Customer A can see the status of the transaction on a page with transactions in his user profile
    @Test
    public void shouldTransferMoneyUnsuccessfully_WhenBeneficiaryNotExists() {
        Bank bank = new Bank();
        Bank internationalBank = new Bank();
        Float balance = 100f;
        Float amount = 25f;
        java.util.Currency eur = java.util.Currency.getInstance("EUR");

        Customer customer = new Customer(bank, "John", "Doe", "john@doe.ee", "pass1234", "400000000001");
        Account sourceBankAccount = new Account(bank, customer, eur, balance);
        Customer beneficiary = new Customer(internationalBank, "Bob", "Jackson", "bobby@tt.ee", "secret", "400000000002");
        Account destinationBankAccount = new Account(bank, beneficiary, eur, 0f);

        Transaction transfer = new Transaction(bank, sourceBankAccount, destinationBankAccount, Currency.EUR, amount, TRANS_DESC);
        transfer.execute();

        assertEquals(REJECTION_DESC, transfer.getRejectionDescription());
        // assertEquals("REJECTED", ""); transaction's status on his profile page

        // FAIL
        // - Bank accounts not add to the Bank
        // - Transfer fail; sender = null
        // - profile page not implemented

        // [!] Currency as Enum in Transaction, mismatch type with java.util.Currency in Account
        // [!] Transfer cannot make between different bank?

        FulibTools.objectDiagrams().dumpSVG("docs/objects/test/trans_s3.svg", transfer);
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
    public void shouldChargeCustomerFeeSuccessfully() {
        Bank bank = new Bank();
        Account bankAccount = bank.getBankAccount();
        Float bankBalance = bankAccount.getBalance();

        Float balance = 100f;
        Float fee = 10f;
        java.util.Currency eur = java.util.Currency.getInstance("EUR");

        Customer customer = new Customer(bank, "John", "Doe", "john@doe.ee", "pass1234", "400000000001");
        Account customerBankAccount = new Account(bank, customer, eur, balance);


        Transaction transfer = new Transaction(bank, customerBankAccount, bankAccount, Currency.EUR, fee, "Fee");
        transfer.execute();

        assertEquals("", transfer.getRejectionDescription());
        assertEquals(bankBalance + fee, bankAccount.getBalance(), 0f);
        assertEquals(90f, customerBankAccount.getBalance(), 0f);

        // FAIL
        // - login not implemented
        // - Bank accounts not add to the Bank
        // - Transfer fail; sender = null

        // [!] There is no distinguish between transaction by admin or by customer, I don't even need to put admin here
        // [!] Currency as Enum in Transaction, mismatch type with java.util.Currency in Account

        FulibTools.objectDiagrams().dumpSVG("docs/objects/test/trans_s4.svg", transfer);
    }


//	Scenario 5: A bank administrator charges a customer some fee which is bigger than the customer's balance
//	Given the bank admin has an admin account
//	And he is logged in
//	And he has charged the customer some amount
//	And there was not enough money on the customer's account
//	Then the customer's account is charged anyway
//	And the customer's balance then is negative

    @Test
    public void shouldChargeCustomerFeeSuccessfully_WhenBalanceNotEnough() {
        Bank bank = new Bank();
        Account bankAccount = bank.getBankAccount();
        Float bankBalance = bankAccount.getBalance();

        Float balance = 10f;
        Float fee = 50f;
        java.util.Currency eur = java.util.Currency.getInstance("EUR");

        Customer customer = new Customer(bank, "John", "Doe", "john@doe.ee", "pass1234", "400000000001");
        Account customerBankAccount = new Account(bank, customer, eur, balance);

        Transaction transfer = new Transaction(bank, customerBankAccount, bankAccount, Currency.EUR, fee, "Expensive fee");
        transfer.execute();

        assertEquals("", transfer.getRejectionDescription());
        assertEquals(bankBalance + fee, bankAccount.getBalance(), 0f);
        assertEquals(-40f, customerBankAccount.getBalance(), 0f);

        // FAIL
        // - login not implemented
        // - Bank accounts not add to the Bank
        // - Transfer fail; sender = null

        // [!] There is no distinguish between transaction by admin or by customer, I don't even need to put admin here
        // [!] Currency as Enum in Transaction, mismatch type with java.util.Currency in Account

        FulibTools.objectDiagrams().dumpSVG("docs/objects/test/trans_s5.svg", transfer);
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
    public void shouldRevokeTransactionSuccessfully() {

        // Arrange
        Bank bank = new Bank();

        Float balanceOfCustomer = 200f;
        Float balanceOfBeneficiary = 300f;
        Float amount = 50f;
        java.util.Currency eur = java.util.Currency.getInstance("EUR");

        Customer customer = new Customer(bank, "John", "Doe", "john@doe.ee", "pass1234", "400000000001");
        Account sourceBankAccount = new Account(bank, customer, eur, balanceOfCustomer);
        Customer beneficiary = new Customer(bank, "Bob", "Jackson", "bobby@tt.ee", "secret", "400000000002");
        Account destinationBankAccount = new Account(bank, beneficiary, eur, balanceOfBeneficiary);

        Transaction transferMade = new Transaction(bank, sourceBankAccount, destinationBankAccount, Currency.EUR, amount, TRANS_DESC);
        transferMade.execute();

        // Act

        // transferMade.Revoke(); <- Some method like this


        // Assert
        assertEquals(balanceOfCustomer, sourceBankAccount.getBalance(), 0f);
        assertEquals(balanceOfBeneficiary, destinationBankAccount.getBalance(), 0f);
        // trace

        // FAIL
        // - login not implemented
        // - Bank accounts not add to the Bank
        // - Transfer fail; sender = null
        // - rovoke not implemented
        // - trace is not create automatically

        // [!] There is no distinguish between transaction by admin or by customer, I don't even need to put admin here
        // [!] Currency as Enum in Transaction, mismatch type with java.util.Currency in Account

        FulibTools.objectDiagrams().dumpSVG("docs/objects/test/trans_s6.svg", bank);
    }


//	Scenario 7: Seed transaction is created by a bank administrator
//	Given a bank admin exists and logged in
//	And there is at least one customer in a system
//	And he has an assigned balance account
//	Then the bank admin can create a seed transaction
//	And specifies the amount of money to be generated
//	And specifies the recipient of the sum
//	And specifies a description of the transaction

    @Test
    public void shouldCreateSeedTransactionSuccessfully() {
        Bank bank = new Bank();

        Float balance = 10f;
        Float amount = 250f;
        java.util.Currency eur = java.util.Currency.getInstance("EUR");

        Customer customer = new Customer(bank, "John", "Doe", "john@doe.ee", "pass1234", "400000000001");
        Account customerBankAccount = new Account(bank, customer, eur, balance);

        Transaction deposit = new Transaction(bank, null, customerBankAccount, Currency.EUR, amount, "Salary");
        deposit.execute();

        assertEquals("", deposit.getRejectionDescription());
        assertEquals(260f, customerBankAccount.getBalance(), 0f);

        // FAIL
        // - login not implemented
        // - Bank accounts not add to the Bank
        // - Transfer fail; sender = null

        // [!] There is no distinguish between transaction by admin or by customer, I don't even need to put admin here
        // [!] Currency as Enum in Transaction, mismatch type with java.util.Currency in Account

        FulibTools.objectDiagrams().dumpSVG("docs/objects/test/trans_s7.svg", deposit);
    }
}

	


