package model;

import database.Datastore;

import java.util.*;

public class Bank {
    private final List<Customer> customers;
    private final Map<String,Customer> customerMap;
    private final List<Administrator> admins;
    private final List<Transaction> transactions;
    private final Map<String,Transaction> transactionsMap;
    private final List<Account> accounts;
    private final Map<String,Account> accountsMap;
    private final List<Trace> traces;
    private final Account theBanksAccount;
    private final Datastore database;

    public Bank() {
        theBanksAccount = new Account(this);
        theBanksAccount.setBalance(69420.00);
        this.admins = new ArrayList<>();
        this.customerMap = new HashMap<>();
        this.accountsMap = new HashMap<>();
        this.transactionsMap = new HashMap<>();

        this.database = new Datastore("src/main/java/files/", this);

        this.customers = database.getAllCustomers();
        this.transactions = database.getAllTransactions();
        this.accounts = database.getAllAccounts();

        for (Customer c : customers
        ) {
            customerMap.put(c.getId(), c);
        }
        for (Account a : accounts
        ) {
            accountsMap.put(a.getId(), a);
        }
        for (Transaction t : transactions
        ) {
            transactionsMap.put(t.getId(), t);
        }
        this.traces = new ArrayList<>();

//        accounts.add(theBanksAccount);
//        accountsMap.put(theBanksAccount.getId(), theBanksAccount);
    }
    // TODO This can be shielded from the outside. but for now leave it at this.
    public void saveData() {
        this.database.save(customers,accounts,transactions);
    }

    public ArrayList<Transaction> getTransActions() {
        return database.getAllTransactions();
    }

    public List<Trace> getTraces() {
        return this.traces;}

    public Account getBankAccount() {
        return this.theBanksAccount;
    }

    public List<Customer> getCustomers()
    {
       return this.customers;
    }
    public Map<String,Customer> getCustomerMap()
    {
       return this.customerMap;
    }

    public Map<String,Account> getAccountsMap()
    {
        return this.accountsMap;
    }

    public List<Account> getAccounts() {
        return this.accounts;
    }

    public Bank addTrace(Trace trace) {
        traces.add(trace);
        return this;
    }

    public Bank addTransaction(Transaction transaction) {
        transactions.add(transaction);
        database.addTransaction(transaction);
        return this;
    }

    public Account getBankAccountById(String id) {
        Account account = this.getAccountsMap().get(id);

//        model.Account acc = this.getAccounts().stream()
//            .filter(account -> id.equals(account.getId()))
//            .findFirst()
//            .orElse(null);
        
        return account;
    }

	public List<Administrator> getAdmins() {
		return this.admins;
	}
}
