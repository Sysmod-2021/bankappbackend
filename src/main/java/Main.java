public class Main {
    public static void main(String[] args) {
        try {
            Bank bank = new Bank();
            Administrator a = bank.createAdministrator("admin", "admin", "a@min.ee", "t");

            System.out.println(bank.getBankAccount().getBalance());
    
            a.createSeedTransactionToBank(5000.00, Currency.EUR, "Seed");
            System.out.println(bank.getTraces());
            System.out.println(bank.getBankAccount().getBalance());

            Customer customer; Customer customer2;

            customer = bank.createCustomer("John", "Doe", "john@doe.ee", "pass1234", 100.0, Currency.EUR);
            customer2 = bank.createCustomer("Bob", "Jackson", "bobby@tt.ee", "secret", 0.0, Currency.EUR);

            a.createTransactionTwoCustomers(customer.getAccount().getId(), customer2.getAccount().getId(), 9.0, "trans1");

            System.out.println(bank.getTransactions());
    
            WebConnector.run(bank);
        }
        catch (Exception exception){
            System.out.println(exception);
        }
    }
}
