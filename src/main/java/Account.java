import java.util.UUID;

public class Account {
    private final Bank bank;  // allows us to access the bank functionality, gives access to all the data
    private String id;
    private Customer customer;
    private Currency currency;
    private Float balance;

    public Account(Bank bank) {
        this.bank = bank;
    }

    public Account(Bank bank, Customer customer, Currency currency, Float balance) {
        this.id = UUID.randomUUID().toString();
        this.bank = bank;
        this.customer = customer;
        this.currency = currency;
        this.balance = balance;
    }

    public String getId() {
        return this.id;
    }

    public Account setId(String id) {
        this.id = id;
        return this;
    }

    public Float getBalance() {
        return this.balance;
    }

    public Account setBalance(Float newBalance) {
        this.balance = newBalance;
        return this;
    }
}
