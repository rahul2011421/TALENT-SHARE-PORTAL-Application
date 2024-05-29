package com.maveric.tsp.UserService.exception;

public class UserProfileNotFoundException extends RuntimeException{

    public UserProfileNotFoundException(String message) {
        super(message);
    }
}
