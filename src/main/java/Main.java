import model.*;

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Administrator a = new Administrator(bank, "admin", "admin", "a@min.ee", "t");
        Customer c1 = new Customer(bank, "Jaak", "Tepandi", "j1.t1@ut.ee", "123451", "0000");
        Customer c2 = new Customer(bank, "Jaak2", "Tepandi2", "j2.t2@ut.ee", "123456", "0001");
        Account acc1 = new Account(c1.getBank(), c1, Currency.EUR, 500.00);
        Account acc2 = new Account(c2.getBank(), c2, Currency.EUR, 500.00);
//        a.createSeedTransactionToBank(5000.00, Currency.EUR, "Seed");
//        a.createTransactionTwoCustomers(acc1.getId(), acc2.getId(), 50.0, "Test");
//        System.out.println(bank.getCustomers());
        bank.saveData();

        Customer c3 = new Customer(bank, "Jaak3", "Tepandi3", "j3.t3@ut.ee", "123451", "0000");
        Account acc3 = new Account(c3.getBank(), c3, Currency.EUR, 501.00);
        bank.saveData();
        System.out.println(bank.getTraces());
        System.out.println(bank.getBankAccount().getBalance());

//        WebConnector.run(bank);
    }
}
