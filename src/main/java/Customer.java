public class Customer extends User {
    public Customer(Bank bank, String firstName, String lastName, String email, String password) {
        super(bank, firstName, lastName, email, password);
    }

    public Account getAccount() {
        return getBank().getAccountByEmail(getEmail());
    }
}
