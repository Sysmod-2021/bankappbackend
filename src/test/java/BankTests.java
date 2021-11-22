import org.fulib.FulibTools;
import org.junit.Test;

public class BankTests {
    @Test
    public void shouldGenerateObjectDiagrams() {
    	java.util.Currency eur = java.util.Currency.getInstance("EUR");
		
    	Bank bank = new Bank();
 
    	Administrator admin = new Administrator(bank, "Admin", "ALice", "admin@bank.ee", "secure_p@ssw0|2d");

    	Customer customer = new Customer(bank, "John", "Doe", "john@doe.ee", "pass1234", "400000000001");
        Account customerBankAccount  = new Account(bank, customer, eur, 100f);
		
		Customer beneficiary = new Customer(bank, "Bob", "Jackson", "bobby@tt.ee", "secret", "400000000002");
		Account destinationBankAccount  = new Account(bank, beneficiary, eur, 0f);

		Transaction transfer = new Transaction(bank, customerBankAccount, destinationBankAccount, Currency.EUR,  25.50f, "ref: 001");
		transfer.execute();

        FulibTools.objectDiagrams().dumpSVG("docs/objects/tranfermoney_objects.svg", transfer);
    }
}








