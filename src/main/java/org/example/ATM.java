package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ATM {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Bank theBank = new Bank("Bank of hapit");

        // add a user
        User aUser = theBank.addUser("John", "Doe", "1234");


        // add a checking account for user
        Account newAccount = new Account ("Checking", aUser, theBank);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;
        while(true){
            curUser = ATM.mainMenuPrompt(theBank, sc);
            ATM.printUserMenu(curUser, sc);
        }


    }

    private static void printUserMenu(User theUser, Scanner sc) {
        theUser.printAccountsSummary();

        int choice;

        do {
            System.out.printf("Welcome %s, what would you like to do?\n", theUser.getFirstName());
            System.out.println("  1) Show account transaction histroy");
            System.out.println("  2) Withdraw");
            System.out.println("  3) Deposit");
            System.out.println("  4) Transfer");
            System.out.println("  5) Quit");
            System.out.println();
            System.out.print("Enter choice: ");
            // if non numeric go to this one

            try{
                choice = sc.nextInt();
            } catch(InputMismatchException e){
                System.out.println("Invalid. Please enter a number");
                sc.nextLine(); // Consume the invalid input
                choice = 0; // Set choice to an invalid value to repeat the loop

            }
            //if numeric outside go to this one

            if (choice <1 || choice > 5){
                System.out.println("Invalid. Please choose between 1 to 5");
            }


        } while (choice < 1 || choice > 5);
        switch (choice) {
            case 1:
                ATM.showTransHistory(theUser, sc);
                break;
            case 2:
                ATM.withdrawalFunds(theUser, sc);
                break;
            case 3:
                ATM.depositFunds(theUser, sc);
                break;
            case 4:
                ATM.transferFunds(theUser, sc);
                break;

        }

        if (choice != 5) {
            ATM.printUserMenu(theUser, sc);// recursion
        }

    }

    private static void depositFunds(User theUser, Scanner sc) {
        int toAcct;
        double amount;
        double acctBal;
        String memo;


        //get account to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account \n" +
                    "to transfer to deposit in: ", theUser.numAccounts());
            toAcct= sc.nextInt()-1;
            if(toAcct < 0 || toAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again");
            }
        } while(toAcct< 0 || toAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(toAcct);
        //get the amount transfer
        do {
            System.out.printf("Enter the amount to deposit : $", acctBal);
            amount = sc.nextDouble();
            if(amount <0){
                System.out.println("Amount must be greater than zero");
            }


        } while(amount <0);

        // gobble up the rest of previous input
        sc.nextLine();

        // get a memo

        System.out.print("Enter a memo: ");
        memo = sc.nextLine();

        // do the deposit
        theUser.addAcctTransaction(toAcct, amount, memo);

    }

    /**
     * Process a fund withdraw from an account
     * @param theUser   the logged in user object
     * @param sc        the Scanner object user for user input
     */
    private static void withdrawalFunds(User theUser, Scanner sc) {
        int fromAcct;
        double amount;
        double acctBal;
        String memo;


    //get account to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account \n" +
                    "to withdraw from: ", theUser.numAccounts());
            fromAcct= sc.nextInt()-1;
            if(fromAcct < 0 || fromAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again");
            }
        } while(fromAcct< 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);
        //get the amount transfer
        do {
            System.out.printf("Enter the amount to transfer (max $%.02f): $", acctBal);
            amount = sc.nextDouble();
            if(amount <0){
                System.out.println("Amount must be greater than zero");
            } else if(amount > acctBal){
                System.out.printf("Amount must not be greater than\n" +
                        "balance of $%.02f\n", acctBal);
            }
        } while(amount <0 || amount > acctBal);

        // gobble up the rest of previous input
        sc.nextLine();

        // get a memo

        System.out.print("Enter a memo: ");
        memo = sc.nextLine();

        // do the withdrawal
        theUser.addAcctTransaction(fromAcct, -1*amount, memo);



    }

    private static void transferFunds(User theUser, Scanner sc) {
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;
        do {
            System.out.printf("Enter the number (1-%d) of the account \n" +
                    "to transfer from: ", theUser.numAccounts());
            fromAcct= sc.nextInt()-1;
            if(fromAcct < 0 || fromAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again");
            }
        } while(fromAcct< 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);

        // get the account to transfer to
        do {
            System.out.printf("Enter the number (1-%d) of the account \n" +
                    "to transfer to: ", theUser.numAccounts());
            toAcct= sc.nextInt()-1;
            if(toAcct < 0 || toAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again");
            }
        } while(toAcct< 0 || toAcct >= theUser.numAccounts());

        // get the amount to transfer
        do {
            System.out.printf("Enter the amount to transfer (max $%.02f): $", acctBal);
            amount = sc.nextDouble();
            if(amount <0){
                System.out.println("Amount must be greater than zero");
            } else if(amount > acctBal){
                System.out.printf("Amount must not be greater than\n" +
                        "balance of $%.02f\n", acctBal);
            }
        } while(amount <0 || amount > acctBal);
        theUser.addAcctTransaction(fromAcct, -1*amount, String.format(
                "Transfer to account %s", theUser.getActUUID(toAcct)));

        theUser.addAcctTransaction(toAcct, amount, String.format(
                "Transfer to account %s", theUser.getActUUID(fromAcct)));

    }

    private static void showTransHistory(User theUser, Scanner sc) {
        int theAcct;

        do {
            System.out.printf("Enter the number between 1 and %d of the account\n" +
                            "whose transactions you want to see: ",
                    theUser.numAccounts());
            try {
                theAcct = sc.nextInt() - 1;
                if (theAcct < 0 || theAcct >= theUser.numAccounts()) {
                    System.out.println("Invalid account. Please try again");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numeric value.");
                sc.nextLine(); // Clear the input buffer
                theAcct = -1; // Assign an invalid value to theAcct to repeat the loop

            }
        }    while (theAcct < 0 || theAcct >= theUser.numAccounts()) ;
            theUser.printAcctTransHistory(theAcct);

    }

    private static User mainMenuPrompt(Bank theBank, Scanner sc) {
        String userID;
        String pin;
        User authUser;

        // prompot the user of ruser ID pin / combo untill a correct is reached
        do {
            System.out.printf("\n\n Welcome to %s\n\n", theBank.getName());
            System.out.print("Enter user ID: ");
            userID = sc.nextLine();
            System.out.print("Enter pin: ");
            pin = sc.nextLine();


            authUser = theBank.userLogin(userID, pin);
            if(authUser == null){
                System.out.println("Incorrect user ID / pin combination" + "Please try again");
            }


        } while(authUser == null);
        return authUser;
    }
}
