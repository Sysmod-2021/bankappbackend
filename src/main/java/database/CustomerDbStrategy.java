package database;

import model.Transaction;
import model.Customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public interface CustomerDbStrategy {

    void save();

    void add(Customer u);

    void add(Collection<Customer> Customers);

    Customer getCustomer(String CustomerId);

    ArrayList<Customer> getAll();

    HashMap<String, Customer> returnCustomerDb();

    String toString();
}
