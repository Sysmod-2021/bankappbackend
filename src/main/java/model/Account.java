package model;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private int count = 0;

    protected PropertyChangeSupport listeners;

    public Account(Bank bank) {
        this.bank = bank;
        this.id = UUID.randomUUID().toString();
        this.iban = getUniqueIban();
        this.currency = Currency.EUR;
        this.status = "ACTIVE";
        this.sent = new HashMap<>();
        this.received = new HashMap<>();
    }
    // Bank account loadsave
    public Account(Bank b, String id) {
        this.bank = b;
        this.id = id;
        this.iban = getUniqueIban();
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
        this.iban = getUniqueIban();
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
    public Account(Bank bank, String id, String iban, String customerId, Currency currency, Double balance, String status) {
        this.bank = bank;
        this.id = id;
        this.iban = iban;
        this.customerId = customerId;
        this.currency = currency;
        this.balance = balance;
        this.status = status;
        this.sent = new HashMap<>();
        this.received = new HashMap<>();
        //addToBank(this);
    }

    private String getUniqueIban() {
        Path accountRecordPath = Paths.get("src", "main", "java", "files", "accounts.txt");
        String accountRecord;

        try {
            accountRecord = Files.readString(accountRecordPath);
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed-to-Read-Accounts-TXT";
        }

        String iban = Iban.random(CountryCode.EE).toString();

        while (accountRecord.contains(iban)) {
            iban = Iban.random(CountryCode.EE).toString();
        }

        return iban;
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

    public List<Transaction> getAllTransactionsWithSignAndOrderedDesc() {
        Map<String, Transaction> updatedSentTransactions = 
            this.sent.entrySet()
            .stream()
            .collect(Collectors.toMap(
                entry -> entry.getKey(),
                entry -> {
                    Transaction copiedTransaction = new Transaction(entry.getValue());
                    return copiedTransaction.setAmount(-copiedTransaction.getAmount());
                }
            ));

        return Stream.of(this.received, updatedSentTransactions)
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
        this.firePropertyChange(PROPERTY_IBAN, oldIban, iban);
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
        if(getOwner() != null)
            return String.join(",",
                getId(),
                getIban(),
                getOwner().getId(),
                getCurrency().toString(),
                getBalance().toString(),
                getStatus()
            );
        else{
            // owner id is empty because its the banks account.
            String out = getId() + ",,," + getCurrency() + "," + getBalance() + "," + getStatus();
            return out;
        }
    }

    public void addCount() {
        this.count++;
    }

    public int getCount() {
        return count;
    }

    public void resetTransactionCounter() {
        this.count = 0;
    }
}
