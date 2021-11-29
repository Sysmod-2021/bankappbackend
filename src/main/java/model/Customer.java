package model;

public class Customer extends User {
    public Customer(Bank bank,String id, String firstName, String lastName, String email, String password) {
        super(bank,id, firstName, lastName, email, password);
    }
    public Account getAccount() {
        return getBank().getAccountByEmail(getEmail());
    }
}
