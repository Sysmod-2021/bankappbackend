import java.time.LocalDateTime;

public class Administrator extends User {
    public Administrator(Bank bank, String firstName, String lastName, String email, String password) {
        super(bank, firstName, lastName, email, password);
    }

    public void createTransactionTwoCustomers (String senderAccountId, String receiverAccountId, float amount, String description) {
        Account sender = new Account(this.getBank())
            .setId(senderAccountId);

        Account receiver = new Account(this.getBank())
            .setId(receiverAccountId);
        
        // default currency for now
        Currency currency = Currency.EUR;
        Transaction transaction = new Transaction(this.getBank(), sender, receiver, currency, amount, description)
            .execute();

        this.getBank().addTransaction(transaction);
        
        Trace trace = new Trace(this.getBank(), transaction, this, LocalDateTime.now());
        this.getBank().addTrace(trace);
    }
}
