package database;

import java.util.ArrayList;
import java.util.HashMap;

import model.Bank;
import model.Transaction;

public interface LoadSaveStrategy {
    ArrayList loadWithList(Bank b);

    void saveWithList(ArrayList transactions);
    void saveWithMap(HashMap transactions);
}
