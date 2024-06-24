package com.tsp.mentorshipService.exceptions;

public class NoMentorApprovalException extends RuntimeException{
    public NoMentorApprovalException(String msg){
        super(msg);
    }
}
