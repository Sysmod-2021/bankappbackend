import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.Currency;

public class Account {

    public static final String PROPERTY_ID = "id";
    public static final String PROPERTY_OWNER = "owner";
    public static final String PROPERTY_CURRENCY = "currency";
    public static final String PROPERTY_BALANCE = "balance";

    private final Bank bank;  // allows us to access the bank functionality, gives access to all the data
    private String id;
    private Customer customer;
    private Currency currency;
    private Float balance;
    // TODO do we keep 2 separate lists or 2 separate maps? (map increases performance but overview might be scuffed)
    private Map<String, Transaction> sent;
    private Map<String, Transaction> received;

    protected PropertyChangeSupport listeners;

    public Account(Bank bank) {
        this.bank = bank;
        this.id = UUID.randomUUID().toString();
    }

    public Account(Bank bank, Customer customer, Currency currency, Float balance) {
        this.id = UUID.randomUUID().toString();
        this.bank = bank;
        this.customer = customer;
        this.currency = currency;
        this.balance = balance;
        this.sent = new HashMap<>();
        this.received = new HashMap<>();
    }

    public void addSentTransaction(Transaction transaction) {
        this.sent.put(transaction.getId(), transaction);
    }

    public void addReceivedTransaction(Transaction transaction) {
        this.received.put(transaction.getId(), transaction);
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

    public Float getBalance() {
        return this.balance;
    }

    public Account setBalance(Float newBalance) {
        if (Objects.equals(newBalance, this.balance)) {
            return this;
        }
        final Float oldBalance = this.balance;
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

    public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
    {
        if (this.listeners != null)
        {
            this.listeners.firePropertyChange(propertyName, oldValue, newValue);
            return true;
        }
        return false;
    }
}
