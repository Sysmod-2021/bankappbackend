package database;

import model.Bank;
import model.Transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Datastore {
    private final String path;
    private TransactionDbInMemory transactionDB;

    public Datastore(String path, Bank b) {
        this.path = path;
        System.out.println(path);
        this.transactionDB = new TransactionDbInMemory(b);
    }

    public void save() {
        transactionDB.save();
    }

    public void add(Transaction t) {
        transactionDB.add(t);
    }
    public void add(Collection<Transaction> transactions) {
        transactionDB.add(transactions);
    }

    public Transaction getTransaction(String transactionID) {
        return transactionDB.getTransaction(transactionID);
    }

    public ArrayList<Transaction> getAll() {
        return transactionDB.getAll();
    }

    public HashMap<String, Transaction> returnDb() {
        return returnDb();
    }

    public String toString() {
        return transactionDB.toString();
    }
}
