package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/**
 * provides a set of methods for interacting with user through the console
 * methods: promptForBigDecimal, promptForInt, promptForString, promptForCredentials
 *          pause, printErrorMessage, printGreeting, printLoginMenu, printMainMenu
  */

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in); // instance of Scanner to read input from console

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine()); // parse input as integer
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }
    public void printUsers(List<User> userList) {
        System.out.println("-------------------------------------------");
        System.out.println("Users");
        System.out.println("ID\t\tName");
        System.out.println("-------------------------------------------");

        for (User user : userList) {
            System.out.println(user.getId() + "\t\t" + user.getUsername());
        }

        System.out.println("-------------------------------------------");
    }

    /**
     * Print the transfer history in a formatted table
     * @param transferHistory List of Transfer objects representing transfer history
     */
    public void printTransferHistory(Transfer[] transferHistory) {
        if (transferHistory != null && transferHistory.length > 0 ) {

            System.out.println("-------------------------------------------");
            System.out.println("Transfers");
            System.out.printf("%-10s%-23s%-10s%n", "ID", "From/To", "Amount");
            System.out.println("-------------------------------------------");

            for (Transfer transfer : transferHistory) {
                String fromTo = "From: " + transfer.getAccountFrom() + " To: " + transfer.getAccountTo();
                System.out.printf("%-10d%-23s$%-10.2f%n", transfer.getTransferId(), fromTo, transfer.getAmount());
            }

            System.out.println("---------");
        } else {
            System.out.println("Transfer array is empty.");
        }
    }

    public void printPendingRequests(List<TransferRequest> pendingRequests) {
        System.out.println("\n-------------------------------------------");
        System.out.println("Pending Transfers");
        System.out.printf("%-10s %-20s %-10s\n", "ID", "To", "Amount");
        System.out.println("-------------------------------------------");

        for (TransferRequest request : pendingRequests) {
            System.out.printf("%-10d %-20s $%-10.2f\n",
                    request.getRequestId(),
                    request.getReceiverUsername(),
                    request.getAmount());
        }

        System.out.println("-------------------------------------------");
        System.out.print("Please enter transfer ID to approve/reject (0 to cancel): ");
    }

    public void printTransferDetails (Transfer transfer){
        System.out.println("Transfer Id: " + transfer.getTransferId() + " Sender: "
                + transfer.getAccountFrom() + " Recipient: " + transfer.getAccountTo() +
                "Amount: " + transfer.getAmount());
    }

    public int promptForRecipientId(List<User> userList) {
        //printUsers(userList); this might be the problem
        return promptForInt("Enter ID of user you are sending to (0 to cancel): ");
    }

    public int promptForTransferId() {
        return promptForInt("Please enter transfer ID to view details (0 to cancel): ");
    }

    public BigDecimal promptForTransferAmount() {
        return promptForBigDecimal("Enter amount: ");
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    } // pause program by prompting user to hit enter and waiting for response

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }


}
