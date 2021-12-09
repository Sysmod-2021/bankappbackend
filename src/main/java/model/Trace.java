package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class Trace {
    private final Bank bank;
    private final String id;
    private final Transaction transaction;
    private final User initiator;
    private final LocalDateTime timestamp;

    public Trace(Bank bank, Transaction transaction, User initiator, LocalDateTime timestamp) {
        this.bank = bank;
        this.id = UUID.randomUUID().toString();
        this.transaction = transaction;
        this.initiator = initiator;
        this.timestamp = timestamp;
    }

    public String getTraceString() {
        String result = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        result += String.format("Time: %s\n", timestamp.format(formatter));
        result += String.format("Transaction Type: %s\n", Transaction.Type.CONSOLE);
        result += String.format("Initiator: %s\n", initiator.getId());
        result += String.format("Transaction Status: %s\n", transaction.getStatus());

        if (!Objects.equals(transaction.getRejectionDescription(), "")) {
            result += String.format("Rejection Reason: %s\n", transaction.getRejectionDescription());
        }

        result += String.format("Bank ID: %s\n", bank.getBankAccount());
        result += String.format("Transaction ID: %s\n", transaction.getId());
        result += String.format("Trace Reference ID: %s\n", id);
        result += "-".repeat(24) + "\n";

        return result;
    }
}
