import java.util.Currency;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AdministratorAccountTests {
	
	static final String ADMIN_EMAIL = "admin@bank.ee";
	
//	Scenario 1: Administrator registers himself successfully
//	Given an admin has the access to the source of the software project
//	And knows where the configuration file with admin accounts is located
//	And he puts his name, email and password into the configuration file
//	And he restarts the bank system
//	Then the new administrator account is created with the provided information
//	And the admin now can login using his email and password  
	@Test
	public void shouldRegistersAdministratorSuccessfully() {
		Bank bank = new Bank();
		
		Administrator admin = new Administrator(bank, "Admin", "ALice", ADMIN_EMAIL, "secure_p@ssw0|2d");
		
		assertEquals(ADMIN_EMAIL, bank.getAdmins().get(0).getEmail());
		
		// FAIL 
		// - administrator account not added to Bank administrator list
		// - getAdmins() not implemented
		// - login not implemented
	}
	
//	Scenario 2: Administrator registers himself unsuccessfully
//	Given an admin has the access to the source of the software project
//	And knows where the configuration file with admin accounts is located
//	And he puts his name, email and password into the configuration file
//	And such email already exists in the configuration file
//	And he restarts the bank system
//	Then the new administrator account is not created
//	And the message about such failure is displayed in a terminal window	
	@Test
	public void shouldRegistersAdministratorUnsuccessfully_WhenEmailAlreadyExists() {
		Bank bank = new Bank();
		
		Administrator adminALice = new Administrator(bank, "Admin", "ALice", ADMIN_EMAIL, "secure_p@ssw0|2d");
		Administrator alsoAdminALice = new Administrator(bank, "ALice", "Peterson", ADMIN_EMAIL, "I-forgot");
		
		assertEquals(1, bank.getAdmins().size());
		// assertEquals("", ""); failure message
		
		// FAIL 
		// - administrator account not added to Bank administrator list
		// - getAdmins() not implemented
		// - validate email not implemented
	}
	
//	Scenario 3: Administrator registers a customer
//	Given an admin is registered and logged in
//	And there is a customer willing to create an account
//	And the customer has a direct face-to-face contact with the bank admin
//	Or the customer has a direct contact with the bank admin via the internet
//	And the customer provides information about his full name, email, personal code
//	Then the bank admin can create an account for the customer
//	And gives the customer his password
	@Test
	public void shouldRegistersCustomerSuccessfully_WhenAdminIsLoggedIn() {
		// Arrange
		Bank bank = new Bank();
		Customer customer = new Customer(bank, "John", "Doe", "john@doe.ee", "pass1234", "400000000001");
		Currency eur = Currency.getInstance("EUR");
		Float balance = 100f;
		
		//Act
		Account customerBankAccount  = new Account(bank, customer, eur, balance);
		
		//Assert
		assertEquals("john@doe.ee", bank.getCustomers().get(0).getEmail());
		
		// FAIL 
		// - login not implemented
		// - administrator account not added to Bank administrator list
		// - validated administrator is the one created an account not implement
	}
	
}
