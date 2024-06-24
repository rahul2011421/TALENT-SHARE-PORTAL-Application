package com.tsp.UserService.exception;

public class UserProfileNotFoundException extends RuntimeException{

    public UserProfileNotFoundException(String message) {
        super(message);
    }
}
