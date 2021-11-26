import model.Account;
import model.Bank;
import model.Currency;
import model.Customer;
import org.fulib.FulibTools;
import org.junit.Test;
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
	public void shouldAssignBankAccountToCustomer() {
		
		// Arrange
		Bank bank = new Bank();
		Customer customer = new Customer(bank, "John", "Doe", "john@doe.ee", "pass1234", "400000000001");
		Double balance = 100.0;
		
		//Act
		Account customerBankAccount  = new Account(bank, customer, Currency.EUR, balance);
		
		//Assert
		assertEquals(balance, bank.getAccounts().get(0).getBalance());
		
		// FAIL 
		// - login not implemented
		// - approve record not implemented
		// - profile page not implement
		
		FulibTools.objectDiagrams().dumpSVG("docs/objects/test/customer_s1.svg", customerBankAccount);
	}
}
