import org.fulib.FulibTools;
import org.junit.Test;

import model.*;

import static org.junit.Assert.assertEquals;

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
		Customer customer = bank.createCustomer("John", "Doe", "john@doe.ee", "pass1234", balance, Currency.EUR);

		assertEquals(balance, customer.getAccount().getBalance());

		FulibTools.objectDiagrams().dumpSVG("docs/objects/customer_account_1.svg", bank);
	}
}
