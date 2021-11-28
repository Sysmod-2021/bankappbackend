package database;

import model.Bank;
import model.Customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class CustomerDbInMemory implements CustomerDbStrategy {
    private HashMap<String, Customer> CustomerHashMap;
    LoadSaveStrategy s;

    public CustomerDbInMemory(Bank b) {
        s = LoadSaveFactory.fromType(LoadSaveStrategyEnum.TEXT);
        CustomerHashMap = new HashMap<>();
        add(s.loadWithList(b));
    }

    @Override
    public void save() {
        s.saveWithMap(CustomerHashMap);
    }

    @Override
    public void add(Customer u) {
        if (u != null) {
            CustomerHashMap.put(u.getId(), u);
        } else {
            throw new IllegalArgumentException("Empty Customer");
        }
    }

    @Override
    public void add(Collection<Customer> Customers) {
        if (Customers.size() > 0) {
            for (Customer u : Customers) {
                add(u);
            }
        } else {
            System.out.println("No Customers found, starting blank");
        }
    }

    @Override
    public Customer getCustomer(String CustomerId) {
        return CustomerHashMap.get(CustomerId);
    }

    @Override
    public ArrayList<Customer> getAll() {
        return new ArrayList<>(CustomerHashMap.values());
    }

    @Override
    public HashMap<String, Customer> returnCustomerDb() {
        return CustomerHashMap;
    }
}
