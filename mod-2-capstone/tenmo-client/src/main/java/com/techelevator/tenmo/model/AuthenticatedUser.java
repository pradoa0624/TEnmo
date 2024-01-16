package com.techelevator.tenmo.model;

public class AuthenticatedUser {
	
	private String token; // used to store authentication token to verify requests made by user
	private User user;	  // variable of type User represents information about authenticated user (username, id)
	
	public String getToken() {
		return token;
	} // returns authentication token
	public void setToken(String token) {
		this.token = token;
	} // sets authentication token with provided value
	public User getUser() {
		return user;
	} // returns User object associated with authenticated user
	public void setUser(User user) {
		this.user = user;
	} // sets User object for authenticated user
}
