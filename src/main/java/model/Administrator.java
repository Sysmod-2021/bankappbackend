package model;

import java.time.LocalDateTime;
import exceptions.TransactionExceptions;

public class Administrator extends User {
    public Administrator(Bank bank, String firstName, String lastName, String email, String password) {
        super(bank, firstName, lastName, email, password);
//        bank.getAdministrators().add(this);
    }

    public Transaction createTransactionTwoCustomers (String senderAccountId, String receiverAccountId, Double amount, String description) throws Bank.AccountDoesNotExistException, TransactionExceptions.TransactionRestrictionException {
        // TODO Are we using map or list? Currently both are written.
        // Setup fake account at this point, senderAccountId / receiverAccountId could be invalid
        // Validation happens during the execution
        Account sender = new Account(this.getBank())
            .setId(senderAccountId);

        Account receiver = new Account(this.getBank())
            .setId(receiverAccountId);
       
        return performTransaction(sender, receiver, amount, description);
    }

    public Transaction createTransactionToBank (String senderAccountId, Double amount, String description) throws Bank.AccountDoesNotExistException, TransactionExceptions.TransactionRestrictionException {
        // Setup fake account at this point, senderAccountId could be invalid
        // Validation happens during the execution
        Account sender = new Account(this.getBank())
            .setId(senderAccountId);

        Account receiver = getBank().getBankAccount();

        return performTransaction(sender, receiver, amount, description);
    }

    public Transaction createTransactionFromBank (String receiverAccountId, Double amount, String description) throws Bank.AccountDoesNotExistException, TransactionExceptions.TransactionRestrictionException {
        // Setup fake account at this point, senderAccountId could be invalid
        // Validation happens during the execution
        Account receiver = new Account(this.getBank())
            .setId(receiverAccountId);

        Account sender = getBank().getBankAccount();

        return performTransaction(sender, receiver, amount, description);
    }

    public Transaction createSeedTransactionToCustomer(String receiverAccountId, Double amount, Currency currency, String description) throws Bank.AccountDoesNotExistException {
        Account receiver = this.getBank().getAccountById(receiverAccountId);
        return performSeedTransaction(receiver, amount, currency, description);
    }

    public Transaction createSeedTransactionToBank(Double amount, Currency currency, String description) throws Bank.AccountDoesNotExistException {
        Account receiver = getBank().getBankAccount();
        return performSeedTransaction(receiver, amount, currency, description);
    }

    private Transaction performTransaction(Account sender, Account receiver, Double amount, String description) throws Bank.AccountDoesNotExistException, TransactionExceptions.TransactionRestrictionException {
        Transaction transaction = getBank().createTransaction(sender, receiver, Currency.EUR, amount, description).execute();
        getBank().createTrace(transaction, this);
        return transaction;
    }

    public void revokeTransaction(String transactionId, String reason) throws Bank.TransactionDoesNotExistException, TransactionExceptions.TransactionCanNotBeRevoked {
        Transaction revokedTransaction = getBank().getTransactionById(transactionId);
        getBank().revokeTransaction(this, revokedTransaction, reason);
    }

    public void setAccountStatus(String accountId, String status) throws Bank.AccountDoesNotExistException {
        Account customerAccount = getBank().getAccountById(accountId);
        customerAccount.setStatus(status);
    }

    private Transaction performSeedTransaction(Account receiver, Double amount, Currency currency, String description) throws Bank.AccountDoesNotExistException {
        Transaction transaction = getBank().createTransaction(null, receiver, currency, amount, description).seed();
        getBank().createTrace(transaction, this);
        return transaction;
    }
}
