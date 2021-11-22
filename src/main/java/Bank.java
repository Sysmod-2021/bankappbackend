import java.util.*;

public class Bank {
    private final List<Customer> customers;
    private final Map<String,Customer> customerMap;
    private final List<Administrator> admins;
    private final List<Transaction> transactions;
    private final List<Account> accounts;
    private final Map<String,Account> accountsMap;
    private final List<Trace> traces;
    private final Account bankAccount;

    public Bank() {
        this.customers = new ArrayList<>();
        this.admins = new ArrayList<>();

        this.transactions = new ArrayList<>();
        this.accounts = new ArrayList<>();
        this.traces = new ArrayList<>();

        this.customerMap = new HashMap<>();
        this.accountsMap = new HashMap<>();

        bankAccount = new Account(this);
        bankAccount.setBalance(69420.00F);

    }

    public Account getBankAccount() {
        return this.bankAccount;
    }

    public List<Customer> getCustomers()
    {
       return this.customers;
    }
    public Map<String,Customer> getCustomerMap()
    {
       return this.customerMap;
    }

    public Map<String,Account> getAccountsMap()
    {
        return this.accountsMap;
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
        Account account = this.getAccountsMap().get(id);

//        Account acc = this.getAccounts().stream()
//            .filter(account -> id.equals(account.getId()))
//            .findFirst()
//            .orElse(null);
        
        return account;
    }

	public List<Administrator> getAdmins() {
		return this.admins;
	}
}
