package com.techelevator.tenmo.dao;

import  com.techelevator.tenmo.model.Account;

import java.util.List;

public interface AccountDAO {

    List<Account> getAccounts();

    Account getAccountByAccountId(int id); // a lot of these were underscored

    Account getAccountByUserId(int id);

    Account getBalanceById(int id);

    /** Account creation is handled in the createUser() method inside JdbcUserDAO.
     I'm omitting the createAccount() method here for continuity's sake. -GR **/
    //Account createAccount(Account account);

    Account updateAccountBalance(Account account);


    /** In the preexisting UserDAO there is no method for deleting a user.
      I'm going to leave out the option to delete an account for now because my instinct is
     that you should not be able to delete an account while the user still exists.**/
    //int deleteAccountByAccountId(int id);

}
