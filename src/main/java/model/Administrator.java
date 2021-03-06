package model;

import java.util.Random;

public class Administrator extends User {
    public Administrator(Bank bank, String firstName, String lastName, String email, String password) {
        super(bank, firstName, lastName, email, password);
//        bank.getAdministrators().add(this);
    }

    public Transaction createTransactionTwoCustomers (String senderAccountId, String receiverAccountId, Double amount, String description) throws Bank.AccountDoesNotExistException, Bank.TransactionRestrictionException {
        // TODO Are we using map or list? Currently both are written.
        // Setup fake account at this point, senderAccountId / receiverAccountId could be invalid
        // Validation happens during the execution
        Account sender = new Account(this.getBank())
            .setId(senderAccountId);

        Account receiver = new Account(this.getBank())
            .setId(receiverAccountId);
       
        return performTransaction(sender, receiver, amount, description);
    }

    public Transaction createTransactionToBank (String senderAccountId, Double amount, String description) throws Bank.AccountDoesNotExistException, Bank.TransactionRestrictionException {
        // Setup fake account at this point, senderAccountId could be invalid
        // Validation happens during the execution
        Account sender = new Account(this.getBank())
            .setId(senderAccountId);

        Account receiver = getBank().getBankAccount();

        return performTransaction(sender, receiver, amount, description);
    }

    public Transaction createTransactionFromBank (String receiverAccountId, Double amount, String description) throws Bank.AccountDoesNotExistException, Bank.TransactionRestrictionException {
        // Setup fake account at this point, senderAccountId could be invalid
        // Validation happens during the execution
        Account receiver = new Account(this.getBank())
            .setId(receiverAccountId);

        Account sender = getBank().getBankAccount();

        return performTransaction(sender, receiver, amount, description);
    }

    public Transaction createSeedTransactionToCustomer(String receiverAccountId,
                                                       Double amount, Currency currency, String description)
            throws Bank.AccountDoesNotExistException, Bank.TransactionRestrictionException {
        Account receiver = this.getBank().getAccountById(receiverAccountId);
        return performSeedTransaction(receiver, amount, currency, description);
    }

    public Transaction createSeedTransactionToBank(Double amount, Currency currency, String description)
            throws Bank.AccountDoesNotExistException, Bank.TransactionRestrictionException {
        Account receiver = getBank().getBankAccount();
        return performSeedTransaction(receiver, amount, currency, description);
    }

    public Transaction createSeedTransaction(String receiverAccountId,
                                             Double amount, Currency currency, String description)
            throws Bank.AccountDoesNotExistException, Bank.TransactionRestrictionException {
        String bankId = getBank().getBankAccount().getId();

        if (bankId.equals(receiverAccountId)) {
            return createSeedTransactionToBank(amount, currency, description);
        }

        return createSeedTransactionToCustomer(receiverAccountId, amount, currency, description);
    }


    public Transaction createTransaction(String senderAccountId, String receiverAccountId, Double amount, String description) throws Bank.AccountDoesNotExistException, Bank.TransactionRestrictionException {
        String bankId = getBank().getBankAccount().getId();
        if(senderAccountId.equals(bankId)){
            return createTransactionFromBank(receiverAccountId, amount, description);
        }

        if(receiverAccountId.equals(bankId)){
            return createTransactionToBank(senderAccountId, amount, description);
        }

        return createTransactionTwoCustomers(senderAccountId, receiverAccountId, amount, description);

    }

    private Transaction performTransaction(Account sender, Account receiver,
                                           Double amount, String description)
            throws Bank.AccountDoesNotExistException, Bank.TransactionRestrictionException {
        return getBank().executeTransaction(
                this,
                Transaction.Type.CONSOLE,
                sender,
                receiver,
                Currency.EUR,
                amount,
                description
        );
    }

    public void revokeTransaction(String transactionId, String reason) throws Bank.TransactionDoesNotExistException, Bank.TransactionCanNotBeRevoked {
        Transaction revokedTransaction = getBank().getTransactionById(transactionId);
        getBank().revokeTransaction(this, revokedTransaction, reason);
    }

    public void setAccountStatus(String accountId, String status) throws Bank.AccountDoesNotExistException {
        Account customerAccount = getBank().getAccountById(accountId);
        customerAccount.setStatus(status);
    }

    private Transaction performSeedTransaction(Account receiver,
                                               Double amount, Currency currency, String description)
            throws Bank.AccountDoesNotExistException, Bank.TransactionRestrictionException {
        return getBank().executeTransaction(
                this,
                Transaction.Type.SEED,
                null,
                receiver,
                currency,
                amount,
                description
        );
    }

    public Customer createCustomerAndAccount(String firstName, String lastName, String email, Currency currency) throws Bank.CustomerExistsException, Bank.AccountExistsException {
        String password = new Random().ints(48, 122)
                            .filter(i -> (i < 57 || i > 65) && (i < 90 || i > 97)) // exclude special characters
                            .mapToObj(i -> (char) i)
                            .limit(10)
                            .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                            .toString();


        return getBank().createCustomer(firstName, lastName, email, password, 0.0, currency);
    }
}
