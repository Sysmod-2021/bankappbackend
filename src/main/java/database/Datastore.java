package database;

import model.Account;
import model.Bank;
import model.Transaction;
import model.Customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Datastore {
    private final String path;
    private TransactionDbInMemory transactionDB;
    private CustomerDbInMemory CustomerDb;
    private AccountDbInMemory accountDb;

    public Datastore(String path, Bank b) {
        this.path = path;
        System.out.println(path);
        this.transactionDB = new TransactionDbInMemory(b);
        this.CustomerDb = new CustomerDbInMemory(b);
        this.accountDb = new AccountDbInMemory(b);
    }

    public void save() {
        transactionDB.save();
    }

    public void addTransaction(Transaction t) {
        transactionDB.add(t);
    }
    public void addTransaction(Collection<Transaction> transactions) {
        transactionDB.add(transactions);
    }

    public void addCustomer(Customer u) {
        CustomerDb.add(u);
    }
    public void addCustomer(Collection<Customer> Customers) {
        CustomerDb.add(Customers);
    }

    public void addAccount(Account a) {
        accountDb.add(a);
    }
    public void addAccount(Collection<Account> accounts) {
        accountDb.add(accounts);
    }

    public Customer getCustomer(String CustomerId) {
        return CustomerDb.getCustomer(CustomerId);
    }
    public Account getAccount(String accId) {
        return accountDb.getAccount(accId);
    }
    public Transaction getTransaction(String tId) {
        return transactionDB.getTransaction(tId);
    }

    public ArrayList<Transaction> getAllTransactions() {
        return transactionDB.getAll();
    }
    public ArrayList<Customer> getAllCustomers() {
        return CustomerDb.getAll();
    }
    public ArrayList<Account> getAllAccounts() {
        return accountDb.getAll();
    }
    public HashMap<String, Transaction> returnTransactionDb() {
        return returnTransactionDb();
    }
    public HashMap<String, Account> returnAccountDb() {
        return returnAccountDb();
    }
    public HashMap<String, Customer> returnCustomerDb() {
        return returnCustomerDb();
    }

    public String toString() {
        return transactionDB.toString();
    }
}
