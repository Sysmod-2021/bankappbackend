public class Customer extends User {
    private final String personalCode;

    public Customer(Bank bank, String firstName, String lastName, String email, String password, String personalCode) {
        super(bank, firstName, lastName, email, password);
        this.personalCode = personalCode;
    }
}
