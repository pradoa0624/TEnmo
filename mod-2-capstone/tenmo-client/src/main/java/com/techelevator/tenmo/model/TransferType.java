package com.techelevator.tenmo.model;

public enum TransferType {
    SEND("Send"),
    REQUEST("Request");

    private final String description;

    TransferType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
