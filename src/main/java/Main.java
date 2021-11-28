import model.*;

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Administrator a = new Administrator(bank, "admin", "admin", "a@min.ee", "t");
        System.out.println(bank.getBankAccount().getBalance());
        System.out.println(bank.getTransActions().get(0).getDescription());
        Customer c1 = new Customer(bank, "Jaak", "Tepandi", "j.t@ut.ee", "123456", "0000");
        Customer c2 = new Customer(bank, "Jaak2", "Tepandi2", "j2.t2@ut.ee", "123456", "0001");
        Account acc1 = new Account(bank, c1, Currency.EUR, 500.00);
        Account acc2 = new Account(bank, c2, Currency.EUR, 500.00);
        bank.getAccountsMap().put(acc1.getId(), acc1);
        bank.getAccountsMap().put(acc2.getId(), acc2);
//        a.createSeedTransactionToBank(5000.00, Currency.EUR, "Seed");
//        a.createTransactionTwoCustomers(acc1.getId(), acc2.getId(), 50.0, "Test");
//        bank.saveData();
        System.out.println(bank.getTraces());
        System.out.println(bank.getBankAccount().getBalance());

//        WebConnector.run(bank);
    }
}
