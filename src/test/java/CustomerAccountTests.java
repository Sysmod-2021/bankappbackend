import model.Account;
import model.Bank;
import model.Currency;
import model.Customer;
import org.fulib.FulibTools;
import org.junit.Test;

import model.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CustomerAccountTests {

	//	Scenario 1: A bank account is assigned to a customer
	//	Given the customer has already registered himself in the system
	//	And such record has been successfully displayed in the admin panel
	//	And an admin has seen the record
	//	And the admin approves the record
	//	Then a new bank account must be assigned to the customer
	//	And it might have some initial positive balance
	//	And the customer now can go to his profile page and see the associated account with the balance
	//	And now the customer can make transactions
	@Test
	public void shouldAssignBankAccountToCustomer() throws Bank.CustomerExistsException, Bank.AccountExistsException {
		Bank bank = new Bank();
		Double balance = 100.0;
		Customer customer = bank.createCustomer("johntest", "Doe", "johntest@doe.ee", "pass1234", balance, Currency.EUR);

		assertEquals(balance, customer.getAccount().getBalance());

		FulibTools.objectDiagrams().dumpSVG("docs/objects/customer_account_1.svg", bank);
	}

	// Scenario 1: Successful - 2 valid users
	@Test
	public void shouldTransferMoneyByCustomerSuccessfully() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AccountDoesNotExistException, Bank.TransactionRestrictionException {
		Bank bank = new Bank();
		Double balance = 100.0;
		Double amount = 10.0;
		Customer customer = bank.createCustomer("johntest", "Doe", "johntest@doe.ee", "pass1234", balance, Currency.EUR);
		Customer beneficiary = bank.createCustomer("Bob", "Jackson", "bobbytest@tt.ee", "secret", 200.0, Currency.EUR);
		var receiverAccountId = beneficiary.getAccount().getId();

		Transaction transfer  = customer.createTransaction(receiverAccountId, amount, "Gift money");

		assertEquals(90.0, customer.getAccount().getBalance(), 0.0);
		assertEquals(210.0, beneficiary.getAccount().getBalance(), 0.0);
		assertEquals(Transaction.Status.EXECUTED, transfer.getStatus());

		FulibTools.objectDiagrams().dumpSVG("docs/objects/customer_account_2.svg", bank);
	}

	// Scenario 2: Successful - user to bank
	@Test
	public void shouldTransferMoneyByCustomerToBankSuccessfully() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AccountDoesNotExistException, Bank.TransactionRestrictionException {
		Bank bank = new Bank();
		Account bankAcc = bank.getBankAccount();
		Double bankBalance = bankAcc.getBalance();
		Double balance = 100.0;
		Double amount = 10.0;
		Customer customer = bank.createCustomer("johntest", "Doe", "johntest@doe.ee", "pass1234", balance, Currency.EUR);

		Transaction transfer  = customer.createTransaction(bankAcc.getId(), amount, "Annual fee");

		assertEquals(90.0, customer.getAccount().getBalance(), 0.0);
		assertEquals(bankBalance + amount, bankAcc.getBalance(), 0.0);
		assertEquals(Transaction.Status.EXECUTED, transfer.getStatus());

		FulibTools.objectDiagrams().dumpSVG("docs/objects/customer_account_3.svg", bank);
	}

	// Scenario 3: Unsuccessful - invalid user's bank account
	@Test
	public void shouldTransferMoneyByCustomerUnsuccessfully_WhenBeneficiaryNotExists() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AccountDoesNotExistException, Bank.TransactionRestrictionException {
		Bank bank = new Bank();
		Double balance = 100.0;
		Double amount = 10.0;
		Customer customer = bank.createCustomer("johntest", "Doe", "johntest@doe.ee", "pass1234", balance, Currency.EUR);

		assertThrows(Bank.AccountDoesNotExistException.class, () -> {
			customer.createTransaction("non-exist0000000", amount, "Gift money");
		});
		assertEquals(100.0, customer.getAccount().getBalance(), 0.0);

		FulibTools.objectDiagrams().dumpSVG("docs/objects/customer_account_4.svg", bank);
	}

	// Scenario 4: Unsuccessful - insufficient funds
	@Test
	public void shouldTransferMoneyByCustomerUnsuccessfully_WhenBalanceNotEnough() throws Bank.CustomerExistsException, Bank.AccountExistsException, Bank.AccountDoesNotExistException, Bank.TransactionRestrictionException {
		Bank bank = new Bank();
		Double balance = 100.0;
		Double amount = 1000.0;
		Customer customer = bank.createCustomer("johntest", "Doe", "johntest@doe.ee", "pass1234", balance, Currency.EUR);
		Customer beneficiary = bank.createCustomer("Bob", "Jackson", "bobbytest@tt.ee", "secret", 200.0, Currency.EUR);
		var receiverAccountId = beneficiary.getAccount().getId();

		Transaction transfer  = customer.createTransaction(receiverAccountId, amount, "Gift money");

		assertEquals(100.0, customer.getAccount().getBalance(), 0.0);
		assertEquals(200.0, beneficiary.getAccount().getBalance(), 0.0);
		assertEquals("Not enough money on the source account", transfer.getRejectionDescription());
		assertEquals(Transaction.Status.ABORTED, transfer.getStatus());

		FulibTools.objectDiagrams().dumpSVG("docs/objects/customer_account_5.svg", bank);
	}

}
