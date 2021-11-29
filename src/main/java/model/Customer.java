package model;

public class Customer extends User {
    public Customer(Bank bank, String firstName, String lastName, String email, String password, String personalCode) {
        super(bank, firstName, lastName, email, password);
        bank.getCustomers().add(this);
        bank.getCustomerMap().put(this.getId(), this);
    }
    public Account getAccount() {
        return getBank().getAccountByEmail(getEmail());
    }
}
