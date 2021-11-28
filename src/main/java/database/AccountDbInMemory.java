package database;

import model.Account;
import model.Bank;
import model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class AccountDbInMemory implements AccountDbStrategy {

    private HashMap<String, Account> accountHashMap;
    LoadSaveStrategy s;

    public AccountDbInMemory(Bank b) {
        s = LoadSaveFactory.fromType(LoadSaveStrategyEnum.TEXT);
        accountHashMap = new HashMap<>();
        add(s.loadWithList(b));
    }

    @Override
    public void save() {
        s.saveWithMap(accountHashMap);
    }

    @Override
    public void add(Account a) {
        if (a != null) {
            accountHashMap.put(a.getId(), a);
        } else {
            throw new IllegalArgumentException("Empty User");
        }
    }

    @Override
    public void add(Collection<Account> accounts) {
        if (accounts.size() > 0) {
            for (Account a : accounts) {
                add(a);
            }
        } else {
            System.out.println("No accounts found, starting blank");
        }
    }

    @Override
    public Account getAccount(String accountId) {
        return accountHashMap.get(accountId);
    }

    @Override
    public ArrayList<Account> getAll() {
        return new ArrayList<>(accountHashMap.values());
    }

    @Override
    public HashMap<String, Account> returnAccountDb() {
        return accountHashMap;
    }
}
