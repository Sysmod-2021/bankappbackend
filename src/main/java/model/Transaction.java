package model;

import java.beans.PropertyChangeSupport;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Transaction {
    public enum Status {
        REVOKED,
        EXECUTED,
        ABORTED
    }
    public static final String PROPERTY_ID = "transaction_id";
    public static final String PROPERTY_SOURCE = "source";
    public static final String PROPERTY_DESTINATION = "destination";
    public static final String PROPERTY_STATUS = "status";
    public static final String PROPERTY_AMOUNT = "amount";
    public static final String PROPERTY_DESCRIPTION = "description";
    public static final String PROPERTY_REJ_DESCRIPTION = "rej_description";
    public static final String PROPERTY_CURRENCY = "currency";
    public static final String PROPERTY_TIMESTAMP = "timestamp";

    protected PropertyChangeSupport listeners;

    private final Bank bank;
    private String id;
    private Account source;
    private Account destination;
    private Currency currency;
    private Double amount;
    private String description;
    private Status status;
    private String rejectionDescription;
    private LocalDateTime timestamp;

    public Transaction(Bank b) {
        // Default constructor - set initial finals and rest is set after.
        this.bank = b;
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
    }

    public Transaction(Bank bank, Account source, Account destination, Currency currency, Double amount, String description) {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.rejectionDescription = "";
        this.bank = bank;

        setSource(source);
        setDestination(destination);
        setCurrency(currency);
        setAmount(amount);
        setDescription(description);

        // Status of transaction is defined after parsing of previous parameters.
    }
    // For load from datastore
    public Transaction(Bank b, String id, Account source, Account dest, Currency c, Double amount, String desc, Status status, String rej_desc, LocalDateTime time) {
        this.bank = b;
        setId(id);
        setSource(source);
        setDestination(dest);
        setCurrency(c);
        setAmount(amount);
        setDescription(desc);
        setRejectionDescription(rej_desc);
        setStatus(status);
        setTime(time);
    }

    // For seed transaction
    public Transaction(Bank bank, Account destination, Currency currency, Double amount, String description) {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.rejectionDescription = "";
        this.bank = bank;

        setSource(null);
        setDestination(destination);
        setCurrency(currency);
        setAmount(amount);
        setDescription(description);
    }

    public Transaction setTime(LocalDateTime time) {
        if (Objects.equals(time, this.timestamp)) {
            return this;
        }
        final Account oldSource = this.source;
        this.timestamp = time;
        this.firePropertyChange(PROPERTY_TIMESTAMP, oldSource, timestamp);
        return this;
    }

    public Transaction setId(String id) {
        if (Objects.equals(id, this.id)) {
            return this;
        }
        final Account oldSource = this.source;
        this.id = id;
        this.firePropertyChange(PROPERTY_ID, oldSource, id);
        return this;
    }
    // shallow copy constructor
    public Transaction(Transaction transaction) {
        this.bank = transaction.bank;
        this.id = transaction.id;
        this.source = transaction.source;
        this.destination = transaction.destination;
        this.currency = transaction.currency;
        this.amount = transaction.amount;
        this.description = transaction.description;
        this.status = transaction.status;
        this.rejectionDescription = transaction.rejectionDescription;
        this.timestamp = transaction.timestamp;
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

    public Double getAmount() {
        return amount;
    }

    public Transaction setAmount(Double amount) {
        if (Objects.equals(amount, this.amount)) {
            return this;
        }
        final Double oldAmount = this.amount;
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

    public Transaction execute() throws Bank.AccountDoesNotExistException, Bank.TransactionRestrictionException {
        Account sender = getBank().getAccountById(this.source.getId());

        if (sender == null) {
            this.status = Status.ABORTED;
            this.rejectionDescription = "Source account is invalid";
            return this;
        } else if (!Objects.equals(sender.getStatus(), "ACTIVE")) {
            throw new Bank.TransactionRestrictionException("Source account is frozen");
        } else {
            this.source = sender;
        }

        Account receiver = getBank().getAccountById(this.destination.getId());

        if (receiver == null) {
            this.status = Status.ABORTED;
            this.rejectionDescription = "Destination account is invalid";
            return this;
        } else if (!Objects.equals(receiver.getStatus(), "ACTIVE")) {
            throw new Bank.TransactionRestrictionException("Destination account is frozen");
        } else {
            this.destination = receiver;
        }

        Double sourceBalance = this.source.getBalance();
        if (sourceBalance < this.amount) {
            this.status = Status.ABORTED;
            this.rejectionDescription = "Not enough money on the source account";
            return this;
        }

        // Currency validation
        if (this.currency != this.destination.getCurrency() || this.currency != this.source.getCurrency()) {
            this.status = Status.ABORTED;
            this.rejectionDescription = "Currency mismatch detected";
            return this;
        }

        Double newSourceBalance = this.source.getBalance() - amount;
        this.source.setBalance(newSourceBalance);

        Double newDestBalance = this.destination.getBalance() + amount;
        this.destination.setBalance(newDestBalance);

        this.status = Status.EXECUTED;
        // Only after everything is executed we will add the transaction to the account transaction list.
        this.source.addSentTransaction(this);
        this.destination.addReceivedTransaction(this);

        this.source.addCount();
        if (this.source.getCount() == 3) {
            this.source.setBalance(this.source.getBalance() + 5.0);
        }
        return this;
    }

    public Transaction seed() throws Bank.AccountDoesNotExistException {
        this.source = null;

        Account receiver = getBank().getAccountById(getDestination().getId());

        if (receiver == null) {
            this.status = Status.ABORTED;
            this.rejectionDescription = "Destination account is invalid";
            return this;
        } else {
            this.destination = receiver;
        }

        // Currency validation
        if (this.currency != this.destination.getCurrency()) {
            this.status = Status.ABORTED;
            this.rejectionDescription = "Currency mismatch detected";
            return this;
        }

        Double newDestBalance = this.destination.getBalance() + amount;
        this.destination.setBalance(newDestBalance);

        this.status = Status.EXECUTED;

        // Only after everything is executed we will add the transaction to the account transaction list.
        this.destination.addReceivedTransaction(this);

        return this;
    }

    public String saveToString() {
        String sourceAndDestination = "";
        if (getSource() == null) {
            sourceAndDestination = "," + getDestination().getId();
        } else if (getDestination() == null) {
            sourceAndDestination = getSource().getId() + ",";
        } else {
            sourceAndDestination = getSource().getId() + "," + getDestination().getId();
        }

        return String.join(",",
            getId(),
            sourceAndDestination,
            getCurrency().toString(),
            getAmount().toString(),
            getDescription(),
            getStatus().toString(),
            getRejectionDescription(),
            getTimestamp().toString()
        );
    }

    public Transaction revoke(String reason) throws Bank.TransactionCanNotBeRevoked {
        if (this.status == Status.ABORTED || this.status == Status.REVOKED) {
            throw new Bank.TransactionCanNotBeRevoked("Denied to revoke, transaction status is not EXECUTED");
        }

        Double newSourceBalance = this.source.getBalance() + amount;
        if (this.source != null) {
            this.source.setBalance(newSourceBalance);
        }

        Double newDestBalance = this.destination.getBalance() - amount;
        if (this.destination != null) {
            this.destination.setBalance(newDestBalance);
        }

        this.status = Status.REVOKED;
        this.rejectionDescription = reason;

        return this;
    }
}
