package com.techelevator.tenmo.model;

import java.math.BigDecimal;
// i don't know if this class needed to be made, the Transfer class has all of this
public class TransferRequest {
    private int accountFrom;
    private int accountTo;
    private BigDecimal amount;
    private int requestId;
    private String receiverUsername;

    public TransferRequest(int accountFrom, int accountTo, BigDecimal amount) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
