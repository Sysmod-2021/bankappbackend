package database;

import model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class CustomerTextLoadSave extends LoadSaveTemplate {

    private final File file;

    public CustomerTextLoadSave() {
        super();
        this.file = new File(super.getPath()+"customers.txt");
    }

    public CustomerTextLoadSave(String path) {
        super();
        this.file = new File(path);
    }
    @Override
    public ArrayList loadWithList(Bank b) {
        File toRead = new File(getPath()+"customers.txt");
        try {
            Scanner sc = new Scanner(toRead);
            while (sc.hasNextLine()) {
                Scanner lineScanner = new Scanner(sc.nextLine().trim());
                lineScanner.useDelimiter(",");
                String accountId = lineScanner.next();
                String firstName = lineScanner.next();
                String lastName = lineScanner.next();
                String email = lineScanner.next();
                String password = lineScanner.next();

                User u = new User(b, accountId, firstName, lastName, email, password);
                users.add(u);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void saveWithList(ArrayList users) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            StringBuilder data = new StringBuilder();

            for (Object u : users) {
                User uu = (User) u;
                data.append(uu.saveToString()).append("\n");
            }
            fileWriter.write(data.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveWithMap(HashMap users) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            StringBuilder data = new StringBuilder();

            for (Object u : users.values()) {
                User uu = (User) u;
                data.append(uu.saveToString()).append("\n");
            }
            fileWriter.write(data.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
