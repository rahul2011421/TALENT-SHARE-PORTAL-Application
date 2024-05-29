package com.tsp.gateway.exceptions;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(String msg){
        super(msg);
    }
    public InvalidTokenException(String msg,Throwable cause){
        super(msg,cause);
    }
}
