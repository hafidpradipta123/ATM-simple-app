package org.example;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Account {
    private String name;
    //private double balance;
    private String uuid;
    private User holder;
    private ArrayList<Transaction> transactions;

    public Account(String name, User holder, Bank theBank) {
        this.name = name;
        this.holder = holder;

        this.uuid = theBank.getNewAccountUUID();

        this.transactions = new ArrayList<Transaction>();

    }

    public String getUUID() {
        return this.uuid;
    }

    public String getSummaryLine() {
        // get the balance
        double balance = this.getBalance();

        // format the summary line if negative
        if (balance >= 0) {
            return String.format("%s: $%.02f :%s", this.uuid, balance, this.name);
        } else {
            return String.format("%s: $(%.02f) :%s", this.uuid, balance, this.name);
        }
    }

    public double getBalance() {
        double balance = 0;
        for (Transaction t : this.transactions) {
            balance += t.getAmount();

        }
        return balance;
    }

    public void printTransHistory() {
        System.out.printf("\nTransaction history for account %s\n", this.uuid);
        for (int t = this.transactions.size()-1; t>= 0; t--){
            System.out.println(this.transactions.get(t).getSummaryline() );

        }
        System.out.println();

    }

    public void addTransaction(double amount, String memo) {
        Transaction newTrans = new Transaction(amount, memo, this);
        this.transactions.add(newTrans);
    }
}