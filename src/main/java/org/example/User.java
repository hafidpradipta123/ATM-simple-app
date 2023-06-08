package org.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User {
    private String firstName;
    private String lastName;
    private String uuid;
    private byte pinHash[];
    private ArrayList<Account> accounts;

    public User( String firstName, String lastName, String pin, Bank theBank){
        // set user's name
        this.firstName = firstName;
        this.lastName = lastName;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.pinHash = md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }

        // get UUID for user this one is using gettter
        this.uuid = theBank.getNewUserUUID();

        this.accounts = new ArrayList<Account>();

        // print log message
        System.out.printf("New user %s, %s with ID %s created.\n",
                lastName, firstName, this.uuid);



    }

    public void addAccount(Account anAcct) {
        // from class account add the (anAcct) the "add" belong to ArrayList
        // note that statement public class Account is the one that makes us able to get this.
        this.accounts.add(anAcct);
    }

    public String getUUID() {
        return this.uuid;
    }

    public boolean validatePin(String aPin) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(aPin.getBytes()), this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }
        return false;

    }

    public String getFirstName() {
        return this.firstName;
    }

    public void printAccountsSummary() {
        System.out.printf("\n\n%s's accounts summary\n", this.firstName);
        for (int a = 0; a< this.accounts.size(); a++){
            System.out.printf("(%d) %s \n", a+1,
                    this.accounts.get(a).getSummaryLine());

        }
        System.out.println();

    }

    public int numAccounts() {
        return this.accounts.size();
    }

    public void printAcctTransHistory(int acctIdx) {
        this.accounts.get(acctIdx).printTransHistory();

    }

    public double getAcctBalance(int acctIdx) {
        return this.accounts.get(acctIdx).getBalance();
    }

    public Object getActUUID(int acctIdx) {
        return this.accounts.get(acctIdx).getUUID();
    }

    public void addAcctTransaction(int acctIdx, double amount, String memo) {
        this.accounts.get(acctIdx).addTransaction(amount,memo);

    }
}
