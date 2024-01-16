package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.CustomLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {

    private CustomLogger customLogger = new CustomLogger(UserController.class);

    @Autowired
    UserDao userDao;

    @Autowired
    TransferDAO transferDao;

    @Autowired
    AccountDAO accountDAO;


    @RequestMapping(path="/users", method = RequestMethod.GET)
    public List<User> getUsers() {
        return userDao.getUsers();
    }

    @RequestMapping(path="/users/{id}", method = RequestMethod.GET)
    public User getUserById(@PathVariable int id) {
        User user = userDao.getUserById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        } else {
            return user;
        }

    }

    @RequestMapping(path="/account_balance/{id}", method = RequestMethod.GET)
    public BigDecimal getBalanceById(@PathVariable int id) {
        Account account = accountDAO.getAccountByUserId(id);
        BigDecimal balance = account.getBalance();

        //BigDecimal balance = accountDAO.getAccountByUserId(id).getBalance();
        if (balance == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Not Found");
        } else {
            return balance;
        }

    }

    @RequestMapping(path="/account/{id}", method = RequestMethod.GET)
    public Account getAccountByUserId(@PathVariable int id) {
        Account account = accountDAO.getAccountByUserId(id);
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        } else {
            System.out.println(account);
            return account;
        }
    }



    @RequestMapping(path="/transferHistory/{id}", method = RequestMethod.GET)
    public List<Transfer> getTransfers(@PathVariable int id){
        List<Transfer> transferList = transferDao.getTransfers(id);
        if (transferList == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Not Found");
        } else {
            return transferList;
        }
    }

    @RequestMapping(path="/transfer/{id}", method = RequestMethod.GET )
    public Transfer getTransferById(@PathVariable int id){
        Transfer transfer = transferDao.getTransferByTransferId(id);
        if (transfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer Not Found");
        } else {
            return transfer;
        }
    }


    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path="/transfer", method = RequestMethod.POST)
    public Transfer createTransfer(@RequestBody Transfer transfer) {
        System.out.println("From the controller method: " + transfer);
        try {
            // Log the incoming transfer for debugging purposes
            customLogger.log(CustomLogger.Level.INFO, "Creating transfer: " + transfer, null);

            // Validate the Transfer object (e.g., check for null values)
            //validateTransfer(transfer);

            // Update sender and recipient account balances
            // Set default transfer status and type
            transferMoney(transfer);
            return transferDao.createTransfer(transfer);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating transfer", e);
        }
    }

    private void validateTransfer(Transfer transfer) {
        try {
            if (transfer.getAccountFrom() != transfer.getAccountTo()) {
                String errorMessage = "Invalid transfer: Cannot transfer money from an account to itself. Transfer details - From: " + transfer.getAccountFrom() + ", To: " + transfer.getAccountTo() + ", Amount: " + transfer.getAmount();
                customLogger.log(CustomLogger.Level.ERROR, errorMessage, null);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot transfer money from an account to itself.");
            }

            // Add other validation logic as needed (e.g., check for null values)
        } catch (Exception e) {
            customLogger.log(CustomLogger.Level.ERROR, "An unexpected error occurred during transfer validation.", e);
            throw e; // Rethrow the exception after logging
        }
    }

    @RequestMapping(path="/transfer/{id}", method = RequestMethod.PUT)
    public Transfer updateTransfer(@RequestBody Transfer transfer, @PathVariable int id){
        transfer.setTransferId(id);
        try{
            Transfer updatedTransfer = transferDao.updateTransferStatus(transfer);
            return updatedTransfer;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer Not Found");
        }

    }


    private void transferMoney(Transfer transfer) {
        Account accountFrom = accountDAO.getAccountByAccountId(transfer.getAccountFrom());
        Account accountTo = accountDAO.getAccountByAccountId(transfer.getAccountTo());
        accountFrom.transfer(accountTo, transfer.getAmount());
        accountDAO.updateAccountBalance(accountFrom);
        accountDAO.updateAccountBalance(accountTo);
    }







}
