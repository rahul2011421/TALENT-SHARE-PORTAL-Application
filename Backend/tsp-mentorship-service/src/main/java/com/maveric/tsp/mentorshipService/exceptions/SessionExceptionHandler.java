package com.maveric.tsp.mentorshipService.exceptions;

import com.maveric.tsp.mentorshipService.dtos.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class SessionExceptionHandler {


    @ExceptionHandler(NoUserFoundException.class)
    public ResponseEntity<ResponseDto> handleNoUserFoundException(Exception e){
        log.error("No Mentor found for this skill set:",e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto(HttpStatus.BAD_REQUEST.toString(),400,"No user found",e.getMessage()));
    }
    @ExceptionHandler(NoDetailsFoundException.class)
    public ResponseEntity<ResponseDto> handleNoDetailsFoundException(Exception e){
        log.error("No such details found:",e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto(HttpStatus.BAD_REQUEST.toString(),400,"No user found",e.getMessage()));
    }

    public ResponseEntity<ResponseDto> handleNoManagerFoundException(Exception e){
        log.error("No Manager tagged for this mentor",e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto(HttpStatus.BAD_REQUEST.toString(),400,"No Manager tagged for this mentor",e.getMessage()));
    }
}
