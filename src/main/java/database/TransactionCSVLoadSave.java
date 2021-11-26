package database;

import model.Bank;
import model.Transaction;

import java.util.ArrayList;
import java.util.HashMap;

public class TransactionCSVLoadSave extends LoadSaveTemplate{


    @Override
    public HashMap<String, Transaction> loadWithMap(Bank b) {
        return null;
    }

    @Override
    public ArrayList<Transaction> loadWithList(Bank b) {
        return null;
    }

    @Override
    public void saveWithList(ArrayList<Transaction> transactions) {

    }

    @Override
    public void saveWithMap(HashMap<String, Transaction> transactions) {

    }
}
