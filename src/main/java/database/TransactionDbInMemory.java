package database;

import model.Bank;
import model.Transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TransactionDbInMemory implements TransactionDbStrategy {
    private HashMap<String, Transaction> transactionHashMap;
    LoadSaveStrategy s;

    TransactionDbInMemory (Bank b) {
        s = LoadSaveFactory.fromType(LoadSaveStrategyEnum.TEXT);
        transactionHashMap = new HashMap<>();
        add(s.loadWithList(b));
    }

    @Override
    public void save() {
        s.saveWithMap(transactionHashMap);
    }

    @Override
    public void add(Transaction t) {
        if (t != null) {
            transactionHashMap.put(t.getId(), t);
        } else {
            throw new IllegalArgumentException("Empty transaction");
        }
    }

    @Override
    public void add(Collection<Transaction> transactions) {
        if (transactions.size() > 0) {
            for (Transaction t : transactions) {
                add(t);
            }
        } else {
            System.out.println("No transactions found, starting blank");
        }
    }

    @Override
    public Transaction getTransaction(String transactionID) {
        return transactionHashMap.get(transactionID);
    }

    @Override
    public ArrayList<Transaction> getAll() {
        return new ArrayList<>(transactionHashMap.values());
    }

    @Override
    public HashMap<String, Transaction> returnDb() {
        return transactionHashMap;
    }
}
