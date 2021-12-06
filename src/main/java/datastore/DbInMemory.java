package datastore;

import model.Account;
import model.Bank;
import model.Customer;
import model.Transaction;

import java.util.*;

public class DbInMemory implements DbStrategy {
    private HashMap<String, Customer> CustomerHashMap;
    private HashMap<String, Transaction> transactionHashMap;
    private HashMap<String, Account> accountHashMap;
    LoadSaveStrategy s;

    public DbInMemory(Bank b) {
        s = LoadSaveFactory.fromType(LoadSaveStrategyEnum.TEXT);
        CustomerHashMap = new HashMap<>();
        transactionHashMap = new HashMap<>();
        accountHashMap = new HashMap<>();
        // Add customer information into database.
        addCustomer(s.customerLoadWithList(b));
        if (getAllCustomers().size() != 0) { // Customers are added and there is at least 1 customer, corresponding accounts can then exist.
            addAccount(s.accountLoadWithList(b)); // Add account information into database.
            if (getAllAccounts().size() != 0) { // Accounts are added and there is at least 1 account, corresponding transaction can then exist.
                addTransaction(s.transactionLoadWithList(b)); // Add transaction information into database.
            }
        }

    }

    @Override
    public void save(List<Customer> customerMap, List<Account> accountsMap, List<Transaction> transactionsMap ) {
        s.saveCustomersWithList(customerMap);
        s.saveAccountsWithList(accountsMap);
        s.saveTransactionsWithList(transactionsMap);
    }

    @Override
    public void addCustomer(Customer u) {
        if (u != null) {
            CustomerHashMap.put(u.getId(), u);
        } else {
            throw new IllegalArgumentException("Empty Customer");
        }
    }

    @Override
    public void addTransaction(Transaction t) {
        if (t != null) {
            transactionHashMap.put(t.getId(), t);
        } else {
            throw new IllegalArgumentException("Empty transaction");
        }
    }

    @Override
    public void addAccount(Account a) {
        if (a != null) {
            accountHashMap.put(a.getId(), a);
        } else {
            throw new IllegalArgumentException("Empty User");
        }
    }

    @Override
    public void addCustomer(Collection<Customer> Customers) {
        if (Customers.size() > 0) {
            for (Customer u : Customers) {
                addCustomer(u);
            }
        } else {
            System.out.println("No Customers found, starting blank");
        }
    }

    @Override
    public void addTransaction(Collection<Transaction> transactions) {
        if (transactions.size() > 0) {
            for (Transaction t : transactions) {
                addTransaction(t);
            }
        } else {
            System.out.println("No transactions found, starting blank");
        }
    }

    @Override
    public void addAccount(Collection<Account> accounts) {
        if (accounts.size() > 0) {
            for (Account a : accounts) {
                addAccount(a);
            }
        } else {
            System.out.println("No accounts found, starting blank");
        }
    }


    @Override
    public Customer getCustomer(String CustomerId) {
        return CustomerHashMap.get(CustomerId);
    }

    @Override
    public Transaction getTransaction(String transactionID) {
        return null;
    }

    @Override
    public Account getAccount(String accountID) {
        return null;
    }

    @Override
    public ArrayList<Customer> getAllCustomers() {
        return new ArrayList<>(CustomerHashMap.values());
    }
    @Override
    public ArrayList<Transaction> getAllTransactions() {
        return new ArrayList<>(transactionHashMap.values());
    }
    @Override
    public ArrayList<Account> getAllAccounts() {
        return new ArrayList<>(accountHashMap.values());
    }

    @Override
    public HashMap<String, Customer> returnCustomerDb() {
        return CustomerHashMap;
    }

    @Override
    public HashMap<String, Transaction> returnTransactionDb() {
        return transactionHashMap;
    }

    @Override
    public HashMap<String, Account> returnAccountDb() {
        return accountHashMap;
    }
}
