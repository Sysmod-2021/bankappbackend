import java.time.LocalDateTime;

public class Administrator extends User {
    public Administrator(Bank bank, String firstName, String lastName, String email, String password) {
        super(bank, firstName, lastName, email, password);
    }

    public void createTransactionTwoCustomers (String senderAccountId, String receiverAccountId, Float amount, String description) {
        // TODO Are we using map or list? Currently both are written.
        // Get account from customer list
        Account sender = getBank().getBankAccountById(senderAccountId);
//        Account sender = new Account(this.getBank())
//            .setId(senderAccountId);

        Account receiver = getBank().getBankAccountById(receiverAccountId);
//        Account receiver = new Account(this.getBank())
//            .setId(receiverAccountId);
        
        // default currency for now
        Currency currency = Currency.EUR;
        Transaction transaction = new Transaction(this.getBank(), sender, receiver, currency, amount, description)
            .execute();
        if (!transaction.getStatus().equals(Transaction.Status.ABORTED)) {
            // Check if the status is aborted. if it's not then we add the transaction to the list.
            getBank().addTransaction(transaction);
        }

        Trace trace = new Trace(this.getBank(), transaction, this, LocalDateTime.now());
        this.getBank().addTrace(trace);
    }
}
