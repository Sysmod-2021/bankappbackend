package database;

import model.Account;
import model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public interface AccountDbStrategy {

    void save();

    void add(Account a);

    void add(Collection<Account> accounts);

    Account getAccount(String accountId);

    ArrayList<Account> getAll();

    HashMap<String, Account> returnAccountDb();

    String toString();
}
