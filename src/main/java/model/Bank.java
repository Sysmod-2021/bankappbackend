package model;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import datastore.Datastore;

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

    public static class AdministratorDoesNotExistException extends Exception {
        public AdministratorDoesNotExistException(String message) {
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

    public static class TransactionCanNotBeRevoked extends Exception {
        public TransactionCanNotBeRevoked(String message) {
            super(message);
        }
    }

    public static class TransactionRestrictionException extends Exception {
        public TransactionRestrictionException(String message) {
            super(message);
        }
    }

    // Main in-memory storage of all the bank-related information
    private final List<Customer> customers;
    private final List<Administrator> administrators;
    private final List<Transaction> transactions;
    private final List<Account> accounts;
    private final List<Trace> traces;
    private final Account theBanksAccount;
    private final Datastore database;

    // Maps below are for faster access (might be irrelevant for our toy use case with 2 customers demonstration)
    private final Map<String, Customer> customerMap;
    private final Map<String, Account> accountsMap;
    private final Map<String,Transaction> transactionsMap;
  
    public Bank() {
      
        this.customers = new ArrayList<>();
        this.administrators = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.accounts = new ArrayList<>();
      
        this.customerMap = new HashMap<>();
        this.accountsMap = new HashMap<>();
        this.transactionsMap = new HashMap<>();
      
        this.database = new Datastore("src/main/java/files/", this);

        theBanksAccount = new Account(this, "sendItToTheBank");
        theBanksAccount.setBalance(69420.00);
        try {
            if (!getAccounts().contains(theBanksAccount)) {
                addAccount(theBanksAccount);
            }
        } catch (AccountExistsException e) {
            // Bank account already exists so were not going to add it again
        }


        customers.addAll(database.getAllCustomers());
        for (Customer c : customers
        ) {
            customerMap.put(c.getId(), c);
        }

        accounts.addAll(database.getAllAccounts().stream().map(a -> a.setCustomer(customerMap.get(a.getCustomerId()))).collect(Collectors.toList()));
        for (Account a : accounts
        ) {
            accountsMap.put(a.getId(), a);
        }

        transactions.addAll(database.getAllTransactions());
        for (Transaction t : transactions
        ) {
            transactionsMap.put(t.getId(), t);
        }
      
        
        this.traces = new ArrayList<>();
        counterRoutine();
//       TODO Need to figure some things out for data persistence with bank account.
//         try {
//             addAccount(bankAccount);
//         } catch (Exception ignored) { // at this moment it can't throw anything here, as everything has just been initialized to empty lists
//         }
    }
  
    // TODO This can be shielded from the outside. but for now leave it at this.
    //  VERY IMPORTANT - ALWAYS CLEAR file contents when testing manually. overwrite of document is a bit wonky.
    public void saveData() {
        this.database.save(customers,accounts,transactions);
    }

    public List<Administrator> getAdministrators() {
        return this.administrators;
    }

    public Account getBankAccount() {
        return this.theBanksAccount;
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

    public Administrator getAdministrator(String id) { // let's say here we don't care about any particular administrator at the moment
        if (administrators.size() > 0) {
            return administrators.stream().filter( a -> a.getId().equals(id)).findFirst().orElse(null);
        }
        return null;
    }

    public Administrator getAdministratorByEmail(String email) throws AdministratorDoesNotExistException {
        Administrator existingAdministrator;
        try {
            existingAdministrator = administrators.stream()
                    .filter(administrator -> administrator.getEmail().equals(email))
                    .collect(Collectors.toList())
                    .get(0);
        } catch (Exception exception) {
            throw new AdministratorDoesNotExistException("Administrator" + email + "does not exist in the bank");
        }

        return existingAdministrator;
    }

    // Customer management

    public Map<String, Customer> getCustomerMap() {
        return this.customerMap;
    }

    private void addCustomer(Customer customer) throws CustomerExistsException {
        for (Customer c : customers) {
            if (c.getEmail().equals(customer.getEmail())) {
                throw new CustomerExistsException("Customer exists: " + customer.getEmail());
            }
        }
        customers.add(customer);
        customerMap.put(customer.getId(), customer);
    }

    public List<Customer> getCustomers() {
        return this.customers;
    }

    public Customer getCustomer(String id) throws CustomerDoesNotExistException {
        Customer customer =  getCustomerMap().get(id);
        if (customer == null) {
            throw new CustomerDoesNotExistException("Customer does not exist: " + id);
        }
        return customer;
    }

    public Customer getCustomerByEmail(String email) throws CustomerDoesNotExistException {
        Customer existingCustomer;
        try {
            existingCustomer = customers.stream()
                                .filter(customer -> customer.getEmail().equals(email))
                                .collect(Collectors.toList())
                                .get(0);
        } catch (Exception exception) {
            throw new CustomerDoesNotExistException("Customer" + email + "does not exist in the bank");
        }

        return existingCustomer;
    }

    public Customer createCustomer(
    		String firstName, 
    		String lastName, 
    		String email, 
    		String password, 
    		Double initial_balance, 
    		Currency currency
    ) throws CustomerExistsException, AccountExistsException {
        Customer customer = new Customer(
        		this,UUID.randomUUID().toString(), 
        		firstName, lastName, email, password);
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

    public Transaction createTransaction(Account source, Account destination,
                                         Currency currency, Double amount, String description) {
        Transaction transaction = new Transaction(
                this,
                source,
                destination,
                currency,
                amount,
                description
        );

        try {
            addTransaction(transaction);
        }
        catch (Exception ignored) {}

        return transaction;
    }

    public Transaction executeTransaction(User initiator, Transaction.Type type,
                                          Account source, Account destination,
                                          Currency currency, Double amount, String description)
            throws AccountDoesNotExistException, TransactionRestrictionException {
        Transaction transaction = this.createTransaction(
                source,
                destination,
                currency,
                amount,
                description
        );

        try {
            if (type == Transaction.Type.SEED) {
                transaction = transaction.seed();
            } else {
                transaction = transaction.execute();
            }
        }
        catch (Exception e) {
            this.logTrace(transaction, initiator, e);
            throw e;
        }

        logTrace(transaction, initiator, null);

        return transaction;
    }

    Transaction performTransaction(User user,
                                   Account sender, Account receiver,
                                   Double amount, String description)
            throws Bank.AccountDoesNotExistException, Bank.TransactionRestrictionException {
        return this.executeTransaction(
                user,
                Transaction.Type.CUSTOMER,
                sender,
                receiver,
                Currency.EUR,
                amount,
                description
        );
    }

    void revokeTransaction(Administrator administrator, Transaction transaction, String reason) throws Bank.TransactionCanNotBeRevoked {
        try {
            Transaction revokedTransaction = transaction.revoke(reason);
            logTrace(revokedTransaction, administrator, null);
        }
        catch (Exception e) {
            logTrace(transaction, administrator, e);
            throw e;
        }
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

    public Map<String, Account> getAccountsMap() {
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
        return new Trace(this, new Transaction(transaction), user, LocalDateTime.now());
    }

    public void logTrace(Transaction transaction, User initiator, Exception e) {
        transaction = getTraceTransaction(transaction, e);
        Trace trace = createTrace(transaction, initiator);
        addTrace(trace);
        String logString = trace.getTraceString();
        File file = new File("src/main/java/files/traces.log");

        try {
            FileWriter fr = new FileWriter(file, true);
            fr.write(logString);
            fr.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Transaction getTraceTransaction(Transaction transaction, Exception e) {
        if (e == null) {
            return transaction;
        }

        if (transaction.getStatus() != Transaction.Status.EXECUTED) {
            transaction = transaction.setStatus(Transaction.Status.ABORTED);
            transaction = transaction.setRejectionDescription(e.getMessage());
        }

        return transaction;
    }

    // Auth

    public AuthResponse authenticate(String email, String password) throws Exception {
        User user = null;
        String user_type;
        try {
            user = this.getAdministratorByEmail(email);

            if (!user.getPassword().equals(password)) {
                throw new Exception("Wrong Credentials");
            }
            user_type = "administrator";
        }
        catch (Bank.AdministratorDoesNotExistException e) {
            user = this.getCustomerByEmail(email);
            if (!user.getPassword().equals(password)) {
                throw new Exception("Wrong Credentials");
            }
            user_type = "customer";
        }
        return new AuthResponse(user.getId(), user_type);
    }

    // Extra feature : Cashback on transactions if done 3 times per hour. -> 5€ per 3 transactions cashback (see Transaction.java)
    // Automatic reset of counter for each account after each hour. -> Routine call starting after bank is launched.
    public void counterRoutine() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // 1 Hour has passed
                resetCounters();
            }
        };
        Timer timer = new Timer();
        long delay = 0;
        long intervalPeriod = 60 * 60 * 1000;
        // schedules the task to be run in an interval
        timer.scheduleAtFixedRate(task, delay,
                intervalPeriod);
    }
    private void resetCounters() {
        for (Account a : getAccounts()
        ) {
            // for each account reset the counter back to 0. So we can count the next interval hour.
            a.resetTransactionCounter();
        }
    }
}
