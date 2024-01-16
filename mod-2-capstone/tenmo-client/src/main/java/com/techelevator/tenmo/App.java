package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.UserService;
import com.techelevator.util.BasicLogger;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    // create instances of ConsoleService, AuthenticationService, & variable 'currentUser' to store information about authenticated user
    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);



    private AuthenticatedUser currentUser;
    //private UserService userService;
    private UserService userService;
    private Transfer transfer;



    public static void main(String[] args) {
        // create instance of App and call its run() method
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();     // print greeting
        loginMenu();                        // call loginMenu()
        if (currentUser != null) {          // if user isn't null,
            userService = new UserService(API_BASE_URL, currentUser);
            mainMenu();                     // proceed to mainMenu()
        }
    }
    // menu for user login and registration, allow users to interact with authentication system
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {                       // 1 == handleRegister()
                handleRegister();
            } else if (menuSelection == 2) {                // 2 == handleLogin()
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    // handle the registration process by prompting user for credentials & attempt to register using AuthenticationService
    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
            BasicLogger.log("Registration successful.");
        } else {
            consoleService.printErrorMessage();
            BasicLogger.log("Registration failed.");
        }
    }

    // handle login process by prompting user for credentials & attempting to log in using AuthenticationService
    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
            BasicLogger.log("Login failed.");
        }
    }

    // main menu options after successful login
    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {           // 1 == viewCurrentBalance()
                viewCurrentBalance(currentUser);
            } else if (menuSelection == 2) {    // 2 == viewTransferHistory()
                viewTransferHistory();
            } else if (menuSelection == 3) {    // 3 == viewPendingRequests()
                viewPendingRequests();
            } else if (menuSelection == 4) {    // 4 == sendBucks()
                sendBucks(currentUser);
            } else if (menuSelection == 5) {    // 5 == requestBucks()
                System.out.println("Option not available at this time.");
            } else if (menuSelection == 0) {    // 0 == continue
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }
    //As an authenticated user of the system, I need to be able to see my Account Balance.
	private void viewCurrentBalance(AuthenticatedUser authenticatedUser) {
        UserService userService = new UserService(API_BASE_URL, currentUser);
        User user = authenticatedUser.getUser();
        int id = user.getId();
        		try {
        			userService.getCurrentBalance(id);
        		} catch (NullPointerException e) {
        			System.out.println("No balance found");
                    BasicLogger.log("No balance found");
        		}



     }
        //As an authenticated user of the system, I need to be able to see transfers I have sent or received.
	private void viewTransferHistory() {
        if (currentUser != null) {
            Transfer[] transferHistory = userService.getTransferHistory(currentUser);
            if (transferHistory != null) {
                consoleService.printTransferHistory(transferHistory);
                int transferId = consoleService.promptForTransferId();
                viewTransfer(transferId);
            } else {
                System.out.println("No transfer history available.");
                BasicLogger.log("No transfer history available.");
            }
        } else {
            System.out.println("User is not logged in.");
            BasicLogger.log("User is not logged in.");
        }

    }

    private void viewTransfer(int transferId){
        Transfer transfer = userService.getTransferDetails(transferId);
        consoleService.printTransferDetails(transfer);

    }

    //optional #8
	private void viewPendingRequests() {
		if (currentUser != null) {
            try {
            List<TransferRequest> pendingRequests = userService.getPendingRequests(currentUser);
            if (!pendingRequests.isEmpty()) {
                consoleService.printPendingRequests(pendingRequests);
            } else {
                System.out.println("You have no pending requests.");
                BasicLogger.log("You have no pending requests.");
            }
        } catch (Exception e) {
                BasicLogger.log("Error fetching pending requests");
            }
        } else {
            System.out.println("User is not logged in.");
            BasicLogger.log("User is not logged in.");
        }
		
	}
    //4. As an authenticated user of the system, I need to be able to send a transfer of a specific amount of TE Bucks to a registered user.
    //I should be able to choose from a list of users to send TE Bucks to.
    //I must not be allowed to send money to myself.
    //A transfer includes the User IDs of the from and to users and the amount of TE Bucks.
    //The receiver's account balance is increased by the amount of the transfer.
    //The sender's account balance is decreased by the amount of the transfer.
    //I can't send more TE Bucks than I have in my account.
    //I can't send a zero or negative amount.
    //A Sending Transfer has an initial status of Approved.

	private void sendBucks(AuthenticatedUser authenticatedUser) {
            userService = new UserService(API_BASE_URL, authenticatedUser);
            if (currentUser != null) {
                try {
                    int accountFrom = authenticatedUser.getUser().getId();

                    List<User> userList = userService.getAllUsers();
                    consoleService.printUsers(userList); // Display the list of users

                    int recipientId = consoleService.promptForRecipientId(userList);
                    if (recipientId == 0) {
                        return; // User chose to cancel
                    }

                    BigDecimal amount = consoleService.promptForTransferAmount();
                    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                        System.out.println("Invalid amount. Please enter a positive value");
                        BasicLogger.log("Invalid amount. Please enter a positive value");
                        return; // User chose to cancel
                    }

                    userService.sendMoney(recipientId, amount);
                } catch (Exception e) {
                    System.out.println("Error sending money");
                    e.printStackTrace();
                    BasicLogger.log("Error sending money");
                }
            } else {
                System.out.println("User is not logged in.");
            }
		
	}

    //is optional, will do if time - this part is done(by Kyle) it would just be the part in the service class
	private void requestBucks(AuthenticatedUser authenticatedUser) {
        userService = new UserService(API_BASE_URL, authenticatedUser);
        if (currentUser != null) {
            try {
                List<User> userList = userService.getAllUsers();
                consoleService.printUsers(userList);

                int accountFrom = currentUser.getUser().getId();
                int accountTo = consoleService.promptForRecipientId(userList);
                if (accountTo == 0) {
                    return; // User chose to cancel
                }

                BigDecimal amount = consoleService.promptForTransferAmount();
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Invalid amount. Please enter a positive value");
                    BasicLogger.log("Invalid amount. Please enter a positive value");
                    return;
                }

                TransferRequest transferRequest = new TransferRequest(accountFrom, accountTo, amount);
                try {
                    userService.requestMoney(currentUser, transferRequest);
                } catch (RestClientResponseException e) {
                    System.out.println("Error sending money request. Code: " + e.getRawStatusCode());
                    BasicLogger.log("Error sending money request. Code: " + e.getRawStatusCode());
                } catch (ResourceAccessException e) {
                    System.out.println("Error accessing the server. Please try again later.");
                    BasicLogger.log("Error accessing the server.");
                }
            } catch (Exception e) {
                BasicLogger.log("Error requesting money");
            }
        } else {
            System.out.println("User is not logged in");
            BasicLogger.log("User is not logged in");
        }
		
	}

}
