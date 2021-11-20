import java.beans.PropertyChangeSupport;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

public class Transaction {
    public enum Status {
        REVOKED,
        EXECUTED
    }

    public static final String PROPERTY_SOURCE = "source";
    public static final String PROPERTY_DESTINATION = "destination";
    public static final String PROPERTY_STATUS = "status";
    public static final String PROPERTY_AMOUNT = "amount";
    public static final String PROPERTY_DESCRIPTION = "description";
    public static final String PROPERTY_REJ_DESCRIPTION = "rej_description";
    public static final String PROPERTY_CURRENCY = "currency";

    protected PropertyChangeSupport listeners;

    private final Bank bank;
    private final String id;
    private Account source;
    private Account destination;
    private Currency currency;
    private Float amount;
    private String description;
    private Status status;
    private String rejectionDescription;
    private final LocalDateTime timestamp;

    public Transaction(Bank bank, Account source, Account destination, Currency currency, Float amount, String description) {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.rejectionDescription = "";
        this.status = Status.EXECUTED;

        this.bank = bank;
        this.source = source;
        this.destination = destination;
        this.currency = currency;
        this.amount = amount;
        this.description = description;
    }

    public Bank getBank() {
        return bank;
    }

    public String getId() {
        return id;
    }

    public Account getSource() {
        return source;
    }

    public Transaction setSource(Account source) {
        if (Objects.equals(source, this.source)) {
            return this;
        }
        final Account oldSource = this.source;
        this.source = source;
        this.firePropertyChange(PROPERTY_SOURCE, oldSource, source);
        return this;
    }

    public Account getDestination() {
        return destination;
    }

    public Transaction setDestination(Account destination) {
        if (Objects.equals(destination, this.destination)) {
            return this;
        }
        final Account oldDestination = this.destination;
        this.destination = destination;
        this.firePropertyChange(PROPERTY_DESTINATION, oldDestination, destination);
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Transaction setCurrency(Currency currency) {
        if (Objects.equals(currency, this.currency)) {
            return this;
        }
        final Currency oldCurrency = this.currency;
        this.currency = currency;
        this.firePropertyChange(PROPERTY_CURRENCY, oldCurrency, currency);
        return this;
    }

    public Float getAmount() {
        return amount;
    }

    public Transaction setAmount(Float amount) {
        if (Objects.equals(amount, this.amount)) {
            return this;
        }
        final Float oldAmount = this.amount;
        this.amount = amount;
        this.firePropertyChange(PROPERTY_AMOUNT, oldAmount, amount);
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Transaction setDescription(String description) {
        if (Objects.equals(description, this.description)) {
            return this;
        }
        final String oldDescription = this.description;
        this.description = description;
        this.firePropertyChange(PROPERTY_DESCRIPTION, oldDescription, description);
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public Transaction setStatus(Status status) {
        if (Objects.equals(status, this.status)) {
            return this;
        }
        final Status oldStatus = this.status;
        this.status = status;
        this.firePropertyChange(PROPERTY_STATUS, oldStatus, status);
        return this;
    }

    public String getRejectionDescription() {
        return rejectionDescription;
    }

    public Transaction setRejectionDescription(String rejectionDescription) {
        if (Objects.equals(rejectionDescription, this.rejectionDescription)) {
            return this;
        }
        final String oldRejectionDescription = this.rejectionDescription;
        this.rejectionDescription = rejectionDescription;
        this.firePropertyChange(PROPERTY_REJ_DESCRIPTION, oldRejectionDescription, rejectionDescription);
        return this;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
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
