package model;

import database.Datastore;

import java.util.*;

public class Bank {
    private final List<Customer> customers;
    private final Map<String,Customer> customerMap;
    private final List<Administrator> admins;
    private final List<Transaction> transactions;
    private final List<Account> accounts;
    private final Map<String,Account> accountsMap;
    private final List<Trace> traces;
    private final Account theBanksAccount;
    private final Datastore database;

    public Bank() {
        theBanksAccount = new Account(this);
        theBanksAccount.setBalance(69420.00);
        this.database = new Datastore("src/main/java/files/", this);
        this.admins = new ArrayList<>();

        this.customers = database.getAllCustomers();


        this.transactions = database.getAllTransactions();
        this.accounts = database.getAllAccounts();
        this.traces = new ArrayList<>();

        this.customerMap = new HashMap<>();
        this.accountsMap = new HashMap<>();

        accounts.add(theBanksAccount);
        accountsMap.put(theBanksAccount.getId(), theBanksAccount);
    }
    // TODO This can be shielded from the outside. but for now leave it at this.
    public void saveData() {
        this.database.save();
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
