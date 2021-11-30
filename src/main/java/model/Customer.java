package model;

import exceptions.TransactionExceptions;

public class Customer extends User {
    public Customer(Bank bank,String id, String firstName, String lastName, String email, String password) {
        super(bank,id, firstName, lastName, email, password);
    }
    public Account getAccount() {
        return getBank().getAccountByEmail(getEmail());
    }

    public Transaction createTransaction(String receiverAccountId, Double amount, String description) throws Bank.AccountDoesNotExistException, TransactionExceptions.TransactionRestrictionException {

        Account receiverAccount = getBank().getAccountById(receiverAccountId);

        return getBank().performTransaction(this, this.getAccount(), receiverAccount, amount, description);
    }
}
