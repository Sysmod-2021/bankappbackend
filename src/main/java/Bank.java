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

    public List<Customer> getCustomers()
    {
       return this.customers;
    }

    public List<Account> getAccounts() {
        return this.accounts;
    }

    public Bank addTrace(Trace trace) {
        traces.add(trace);
        return this;
    }

    public Bank addTransaction(Transaction transaction) {
        transactions.add(transaction);
        return this;
    }

    public Account getBankAccountById(String id) {
        Account acc = this.getAccounts().stream()
            .filter(account -> id.equals(account.getId()))
            .findFirst()
            .orElse(null);
        
        return acc;
    }
}
