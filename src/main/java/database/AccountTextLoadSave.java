package database;

import model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class AccountTextLoadSave extends LoadSaveTemplate {

    private final File file;

    public AccountTextLoadSave() {
        super();
        this.file = new File(super.getPath()+"accounts.txt");
    }

    public AccountTextLoadSave(String path) {
        super();
        this.file = new File(path);
    }

    @Override
    public ArrayList loadWithList(Bank b) {
        File toRead = new File(getPath()+"accounts.txt");
        try {
            Scanner sc = new Scanner(toRead);
            while (sc.hasNextLine()) {
                Scanner lineScanner = new Scanner(sc.nextLine().trim());
                lineScanner.useDelimiter(",");
                String accountId = lineScanner.next();
                Customer customer = b.getCustomerMap().get(lineScanner.next());
                Currency currency = Currency.valueOf(lineScanner.next());
                Double balance = Double.parseDouble(lineScanner.next());

                Account a = new Account(b, accountId, customer, currency, balance);
                accounts.add(a);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public void saveWithList(ArrayList transactions) {

    }

    @Override
    public void saveWithMap(HashMap transactions) {

    }
}
