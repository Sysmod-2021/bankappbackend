package database;

import java.util.ArrayList;
import java.util.HashMap;

import model.Bank;
import model.Transaction;

public interface LoadSaveStrategy {

    HashMap<String, Transaction> loadWithMap(Bank b);
    ArrayList<Transaction> loadWithList(Bank b);

    void saveWithList(ArrayList<Transaction> transactions);
    void saveWithMap(HashMap<String,Transaction> transactions);
}
