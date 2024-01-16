package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

public class Account {
    @JsonProperty("account_id")
    private int accountId;
    @JsonProperty("user_id")
    private int userId;
    private BigDecimal balance = BigDecimal.valueOf(1000);

    public Account() {
    }

    public Account(int account_id, int user_id, BigDecimal balance) {
        this.accountId = account_id;
        this.userId = user_id;
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int account_id) {
        this.accountId = account_id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int user_id) {
        this.userId = user_id;
    } // is there a reason these are underscored, I changed it, this is here if it needs to be changed again

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString(){
        return "Account{" +
                "account_id=" + accountId +
                ", user_id=" + userId +
                ", balance =" + balance +
                '}';
    }

    public void transfer(Account accountTo, BigDecimal transferAmount) {
        if(this.balance.compareTo(transferAmount) >= 0) {
            this.balance = this.balance.subtract(transferAmount);
            accountTo.balance = accountTo.balance.add(transferAmount);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, transferAmount + " exceeds the remaining balance of " + this.balance);
        }
    }
}
