package com.techelevator.tenmo.model;

import java.util.Objects;

public class User {

    private int id; // unique identifier for user
    private String username; // holds User username

    public int getId() {
        return id;
    } // return user id

    public void setId(int id) {
        this.id = id;
    } // set user id

    public String getUsername() {
        return username;
    } // return username

    public void setUsername(String username) {
        this.username = username;
    } // set username

    @Override
    public boolean equals(Object other) {
        if (other instanceof User) {
            User otherUser = (User) other;
            return otherUser.getId() == id
                    && otherUser.getUsername().equals(username);
        } else {
            return false;
        }
    } // check id, username of current user object == to other user object

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    } // generate hashCode for User object based on id and username



}
