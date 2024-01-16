package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.*;
import com.techelevator.util.CustomLogger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// (Some methods need filled in) I also don't know the accurate endpoints,
// We are creating them  in the controller classes server side

public class UserService {

    private String BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;
    private CustomLogger customLogger = new CustomLogger(UserService.class);

    public UserService(String url, AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
        this.BASE_URL = url;
    }

    /**
     * replace "balance" with actual endpoint
     */
    public void getCurrentBalance(int userId) {
        try {
            // Make a GET request to the balance endpoint for the specific user ID
            BigDecimal balance = restTemplate.exchange(
                    BASE_URL + "/account_balance/" + userId,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    BigDecimal.class
            ).getBody();

            customLogger.log(CustomLogger.Level.INFO, "Balance retrieved successfully for user ID: " + userId, null);
            // Display the balance to the user
            System.out.println("Your current account balance is: $" + balance);
        } catch (RestClientResponseException e) {
            customLogger.log(CustomLogger.Level.ERROR, "Error retrieving balance. Code: " + e.getRawStatusCode(), e);
        } catch (ResourceAccessException e) {
            customLogger.log(CustomLogger.Level.ERROR, "Error accessing the server. Please try again later.", e);
        }
    }

//    /**
//    * Get all users excluding currentUser (cannot send money to yourself)
//    * @param currentUser to exclude
//
//   */


    // Method for grabbing users and recipients account ids.


    public Integer getAccountByUserId(int userId){
        try{
            ResponseEntity<Account> response = restTemplate.exchange(
                    BASE_URL + "/account/" + userId,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Account.class
            );

            Account account = response.getBody();
             int accountId = account.getAccountId();
             return accountId;
        } catch (RestClientResponseException e) {
            customLogger.log(CustomLogger.Level.ERROR, "Error retrieving account id. Code: " + e.getRawStatusCode(), e);
        } catch (ResourceAccessException e) {
            customLogger.log(CustomLogger.Level.ERROR, "Error accessing the server. Please try again later.", e);
        }
        return null;
    }
    public User getUserById(int userId) {
        try {
            // Make a GET request to the server to fetch user details by ID
            ResponseEntity<User> response = restTemplate.exchange(
                    BASE_URL + "/users/" + userId,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    User.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // Return the User object
                return response.getBody();
            } else {
                // Handle the case where the user is not found
                return null;
            }
        } catch (RestClientResponseException e) {
            // Handle exceptions...
            return null; // Or handle the error accordingly
        }
    }

    private String getRecipientUsername(int recipientId) {
        try {
            // Make a request to UserController on the server to get user details by ID
            ResponseEntity<User> response = restTemplate.exchange(
                    BASE_URL + "/users/" + recipientId,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    User.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // Return the username of the recipient
                return response.getBody().getUsername();
            } else {
                // Handle the case where the user is not found
                return null;
            }
        } catch (RestClientResponseException e) {
            // Handle exceptions...
            return null; // Or handle the error accordingly
        }
    }

    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>();
        try {
            User[] usersArray = restTemplate.exchange(BASE_URL + "/users", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();

            // Filter out the currentUser from the list
            if (usersArray != null && currentUser != null) {
                usersList = Arrays.stream(usersArray)
                        .filter(user -> user.getId() != currentUser.getUser().getId())
                        .collect(Collectors.toList());
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            customLogger.log(CustomLogger.Level.ERROR, e.getMessage(), e);
        }
        return usersList;

    }

    public Transfer[] getTransferHistory(AuthenticatedUser currentUser) {
        Transfer[] transferHistory = null;
        int userId = currentUser.getUser().getId();
        int accountId = getAccountByUserId(userId);
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(BASE_URL + "/transferHistory/" + accountId, HttpMethod.GET,
                    makeAuthEntity(), Transfer[].class);
            transferHistory = response.getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            customLogger.log(CustomLogger.Level.ERROR, e.getMessage(), e);
            System.out.println("Error retrieving transfer history. Please try again.");
        }
        return transferHistory;
    }

    public void sendMoney(int recipientId, BigDecimal amount) {
        try {
            customLogger.debug("Sender's Account ID: " + currentUser.getUser().getId());
            customLogger.debug("Recipient's Account ID: {}");
            int userId = currentUser.getUser().getId();
            int accountTo = getAccountByUserId(recipientId);
            int accountFrom = getAccountByUserId(userId);
            Transfer transfer = new Transfer(accountTo, accountFrom, amount);
            // Set the transfer status to "Approved" initially if applicable
            //TransferStatus transferStatus = TransferStatus.APPROVED;

            // Make a POST request to the server to initiate the money transfer
            // Transfer transfer = new Transfer(transferId, TransferType.SEND, accountFrom, accountTo, amount);

            // Debugging: Print sender and receiver usernames

            // Set the sender and receiver usernames
//            transfer.setSenderUsername(currentUser.getUser().getUsername());
//            transfer.setReceiverUsername(getRecipientUsername(accountTo));
            transfer.setTransferStatusId(2);
            transfer.setTransferTypeId(2);
            transfer.setAccountFrom(accountFrom);
            transfer.setAccountTo(accountTo);

            // Set the sender and receiver account IDs

            /*transfer.setAccountFrom(transfer.getAccountFrom());
            transfer.setAccountTo(transfer.getAccountTo());*/
            // Debug statements to verify account IDs after setting usernames
            customLogger.debug("Sender's Account ID (After setting usernames and account IDs): " + transfer.getAccountFrom());
            customLogger.debug("Recipient's Account ID (After setting usernames and account IDs): " + transfer.getAccountTo());




            HttpEntity<Transfer> entity = makeTransferEntity(transfer);
            /*Transfer createdTransfer = restTemplate.exchange(
                    BASE_URL + "/transfer", HttpMethod.POST, entity,
                    Transfer.class).getBody();*/

           try {
               ResponseEntity<Transfer> responseEntity = restTemplate.exchange(
                    BASE_URL + "/transfer", HttpMethod.POST, entity, Transfer.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Transfer createdTransfer = responseEntity.getBody();
                // Proceed with your logic
                String successMessage = "Money sent successfully. Transfer ID: " + createdTransfer.getTransferId();
                System.out.println(successMessage);
                customLogger.log(CustomLogger.Level.INFO, successMessage, null);
            } else {
                System.out.println("Server responded with error: " + responseEntity.getStatusCode());
                System.out.println("Response body: " + responseEntity.getBody());
                // Handle the error appropriately
            }
        } catch (HttpClientErrorException e) {
            System.out.println("Client error: " + e.getStatusCode() + ", " + e.getResponseBodyAsString());
            // Handle client error
        } catch (HttpServerErrorException e) {
            System.out.println("Server error: " + e.getStatusCode() + ", " + e.getResponseBodyAsString());
            // Handle server error
        } catch (RestClientException e) {
            System.out.println("RestTemplate exception: " + e.getMessage());
            // Handle other RestTemplate exceptions
        }





            // Display a success message to the user, I don't know how to do the split one |, also should probably be logged
            //assert createdTransfer != null;

        } catch (RestClientResponseException e) {
            customLogger.log(CustomLogger.Level.ERROR, "Error sending money. Code: " + e.getRawStatusCode(), e);
            System.out.println("Error sending money. Code: " + e.getRawStatusCode());

            // Log server response details
            System.out.println("Response body: " + e.getResponseBodyAsString());

            // Print the full stack trace for debugging
            e.printStackTrace();
        } catch (ResourceAccessException e) {
            customLogger.log(CustomLogger.Level.ERROR, "Error accessing the server. Please try again later.", e);
            System.out.println("Error accessing the server. Please try again later.");
        }
    }

    public void requestMoney(AuthenticatedUser currentUser, TransferRequest transferRequest) {

        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    BASE_URL + "/transfer",
                    HttpMethod.POST,
                    makeTransferRequestEntity(transferRequest, currentUser),
                    Void.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Money request sent successfully.");
                customLogger.log(CustomLogger.Level.INFO, "Money request sent successfully.", null);
            } else {
                System.out.println("Unexpected response: " + response.getStatusCodeValue());
                customLogger.log(CustomLogger.Level.ERROR, "Unexpected response: " + response.getStatusCodeValue(), null);
            }

        } catch (RestClientResponseException e) {
            customLogger.log(CustomLogger.Level.ERROR, "Error sending money request. Code: " + e.getRawStatusCode(), e);
            System.out.println("Error sending money request. Code: " + e.getRawStatusCode());
        } catch (ResourceAccessException e) {
            customLogger.log(CustomLogger.Level.ERROR, "Error accessing the server. Please try again later.", e);
            System.out.println("Error accessing the server. Please try again later.");
        }
    }

    //This is optional, will do if time
    public List<TransferRequest> getPendingRequests(AuthenticatedUser currentUser) {
        // TODO: Implement the logic to get pending requests
        return null;
    }

    //This is to get transfer details by transfer ID, I need to implement it in view transfer method in app class
    public Transfer getTransferDetails(int transferId) {
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(
                    BASE_URL + "/transfer/" + transferId, HttpMethod.GET, makeAuthEntity(),
                    Transfer.class);
            Transfer transfer = response.getBody();

            return transfer;

        } catch (RestClientResponseException e) {
            System.out.println("Error retrieving transfer details. Code: " + e.getRawStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Error accessing the server. Please try again later.");
        }
        return null;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(transfer, headers);
    }




    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }

    private HttpEntity<TransferRequest> makeTransferRequestEntity(TransferRequest transferRequest, AuthenticatedUser currentUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(transferRequest, headers);
    }

}
