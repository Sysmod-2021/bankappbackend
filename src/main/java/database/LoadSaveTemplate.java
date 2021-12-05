package database;

import model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class LoadSaveTemplate implements LoadSaveStrategy {
    private String path;
    private File customerFile;
    private File transactionFile;
    private File accountFile;
    public ArrayList<Transaction> transactions;
    public ArrayList<Customer> users;
    public ArrayList<Account> accounts;
    public HashMap<String, Transaction> transactionMap;
    public HashMap<String, Customer> userHashMap;
    public HashMap<String, Account> accountHashMap;

    LoadSaveTemplate() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            this.path = "src\\main\\java\\files";
        } else {
            this.path = "src/main/java/files";
        }

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
