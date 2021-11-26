package database;

import model.Transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public interface TransactionDbStrategy {


    void save();

    void add(Transaction t);

    void add(Collection<Transaction> transactions);

    Transaction getTransaction(String transactionID);

    ArrayList<Transaction> getAll();

    HashMap<String, Transaction> returnDb();

    String toString();

}
