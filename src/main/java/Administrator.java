import java.time.LocalDateTime;

public class Administrator extends User {
    public Administrator(Bank bank, String firstName, String lastName, String email, String password) {
        super(bank, firstName, lastName, email, password);
    }

    public void createTransactionTwoCustomers (String senderAccountId, String receiverAccountId, Double amount, String description) {
        // TODO Are we using map or list? Currently both are written.
        // Setup fake account at this point, senderAccountId / receiverAccountId could be invalid
        // Validation happens during the execution
        Account sender = new Account(this.getBank())
            .setId(senderAccountId);

        Account receiver = new Account(this.getBank())
            .setId(receiverAccountId);
        
        performTransaction(sender, receiver, amount, description);
    }

    public void createTransactionToBank (String senderAccountId, Double amount, String description) {
        // Setup fake account at this point, senderAccountId could be invalid
        // Validation happens during the execution
        Account sender = new Account(this.getBank())
            .setId(senderAccountId);

        Account receiver = getBank().getBankAccount();

        performTransaction(sender, receiver, amount, description);
    }

    public void createTransactionFromBank (String receiverAccountId, Double amount, String description) {
        // Setup fake account at this point, senderAccountId could be invalid
        // Validation happens during the execution
        Account receiver = new Account(this.getBank())
            .setId(receiverAccountId);

        Account sender = getBank().getBankAccount();

        performTransaction(sender, receiver, amount, description);
    }

    private void performTransaction(Account sender, Account receiver, Double amount, String description) {
        Transaction transaction = new Transaction(this.getBank(), sender, receiver, Currency.EUR, amount, description)
        .execute();
        if (!transaction.getStatus().equals(Transaction.Status.ABORTED)) {
            // Check if the status is aborted. if it's not then we add the transaction to the list.
            getBank().addTransaction(transaction);
        }

        Trace trace = new Trace(this.getBank(), transaction, this, LocalDateTime.now());
        this.getBank().addTrace(trace);
    }
}
