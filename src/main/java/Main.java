import model.*;

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Administrator a = new Administrator(bank, "admin", "admin", "a@min.ee", "t");
//        Customer c1 = new Customer(bank, "Jaak", "Tepandi", "j1.t1@ut.ee", "123451", "0000");
//        Customer c2 = new Customer(bank, "Jaak2", "Tepandi2", "j2.t2@ut.ee", "123456", "0001");
//        Account acc1 = new Account(c1.getBank(), c1, Currency.EUR, 500.00);
//        Account acc2 = new Account(c2.getBank(), c2, Currency.EUR, 500.00);
//        bank.getCustomers().add(c1);
//        bank.getCustomers().add(c2);
//        bank.getAccounts().add(acc1);
//        bank.getAccounts().add(acc2);
//        bank.getCustomerMap().put(c1.getId(),c1);
//        bank.getCustomerMap().put(c2.getId(),c2);
//        bank.getAccountsMap().put(acc1.getId(), acc1);
//        bank.getAccountsMap().put(acc2.getId(), acc2);
//        a.createSeedTransactionToBank(5000.00, Currency.EUR, "Seed");
//        a.createTransactionTwoCustomers(acc1.getId(), acc2.getId(), 50.0, "Test");
        System.out.println(bank.getCustomers());
        bank.saveData();
        System.out.println(bank.getTraces());
        System.out.println(bank.getBankAccount().getBalance());

//        WebConnector.run(bank);
    }
}
