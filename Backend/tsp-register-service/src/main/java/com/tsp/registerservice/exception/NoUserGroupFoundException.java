package com.tsp.registerservice.exception;

public class NoUserGroupFoundException extends RuntimeException {
    public NoUserGroupFoundException(String msg) {
        super(msg);
    }
}
