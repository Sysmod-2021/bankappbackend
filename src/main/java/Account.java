import java.util.UUID;

public class Account {
    private final Bank bank;  // allows us to access the bank functionality, gives access to all the data
    private final String id;
    private final Customer customer;
    private final Currency currency;
    private Float balance;

    public Account(Bank bank, Customer customer, Currency currency, Float balance) {
        this.id = UUID.randomUUID().toString();
        this.bank = bank;
        this.customer = customer;
        this.currency = currency;
        this.balance = balance;
    }
}
