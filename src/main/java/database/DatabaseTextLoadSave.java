package database;

import model.*;
import model.Currency;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class DatabaseTextLoadSave extends LoadSaveTemplate {

    private final File customerFile;
    private final File accountFile;
    private final File transactionFile;

    private String getPathDelimiter() {
        String pathDelimiter = "";

        if (System.getProperty("os.name").startsWith("Windows")) {
            pathDelimiter = "\\";
        } else {
            pathDelimiter = "/";
        }

        return pathDelimiter;
    }

    public DatabaseTextLoadSave() {
        super();
        String pathDelimiter = getPathDelimiter();
        this.customerFile = new File(super.getPath() + pathDelimiter + "customers.txt");
        this.accountFile = new File(super.getPath() + pathDelimiter + "accounts.txt");
        this.transactionFile = new File(super.getPath() + pathDelimiter + "transactions.txt");
    }

    public DatabaseTextLoadSave(String path) {
        super();
        this.customerFile = new File(path);
        this.transactionFile = new File(path);
        this.accountFile = new File(path);
    }

    @Override
    public ArrayList transactionLoadWithList(Bank b) {
        String pathDelimiter = getPathDelimiter();
        File toRead = new File(getPath() + pathDelimiter + "transactions.txt");
        try {
            Scanner sc = new Scanner(toRead);
            while (sc.hasNextLine()) {
                Scanner lineScanner = new Scanner(sc.nextLine().trim());
                lineScanner.useDelimiter(",");
                String transActionId = lineScanner.next();
                Account sourceId = b.getAccountsMap().get(lineScanner.next());
                Account receiverId = b.getAccountsMap().get(lineScanner.next());
                Currency currency = model.Currency.valueOf(lineScanner.next());
                Double amount = Double.parseDouble(lineScanner.next());
                String description = lineScanner.next();
                Transaction.Status status = Transaction.Status.valueOf(lineScanner.next());
                String rej_desc = lineScanner.next();
                LocalDateTime timestamp = LocalDateTime.parse(lineScanner.next());

                Transaction t = new Transaction(b, transActionId, sourceId, receiverId, currency, amount, description, status, rej_desc, timestamp);
                transactions.add(t);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public ArrayList customerLoadWithList(Bank b) {
        String pathDelimiter = getPathDelimiter();
        File toRead = new File(getPath()+ pathDelimiter + "customers.txt");
        try {
            Scanner sc = new Scanner(toRead);;
            while (sc.hasNextLine()) {
                Scanner lineScanner = new Scanner(sc.nextLine().trim());
                lineScanner.useDelimiter(",");
                String accountId = lineScanner.next();
                String firstName = lineScanner.next();
                String lastName = lineScanner.next();
                String email = lineScanner.next();
                String password = lineScanner.next();

                Customer u = new Customer(b, accountId, firstName, lastName, email, password);
                users.add(u);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public ArrayList accountLoadWithList(Bank b) {
        String pathDelimiter = getPathDelimiter();
        File toRead = new File(getPath() + pathDelimiter + "accounts.txt");
        try {
            Scanner sc = new Scanner(toRead);
            while (sc.hasNextLine()) {
                System.out.println("newline");
                Scanner lineScanner = new Scanner(sc.nextLine().trim());
                lineScanner.useDelimiter(",");
                String accountId = lineScanner.next();
                String customerId = null;
                Currency currency = null;
                Double balance = null;
                try {
                    customerId = lineScanner.next();
                    currency = Currency.valueOf(lineScanner.next());
                    balance = Double.parseDouble(lineScanner.next());
                    Account a = new Account(b, accountId, customerId, currency, balance);
                    accounts.add(a);
                } catch (NullPointerException e) {
                    // nullpointer = bank's account. update existing banks account without customer param.
                    b.getBankAccount().setId(b.getBankAccount().getId());
                    b.getBankAccount().setBalance(balance);
                }



            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public void saveWithList(ArrayList users) {
        try {
            Writer fileWriter = new FileWriter(customerFile);
            StringBuilder data = new StringBuilder();

            for (Object u : users) {
                User uu = (User) u;
                data.append(uu.saveToString()).append("\n");
            }
            fileWriter.write(data.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveTransactionsWithList(List transactions) {
        try {
            Writer fileWriter = new FileWriter(transactionFile);
            StringBuilder data = new StringBuilder();

            for (Object t: transactions) {
                Transaction tt = (Transaction) t;
                data.append(tt.saveToString()).append("\n");
            }
            fileWriter.write(data.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void saveAccountsWithList(List accounts) {
        try {
            Writer fileWriter = new FileWriter(accountFile);
            StringBuilder data = new StringBuilder();

            for (Object a : accounts) {
                Account aa = (Account) a;
                data.append(aa.saveToString()).append("\n");
            }
            fileWriter.write(data.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void saveCustomersWithList(List customers) {
        try {
            Writer fileWriter = new FileWriter(customerFile);
            StringBuilder data = new StringBuilder();

            for (Object c : customers) {
                Customer cc = (Customer) c;
                System.out.println(cc.getId());
                System.out.println(cc.getFirstName());
                System.out.println(cc.getLastName());
                System.out.println(cc.getEmail());
                System.out.println(cc.getPassword());
                data.append(cc.saveToString()).append("\n");
            }
            fileWriter.write(data.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
