package database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Bank;
import model.Transaction;

public interface LoadSaveStrategy {
    ArrayList transactionLoadWithList(Bank b);
    ArrayList customerLoadWithList(Bank b);
    ArrayList accountLoadWithList(Bank b);

    void saveWithList(ArrayList transactions);
    void saveCustomersWithList(List customers);
    void saveAccountsWithList(List accounts);
    void saveTransactionsWithList(List transactions);
}
