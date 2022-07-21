package com.example.ldapauthservice.models;

import javax.naming.AuthenticationException;

public class AuthenticationResult {
    private boolean authenticated;


    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Exception getAuthenticationException() {
        return authenticationException;
    }

    public void setAuthenticationException(Exception authenticationException) {
        this.authenticationException = authenticationException;
    }

    private Exception authenticationException;
}
