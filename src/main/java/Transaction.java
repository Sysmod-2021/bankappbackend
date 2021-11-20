import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

public class Transaction {
    public enum Status {
        REVOKED,
        EXECUTED
    }

    private final Bank bank;
    private final String id;
    private final Account source;
    private final Account destination;
    private final Currency currency;
    private final Float amount;
    private final String description;
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

    public Account getDestination() {
        return destination;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Float getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public String getRejectionDescription() {
        return rejectionDescription;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setRejectionDescription(String rejDescription) {
        this.rejectionDescription = rejDescription;
    }

    public void setStatus(Status s) {
        this.status = s;
    }
}
