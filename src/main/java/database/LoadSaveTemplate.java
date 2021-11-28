package database;

import model.Account;
import model.Bank;
import model.Transaction;
import model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class LoadSaveTemplate implements LoadSaveStrategy {
    private String path;
    public File file;
    public ArrayList<Transaction> transactions;
    public ArrayList<User> users;
    public ArrayList<Account> accounts;
    public HashMap<String, Transaction> transactionMap;
    public HashMap<String, User> userHashMap;
    public HashMap<String, Account> accountHashMap;

    public abstract ArrayList loadWithList(Bank b);

    public abstract void saveWithList(ArrayList transactions);

    public abstract void saveWithMap(HashMap transactions);

    LoadSaveTemplate() {
        this.path = "src/main/java/files/";

        transactions = new ArrayList<>();
        transactionMap = new HashMap<>();
        users = new ArrayList<>();
        userHashMap = new HashMap<>();
        accounts = new ArrayList<>();
        accountHashMap = new HashMap<>();
    }

    LoadSaveTemplate(String path) {
        if (path == null | path.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty path given");
        }
        this.path = path;
        transactionMap = new HashMap<>();
        transactions = new ArrayList<>();
    }

    String getPath() {
        return path;
    }
}
