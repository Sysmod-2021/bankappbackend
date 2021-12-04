package model;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.iban4j.Iban;
import org.iban4j.CountryCode;

public class Account {
    public static final String PROPERTY_ID = "id";
    public static final String PROPERTY_IBAN = "iban";
    public static final String PROPERTY_OWNER = "owner";
    public static final String PROPERTY_CURRENCY = "currency";
    public static final String PROPERTY_BALANCE = "balance";
    public static final String PROPERTY_STATUS = "status";

    private final Bank bank;  // allows us to access the bank functionality, gives access to all the data
    private String id;
    private String iban;
    private String customerId;
    private Customer customer;
    private Currency currency;
    private Double balance;
    private String status;
    private Map<String, Transaction> sent;
    private Map<String, Transaction> received;

    protected PropertyChangeSupport listeners;

    public Account(Bank bank) {
        this.bank = bank;
        this.id = UUID.randomUUID().toString();
        // TODO: ensure the iban number is unique
        this.iban = Iban.random(CountryCode.EE).toString();
        this.currency = Currency.EUR;
        this.status = "ACTIVE";
        this.sent = new HashMap<>();
        this.received = new HashMap<>();
    }
    // Bank account loadsave
    public Account(Bank b, String id) {
        this.bank = b;
        this.id = id;
        // TODO: ensure the iban number is unique
        this.iban = Iban.random(CountryCode.EE).toString();
        this.currency = Currency.EUR;
        this.status = "ACTIVE";
        this.sent = new HashMap<>();
        this.received = new HashMap<>();
    }

    private void addToBank(Account account) {
        account.getBank().getAccountsMap().put(account.getId(), account);
        account.getBank().getAccounts().add(account);
    }

    public Account(Bank bank, Customer customer, Currency currency, Double balance) {
        this.id = UUID.randomUUID().toString();
        // TODO: ensure the iban number is unique
        this.iban = Iban.random(CountryCode.EE).toString();
        this.bank = bank;
        this.customer = customer;
        this.currency = currency;
        this.balance = balance;
        this.status = "ACTIVE";
        this.sent = new HashMap<>();
        this.received = new HashMap<>();
//        addToBank(this);
    }
    // for LoadSaveAccount
    public Account(Bank bank,String id, String customerId, Currency currency, Double balance) {
        this.bank = bank;
        this.id = id;
        this.customerId = customerId;
        this.currency = currency;
        this.balance = balance;
        this.sent = new HashMap<>();
        this.received = new HashMap<>();
        //addToBank(this);
    }

    public void addSentTransaction(Transaction transaction) {
        this.sent.put(transaction.getId(), transaction);
    }

    public void addReceivedTransaction(Transaction transaction) {
        this.received.put(transaction.getId(), transaction);
    }

    public Map<String, Transaction> getSentTransaction() {
        return this.sent;
    }

    public Map<String, Transaction> getReceivedTransaction() {
        return this.received;
    }

    public List<Transaction> getAllTransactionsOrderedDesc() {
        return Stream.of(this.received, this.sent)
                .flatMap(map -> map.entrySet().stream())
                .map(e -> e.getValue())
                .sorted(Comparator.comparing(Transaction::getTimestamp, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public String getId() {
        return this.id;
    }

    public Account setId(String id) {
        if (Objects.equals(id, this.id)) {
            return this;
        }
        final String oldId = this.id;
        this.id = id;
        this.firePropertyChange(PROPERTY_ID, oldId, id);
        return this;
    }

    public String getIban() { return this.iban; }

    public Account setIban(String iban) {
        if (Objects.equals(iban, this.iban)) {
            return this;
        }
        final String oldIban = this.iban;
        this.iban = iban;
        this.firePropertyChange(PROPERTY_ID, oldIban, iban);
        return this;
    }

    public String getCustomerId(){
        return  customerId;
    }
    public Double getBalance() {
        return this.balance;
    }

    public Account setBalance(Double newBalance) {
        if (Objects.equals(newBalance, this.balance)) {
            return this;
        }
        final Double oldBalance = this.balance;
        this.balance = newBalance;
        this.firePropertyChange(PROPERTY_BALANCE, oldBalance, balance);
        return this;
    }

    public Bank getBank() {
        return bank;
    }

    public Customer getOwner() {
        return customer;
    }

    public Account setCustomer(Customer newCustomer) {
        if (Objects.equals(newCustomer, this.customer)) {
            return this;
        }
        final Customer oldCustomer = this.customer;
        this.customer = newCustomer;
        this.firePropertyChange(PROPERTY_OWNER, oldCustomer, customer);
        return this;

    }

    public Currency getCurrency() {
        return currency;
    }

    public Account setCurrency(Currency newCurrency) {
        if (Objects.equals(newCurrency, this.currency)) {
            return this;
        }
        final Currency oldCurrency = this.currency;
        this.currency = newCurrency;
        this.firePropertyChange(PROPERTY_CURRENCY, oldCurrency, currency);
        return this;
    }

    public String getStatus() {
        return this.status;
    }

    public Account setStatus(String newStatus) {
        if (Objects.equals(newStatus, this.status)) {
            return this;
        }
        final String oldStatus = this.status;
        this.status = newStatus;
        this.firePropertyChange(PROPERTY_STATUS, oldStatus, status);
        return this;
    }

    public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (this.listeners != null) {
            this.listeners.firePropertyChange(propertyName, oldValue, newValue);
            return true;
        }
        return false;
    }
    public String saveToString() {
        String out = "";
        try {
            out += getId() + "," + getOwner().getId() + "," + getCurrency() + "," + getBalance();
            return out;
        } catch (NullPointerException e) {
            // owner id is empty because its the banks account.
            out += getId() + ",," + getCurrency() + "," + getBalance();
            return out;
        }

    }
}
