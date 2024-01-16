package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

// responsible for handling user authentication and registration by interacting with remote server through HTTP requests
public class AuthenticationService {

    private final String baseUrl; // store base URL of remote server to which requests will be sent
    private final RestTemplate restTemplate = new RestTemplate(); // HTTP client provided by Spring. used to perform HTTP requests to the server

    // one constructor, takes URL as a param, initialize baseUrl variable
    public AuthenticationService(String url) {
        this.baseUrl = url;
    }


    /**
     * login by creating HTTP request with bundled credentials
     * send POST request to "login" using RestTemplate
     * return AutenticatedUser if successful
      */
    public AuthenticatedUser login(UserCredentials credentials) {
        HttpEntity<UserCredentials> entity = createCredentialsEntity(credentials);
        AuthenticatedUser user = null;
        try {
            ResponseEntity<AuthenticatedUser> response =
                    restTemplate.exchange(baseUrl + "login", HttpMethod.POST, entity, AuthenticatedUser.class);
            user = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return user;
    }

    /**
     * register is like login() but returns true if successful, otherwise log exceptions and return false
      */
    //Either here or in the App class we need to implement that new users start with 10,000 TE Bucks,
    // or maybe in a class server side I dunno
    public boolean register(UserCredentials credentials) {
        HttpEntity<UserCredentials> entity = createCredentialsEntity(credentials);
        boolean success = false;
        try {
            restTemplate.exchange(baseUrl + "register", HttpMethod.POST, entity, Void.class);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }


    /**
     * create HTTP request entity that includes bundled credentials, sets content type to JSON
     * used internally by login() and register() to create request entity before making HTTP requests
     */
    private HttpEntity<UserCredentials> createCredentialsEntity(UserCredentials credentials) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(credentials, headers);
    }



}
