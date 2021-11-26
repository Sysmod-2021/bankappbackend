package database;

import model.Bank;
import model.Transaction;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class LoadSaveTemplate implements LoadSaveStrategy {
    private String path;
    public File file;
    public ArrayList<Transaction> transactions;
    public HashMap<String, Transaction> transactionMap;

    public abstract ArrayList<Transaction> loadWithList(Bank b);

    public abstract HashMap<String, Transaction> loadWithMap(Bank b);

    public abstract void saveWithList(ArrayList<Transaction> transactions);

    public abstract void saveWithMap(HashMap<String, Transaction> transactions);

    LoadSaveTemplate() {
        this.path = "src/main/java/files/";

        transactions = new ArrayList<>();
        transactionMap = new HashMap<>();
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
