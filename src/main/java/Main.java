public class Main {
    public static void main(String[] args) throws Bank.AccountDoesNotExistException {
        Bank bank = new Bank();
        Administrator a = new Administrator(bank, "admin", "admin", "a@min.ee", "t");
        System.out.println(bank.getBankAccount().getBalance());

        a.createSeedTransactionToBank(5000.00, Currency.EUR, "Seed");
        System.out.println(bank.getTraces());
        System.out.println(bank.getBankAccount().getBalance());

        WebConnector.run(bank);
    }
}
