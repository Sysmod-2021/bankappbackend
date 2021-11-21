import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TransactionTests {
	
	static final String BENEFICIARY_NAME = "Bob Jackson";
	static final String TRANS_DESC = "Ref: concert ticket";
	static final String REJECTION_DESC = "Destination account is invalid";
	
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
		Account sourceBankAccount  = new Account(bank, customer, eur, balance);
		Customer beneficiary = new Customer(bank, "Bob", "Jackson", "bobby@tt.ee", "secret", "400000000002");
		Account destinationBankAccount  = new Account(bank, beneficiary, eur, 0f);

		Transaction transfer = new Transaction(bank, sourceBankAccount, destinationBankAccount, Currency.EUR,  amount, TRANS_DESC);
		
		var beneficiaryAcc = transfer.getDestination().getOwner();
		assertEquals(BENEFICIARY_NAME, beneficiaryAcc.getFirstName() + " " + beneficiaryAcc.getLastName());
		assertEquals(25f, transfer.getAmount(), 0f);
		assertEquals(TRANS_DESC, transfer.getDescription());
		// assertEquals("", ""); can confirm transaction
		
		
		// FAIL 
		// - login not implemented
		// - Transaction confirm() not implemented
		
		// [!] Currency as Enum in Transaction, mismatch type with java.util.Currency in Account
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
		Account sourceBankAccount  = new Account(bank, customer, eur, balance);
		Customer beneficiary = new Customer(bank, "Bob", "Jackson", "bobby@tt.ee", "secret", "400000000002");
		Account destinationBankAccount  = new Account(bank, beneficiary, eur, 0f);

		Transaction transfer = new Transaction(bank, sourceBankAccount, destinationBankAccount, Currency.EUR,  amount, TRANS_DESC);
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
		Account sourceBankAccount  = new Account(bank, customer, eur, balance);
		Customer beneficiary = new Customer(internationalBank, "Bob", "Jackson", "bobby@tt.ee", "secret", "400000000002");
		Account destinationBankAccount  = new Account(bank, beneficiary, eur, 0f);

		Transaction transfer = new Transaction(bank, sourceBankAccount, destinationBankAccount, Currency.EUR,  amount, TRANS_DESC);
		transfer.execute();
		
		assertEquals(REJECTION_DESC, transfer.getRejectionDescription());
		// assertEquals("REJECTED", ""); transaction's status on his profile page
		
		// FAIL 
		// - Bank accounts not add to the Bank
		// - Transfer fail; sender = null
		// - profile page not implemented
		
		// [!] Currency as Enum in Transaction, mismatch type with java.util.Currency in Account
		// [!] Transfer cannot make between different bank?
	}
	
	
//	Scenario 4: A bank administrator charges a customer some fee
//	Given the bank admin has an admin account
//	And he is logged in
//	And the customer, who will be charged with a fee, exists in the system
//	And he has a bank account assigned
//	Then the bank admin can create a transaction to charge the customer's bank account
//	And can specify an amount to charge
//	And description of the transaction

//	Scenario 5: A bank administrator charges a customer some fee which is bigger than the customer's balance
//	Given the bank admin has an admin account
//	And he is logged in
//	And he has charged the customer some amount
//	And there was not enough money on the customer's account
//	Then the customer's account is charged anyway
//	And the customer's balance then is negative

//	Scenario 6: A bank administrator revokes a transaction
//	Given an existing and logged in customer has created a transaction
//	And the transaction has been successfully executed
//	And it appeared that the transaction is incorrect
//	Then the bank admin revokes the transaction
//	And money are withdrawn from the receiver's bank account
//	And money are returned to the sender
//	And an additional trace about revoked account is created

//	Scenario 7: Seed transaction is created by a bank administrator
//	Given a bank admin exists and logged in
//	And there is at least one customer in a system
//	And he has an assigned balance account
//	Then the bank admin can create a seed transaction
//	And specifies the amount of money to be generated
//	And specifies the recipient of the sum
//	And specifies a description of the transaction
}

	


