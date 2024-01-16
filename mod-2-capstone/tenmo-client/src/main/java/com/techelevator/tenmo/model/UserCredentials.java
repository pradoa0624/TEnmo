package com.techelevator.tenmo.model;

// used to bundle user credentials(username, password) used for authentication
public class UserCredentials {

    private String username; // holds username associated with user credentials
    private String password; // holds password associated with user credentials

    // constructors
    public UserCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}

    // getters and setters
	public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

