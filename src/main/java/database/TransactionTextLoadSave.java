package database;

import model.Account;
import model.Bank;
import model.Currency;
import model.Transaction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TransactionTextLoadSave extends LoadSaveTemplate{

    private final File file;
    public TransactionTextLoadSave() {
        super();
        this.file = new File(super.getPath()+"transactions.txt");
    }

    public TransactionTextLoadSave(String path) {
        super();
        this.file = new File(path);
    }

    @Override
    public ArrayList<Transaction> loadWithList(Bank bank) {
        File toRead = new File(getPath()+"transactions.txt");
        try {
            Scanner sc = new Scanner(toRead);
            while (sc.hasNextLine()) {
                Scanner lineScanner = new Scanner(sc.nextLine().trim());
                lineScanner.useDelimiter(",");
                String transActionId = lineScanner.next();
                Account sourceId = bank.getAccountsMap().get(lineScanner.next());
                Account receiverId = bank.getAccountsMap().get(lineScanner.next());
                Currency currency = model.Currency.valueOf(lineScanner.next());
                Double amount = Double.parseDouble(lineScanner.next());
                String description = lineScanner.next();
                Transaction.Status status = Transaction.Status.valueOf(lineScanner.next());
                String rej_desc = lineScanner.next();
                LocalDateTime timestamp = LocalDateTime.parse(lineScanner.next());

                Transaction t = new Transaction(bank, transActionId, sourceId, receiverId, currency, amount, description, status, rej_desc, timestamp);
                transactions.add(t);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public void saveWithList(ArrayList transactions) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            StringBuilder data = new StringBuilder();

            for (Object t : transactions) {
                Transaction tt = (Transaction) t;
                data.append(tt.saveToString()).append("\n");
            }
            fileWriter.write(data.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void saveWithMap(HashMap transactions) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            StringBuilder data = new StringBuilder();

            for (Object t : transactions.values()) {
                Transaction tt = (Transaction) t;
                data.append(tt.saveToString()).append("\n");
            }
            fileWriter.write(data.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
