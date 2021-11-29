package model;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import exceptions.TransactionExceptions;

public class Bank {
    // Exceptions

    public static class AccountExistsException extends Exception {
        public AccountExistsException(String message) {
            super(message);
        }
    }

    public static class AccountDoesNotExistException extends Exception {
        public AccountDoesNotExistException(String message) {
            super(message);
        }
    }

    public static class CustomerDoesNotExistException extends Exception {
        public CustomerDoesNotExistException(String message) {
            super(message);
        }
    }

    public static class CustomerExistsException extends Exception {
        public CustomerExistsException(String message) {
            super(message);
        }
    }

    public static class AdministratorExistsException extends Exception {
        public AdministratorExistsException(String message) {
            super(message);
        }
    }

    public static class TransactionExistsException extends Exception {
        public TransactionExistsException(String message) {
            super(message);
        }
    }

    public static class TransactionDoesNotExistException extends Exception {
        public TransactionDoesNotExistException(String message) {
            super(message);
        }
    }

    // Main in-memory storage of all the bank-related information
    private final List<Customer> customers;
    private final List<Administrator> administrators;
    private final List<Transaction> transactions;
    private final List<Account> accounts;
    private final List<Trace> traces;
    private final Account bankAccount;

    // Maps below are for faster access (might be irrelevant for our toy use case with 2 customers demonstration)
    private final Map<String, Customer> customerMap;
    private final Map<String, Account> accountsMap;

    public Bank() {
        this.customers = new ArrayList<>();
        this.administrators = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.accounts = new ArrayList<>();
        this.traces = new ArrayList<>();

        this.customerMap = new HashMap<>();
        this.accountsMap = new HashMap<>();

        bankAccount = new Account(this);
        bankAccount.setBalance(69420.0);
        try {
            addAccount(bankAccount);
        } catch (Exception ignored) { // at this moment it can't throw anything here, as everything has just been initialized to empty lists
        }
    }

    public List<Administrator> getAdministrators() {
        return this.administrators;
    }

    public Account getBankAccount() {
        return this.bankAccount;
    }

    // Administrator management

    private void addAdministrator(Administrator administrator) throws AdministratorExistsException {
        for (Administrator a : administrators) {
            if (a.getEmail().equals(administrator.getEmail())) {
                throw new AdministratorExistsException("Administrator exists: " + administrator.getEmail());
            }
        }
        administrators.add(administrator);
    }

    public Administrator createAdministrator(String firstName, String lastName, String email, String password) throws AdministratorExistsException {
        Administrator administrator = new Administrator(this, firstName, lastName, email, password);
        addAdministrator(administrator);
        return administrator;
    }

    public Administrator getAdministrator() { // let's say here we don't care about any particular administrator at the moment
        if (administrators.size() > 0) {
            return administrators.get(0);
        }
        return null;
    }

    // Customer management

    private Map<String, Customer> getCustomerMap() {
        return this.customerMap;
    }

    private void addCustomer(Customer customer) throws CustomerExistsException {
        for (Customer c : customers) {
            if (c.getEmail().equals(customer.getEmail())) {
                throw new CustomerExistsException("Customer exists: " + customer.getEmail());
            }
        }
        customers.add(customer);
        customerMap.put(customer.getEmail(), customer);
    }

    public List<Customer> getCustomers() {
        return this.customers;
    }

    public Customer getCustomerByEmail(String email) throws CustomerDoesNotExistException {
        Customer customer =  getCustomerMap().get(email);
        if (customer == null) {
            throw new CustomerDoesNotExistException("Account does not exist: " + email);
        }
        return customer;
    }

    public Customer createCustomer(String firstName, String lastName, String email, String password, Double initial_balance, Currency currency) throws CustomerExistsException, AccountExistsException {
        Customer customer = new Customer(this, firstName, lastName, email, password);
        addCustomer(customer);

        createAccount(customer, currency, initial_balance);

        return customer;
    }

    // Transaction management

    private void addTransaction(Transaction transaction) throws TransactionExistsException {
        for (Transaction t : transactions) {
            if (t.getId().equals(transaction.getId())) {
                throw new TransactionExistsException("Transaction exists: " + transaction.getId());
            }
        }
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public List<Transaction> getTransactionsBySource(Account account) {
        return transactions.stream()
                .filter(transaction -> transaction.getSource().getId().equals(account.getId()))
                .sorted(Comparator.comparing(Transaction::getTimestamp))
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByDestination(Account account) {
        return transactions.stream()
                .filter(transaction -> transaction.getDestination().getId().equals(account.getId()))
                .sorted(Comparator.comparing(Transaction::getTimestamp))
                .collect(Collectors.toList());
    }

    public Transaction getTransactionById(String transactionId) throws TransactionDoesNotExistException {
        Transaction existingTransaction;
        try {
            existingTransaction = transactions.stream()
                                    .filter(transaction -> transaction.getId().equals(transactionId))
                                    .collect(Collectors.toList())
                                    .get(0);
        } catch (Exception exception) {
            throw new TransactionDoesNotExistException("Transaction" + transactionId + "does not exist in the bank");
        }

        return existingTransaction;
    }

    public Transaction createTransaction(Account source, Account destination, Currency currency, Double amount, String description) {
        Transaction transaction = new Transaction(this, source, destination, currency, amount, description);
        try {
            addTransaction(transaction);
        } catch (Exception ignored) {
        }
        return transaction;
    }

    void revokeTransaction(Administrator administrator, Transaction transaction, String reason) throws TransactionExceptions.TransactionCanNotBeRevoked {
        Transaction revokedTransaction = transaction.revoke(reason);
        this.createTrace(revokedTransaction, administrator);
    }

    // Account management

    public Account getAccountById(String id) throws AccountDoesNotExistException {
        Account account = this.getAccountsMap().get(id);
        if (account == null) {
            throw new AccountDoesNotExistException("Account does not exist: " + id);
        }
        return account;
    }

    public Account getAccountByEmail(String email) {
        return getAccounts().stream()
                .filter(account -> {
                    Customer customer = account.getOwner();
                    return customer != null && customer.getEmail().equals(email); // the bank's account can have nil owner
                })
                .findFirst()
                .orElse(null);
    }

    public List<Account> getAccounts() {
        return this.accounts;
    }

    private Map<String, Account> getAccountsMap() {
        return this.accountsMap;
    }

    private void addAccount(Account account) throws AccountExistsException {
        for (Account a : accounts) {
            if (a.getId().equals(account.getId())) {
                throw new AccountExistsException("Account exists: " + account.getId());
            }
        }
        accounts.add(account);
        accountsMap.put(account.getId(), account);
    }

    public Account createAccount(Customer customer, Currency currency, Double balance) throws AccountExistsException {
        Account account = new Account(this, customer, currency, balance);
        addAccount(account);
        return account;
    }

    // Trace management

    public List<Trace> getTraces() {
        return this.traces;
    }

    private void addTrace(Trace trace) {
        traces.add(trace);
    }

    public Trace createTrace(Transaction transaction, User user) {
        Trace trace = new Trace(this, new Transaction(transaction), user, LocalDateTime.now());
        addTrace(trace);
        return trace;
    }
}
