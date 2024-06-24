package com.tsp.mentorshipService.exceptions;

public class NoManagerFoundException extends RuntimeException{
    public NoManagerFoundException(String msg){
        super(msg);
    }
}
