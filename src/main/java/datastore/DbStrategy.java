package datastore;

import model.Account;
import model.Transaction;
import model.Customer;

import java.util.*;

public interface DbStrategy {

    void save(List<Customer> customerMap, List<Account> accountsMap, List<Transaction> transactionsMap );

    void addCustomer(Customer u);
    void addTransaction(Transaction t);
    void addAccount(Account a);

    void addCustomer(Collection<Customer> Customers);
    void addTransaction(Collection<Transaction> transactions);
    void addAccount(Collection<Account> accounts);

    Customer getCustomer(String CustomerId);
    Transaction getTransaction(String transactionID);
    Account getAccount(String accountID);

    ArrayList<Customer> getAllCustomers();
    ArrayList<Transaction> getAllTransactions();
    ArrayList<Account> getAllAccounts();

    HashMap<String, Customer> returnCustomerDb();
    HashMap<String, Transaction> returnTransactionDb();
    HashMap<String, Account> returnAccountDb();

    String toString();
}
