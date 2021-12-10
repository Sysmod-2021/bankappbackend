import model.*;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class TraceTests {
    static final String TRANS_DESC = "Seed transaction";

    private Boolean stringInTrace(String string) {
        Path logPath = Paths.get("src", "main", "java", "files", "traces.log");
        String logRecord;

        try {
            logRecord = Files.readString(logPath);
        } catch (IOException e) {
            return false;
        }

        return logRecord.contains(string);
    }

    @Test
    public void traceExists()
            throws Bank.CustomerExistsException,
                   Bank.AccountExistsException,
                   Bank.AdministratorExistsException,
                   Bank.AccountDoesNotExistException,
                   Bank.TransactionRestrictionException {
        Bank bank = new Bank();
        Administrator admin = bank.createAdministrator(
                "Admin",
                "ALice",
                "admin@bank.ee",
                "secure_p@ssw0|2d"
        );
        Customer customer = bank.createCustomer(
                "Bob",
                "Jackson",
                "bobbytest@tt.ee",
                "secret",
                0.0,
                Currency.EUR
        );
        Transaction transfer = admin.createSeedTransactionToCustomer(
                customer.getAccount().getId(),
                30.0,
                Currency.EUR,
                TRANS_DESC
        );
        assertTrue(stringInTrace(transfer.getId()));
    }
}
