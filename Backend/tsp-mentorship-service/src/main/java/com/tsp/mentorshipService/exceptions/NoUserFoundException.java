package com.tsp.mentorshipService.exceptions;

public class NoUserFoundException extends RuntimeException{
    public NoUserFoundException(String msg){
        super(msg);
    }
}
