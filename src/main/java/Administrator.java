import java.time.LocalDateTime;

public class Administrator extends User {
    public Administrator(Bank bank, String firstName, String lastName, String email, String password) {
        super(bank, firstName, lastName, email, password);
        bank.getAdministrators().add(this);
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

    public Transaction createSeedTransactionToCustomer(String receiverAccountId, Double amount, Currency currency, String description) {
        Account receiver = this.getBank().getAccountById(receiverAccountId);
        return performSeedTransaction(receiver, amount, currency, description);
    }

    public Transaction createSeedTransactionToBank(Double amount, Currency currency, String description) {
        Account receiver = getBank().getBankAccount();
        return performSeedTransaction(receiver, amount, currency, description);
    }

    private void performTransaction(Account sender, Account receiver, Double amount, String description) {
        Transaction transaction = getBank().createTransaction(sender, receiver, Currency.EUR, amount, description);
        transaction.execute();
        getBank().createTrace(transaction, this);
    }

    private Transaction performSeedTransaction(Account receiver, Double amount, Currency currency, String description) {
        Transaction transaction = getBank().createTransaction(null, receiver, currency, amount, description).seed();
        getBank().createTrace(transaction, this);
        return transaction;
    }
}
