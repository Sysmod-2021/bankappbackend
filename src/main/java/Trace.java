import java.time.LocalDateTime;
import java.util.UUID;

public class Trace {
    private final Bank bank;
    private final String id;
    private final Transaction transaction;
    private final User user;
    private final LocalDateTime timestamp;

    public Trace(Bank bank, Transaction transaction, User user, LocalDateTime timestamp) {
        this.bank = bank;
        this.id = UUID.randomUUID().toString();
        this.transaction = transaction;
        this.user = user;
        this.timestamp = timestamp;
    }
}
