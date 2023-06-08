package org.example;

import java.awt.datatransfer.FlavorTable;
import java.util.ArrayList;
import java.util.Random;

public class Bank {
    private String name;
    private ArrayList<User> users;
    private ArrayList<Account> accounts;

    public Bank(String name) {
        this.name = name;
        this.users = new ArrayList<User>();
        this.accounts = new ArrayList<Account>();
    }

    public String getNewUserUUID() {
        String uuid;
        Random rng = new Random();
        int len = 2;
        boolean nonUnique;

        do {

            uuid = "";
            for (int c = 0; c < len; c++) {
                uuid += ((Integer) rng.nextInt(10)).toString();
            }
            nonUnique = false;
            for (User u : this.users) {
                if (uuid.compareTo(u.getUUID()) == 0) {
                    nonUnique = true;
                    break;
                }
            }
        } while (nonUnique);
        return uuid;
    }

    public String getNewAccountUUID() {
        String uuid;
        Random rng = new Random();
        int len = 10;
        boolean nonUnique;

        do {

            uuid = "";
            for (int c = 0; c < len; c++) {
                uuid += ((Integer) rng.nextInt(10)).toString();
            }
            nonUnique = false;
            for (Account a : this.accounts) {
                if (uuid.compareTo(a.getUUID()) == 0) {
                    nonUnique = true;
                    break;
                }
            }
        } while (nonUnique);
        return uuid;
    }


    public User addUser(String firstName, String lastName, String pin) {
        User newUser = new User(firstName, lastName, pin, this); // "this" refer to the whole class?
        this.users.add(newUser);

        //create a savings
        Account newAccount = new Account("Saving", newUser, this);
        newUser.addAccount(newAccount); // class user ngambil account terus di add pake getter
        this.accounts.add(newAccount); // kenapa disini ga usah ada getter.

        return newUser;

    }

    public User userLogin(String userID, String pin) {
        for (User u : this.users) {
            if (u.getUUID().compareTo(userID) == 0 && u.validatePin(pin)) {
                return u;
            }
        }
        return null;
    }

    public void addAccount(Account anAcct) {
        this.accounts.add(anAcct);
    }


    public Object getName() {
        return this.name;
    }
}

