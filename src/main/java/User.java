import java.util.UUID;

public class User {
    private final Bank bank;
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;

    public User(Bank bank, String firstName, String lastName, String email, String password) {
        this.bank = bank;
        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Bank getBank() {
        return this.bank;
    }
}
