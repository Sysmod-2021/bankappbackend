import model.Administrator;
import model.Bank;
import web.WebConnector;

public class Main {
    public static void main(String[] args) {
        try {
            Bank bank = new Bank();
            Administrator a = bank.createAdministrator("admin", "admin", "a@min.ee", "t");
//            System.out.println(bank.getCustomers());
//            for (Account acc : bank.getAccounts()
//                 ) {
//                System.out.println(a.saveToString());
//            }
//            System.out.println();
//
//            System.out.println(bank.getBankAccount().getBalance());
//
//            a.createSeedTransactionToBank(5000.00, Currency.EUR, "Seed");
//            System.out.println(bank.getTraces());
//            System.out.println(bank.getBankAccount().getBalance());
//
//            Customer customer; Customer customer2;
//
//            customer = bank.createCustomer("John", "Doe", "5euro@test.ee", "pass1234", 100.0, Currency.EUR);
//            customer2 = bank.createCustomer("Bob", "Jackson", "10euro@test.ee", "secret", 0.0, Currency.EUR);
//            bank.saveData();
//            System.out.println(bank.getTransactions());

//            TimerTask task = new TimerTask() {
//                @Override
//                public void run() {
//                    // 1 Hour has passed
//                    try {
//                        a.createTransactionTwoCustomers(customer.getAccount().getId(), customer2.getAccount().getId(), 10.0, "trans3");
//                        bank.saveData();
//                    } catch (Bank.AccountDoesNotExistException | Bank.TransactionRestrictionException e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//            Timer timer = new Timer();
//            long delay = 0;
//            long intervalPeriod = 15 * 1000;
//            // schedules the task to be run in an interval
//            timer.scheduleAtFixedRate(task, delay,
//                    intervalPeriod);

            WebConnector.run(bank);
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }
}
