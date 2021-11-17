import java.util.ArrayList;
import java.util.List;

public class Bank {
    private final List<Customer> customers;
    private final List<Administrator> admins;
    private final List<Transaction> transactions;
    private final List<Account> accounts;
    private final List<Trace> traces;

    public Bank() {
        this.customers = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.accounts = new ArrayList<>();
        this.traces = new ArrayList<>();
    }
}
