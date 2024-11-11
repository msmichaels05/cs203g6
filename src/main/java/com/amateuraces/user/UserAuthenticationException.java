package com.amateuraces.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Custom Exception for Authentication Errors
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAuthenticationException extends RuntimeException {
    public UserAuthenticationException() {
        super("User not authenticated");
    }
}