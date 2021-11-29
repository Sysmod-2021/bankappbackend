package database;
import model.Account;
import model.Bank;
import model.Transaction;
import model.Customer;

import java.util.*;

public class Datastore {
    private final String path;
    private DbInMemory DbInMemory;

    public Datastore(String path, Bank b) {
        this.path = path;
        this.DbInMemory = new DbInMemory(b);
    }

    public void save(List<Customer> customerMap, List<Account> accountsMap, List<Transaction> transactionsMap ) {
        DbInMemory.save(customerMap,accountsMap,transactionsMap);
    }

    public void addTransaction(Transaction t) {
        DbInMemory.addTransaction(t);
    }
    public void addTransaction(Collection<Transaction> transactions) {
        DbInMemory.addTransaction(transactions);
    }

    public void addCustomer(Customer u) {
        DbInMemory.addCustomer(u);
    }
    public void addCustomer(Collection<Customer> Customers) {
        DbInMemory.addCustomer(Customers);
    }

    public void addAccount(Account a) {
        DbInMemory.addAccount(a);
    }
    public void addAccount(Collection<Account> accounts) {
        DbInMemory.addAccount(accounts);
    }

    public Customer getCustomer(String CustomerId) {
        return DbInMemory.getCustomer(CustomerId);
    }
    public Account getAccount(String accId) {
        return DbInMemory.getAccount(accId);
    }
    public Transaction getTransaction(String tId) {
        return DbInMemory.getTransaction(tId);
    }

    public ArrayList<Transaction> getAllTransactions() {
        return DbInMemory.getAllTransactions();
    }
    public ArrayList<Customer> getAllCustomers() {
        return DbInMemory.getAllCustomers();
    }
    public ArrayList<Account> getAllAccounts() {
        return DbInMemory.getAllAccounts();
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
        return "";
    }
}
