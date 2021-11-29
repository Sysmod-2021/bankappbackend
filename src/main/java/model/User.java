package model;

import java.beans.PropertyChangeSupport;
import java.util.Objects;
import java.util.UUID;

public abstract class User {

    public static final String PROPERTY_FNAME = "fname";
    public static final String PROPERTY_LNAME = "lname";
    public static final String PROPERTY_EMAIL = "email";
    public static final String PROPERTY_PASSWORD = "pword";
    public static final String PROPERTY_ID = "ID";

    protected PropertyChangeSupport listeners;

    private final Bank bank;
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public User(Bank bank, String firstName, String lastName, String email, String password) {
        this.bank = bank;
        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
    // for LoadSaveData
    public User(Bank bank,String id, String firstName, String lastName, String email, String password) {
        this.bank = bank;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String fname) {
        if (Objects.equals(fname, this.firstName)) {
            return this;
        }
        final String oldFName = this.firstName;
        this.firstName = fname;
        this.firePropertyChange(PROPERTY_FNAME, oldFName, fname);
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setId(String uid) {
        if (Objects.equals(uid, this.id)) {
            return this;
        }
        final String oldId = this.id;
        this.id = uid;
        this.firePropertyChange(PROPERTY_ID, oldId, uid);
        return this;
    }

    public User setLastName(String lname) {
        if (Objects.equals(lname, this.lastName)) {
            return this;
        }
        final String oldLName = this.lastName;
        this.lastName = lname;
        this.firePropertyChange(PROPERTY_LNAME, oldLName, lname);
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        if (Objects.equals(email, this.email)) {
            return this;
        }
        final String oldEmail = this.email;
        this.email = email;
        this.firePropertyChange(PROPERTY_EMAIL, oldEmail, email);
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        if (Objects.equals(password, this.password)) {
            return this;
        }
        final String oldPassword = this.password;
        this.password = password;
        this.firePropertyChange(PROPERTY_PASSWORD, oldPassword, password);
        return this;
    }

    public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
    {
        if (this.listeners != null)
        {
            this.listeners.firePropertyChange(propertyName, oldValue, newValue);
            return true;
        }
        return false;
    }

    public Bank getBank() {
        return this.bank;
    }
    public String saveToString() {
        String out = "";
        out += getId() + "," + getFirstName() + "," + getLastName() + "," + getEmail() + "," + getPassword();
        return out;
    }

}
