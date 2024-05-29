package com.maveric.tsp.UserService.exception;

import com.maveric.tsp.UserService.dtos.ResponseDto;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserProfileNotFoundException.class)
    public ResponseEntity<ResponseDto> handleInvalidCredentialsException(UserProfileNotFoundException e){
        log.error("User Profile Not Found Exception:",e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto<>(HttpStatus.BAD_REQUEST.toString(),400,"UserProfile Not Found",e.getMessage()));
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ResponseDto> handleFeignExceptions(FeignException e){
        log.error("Feign Exception while fetching user from register-service:",e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto<>(HttpStatus.BAD_REQUEST.toString(),400,"FeignException","Client error"));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ResponseDto> handleInvalidInputExceptions(Exception e){
        log.error("Invalid Input:",e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto<>(HttpStatus.BAD_REQUEST.toString(),400,"InvalidInputException",e.getMessage()));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseDto> handleCustomExceptions(CustomException e){
        log.error("Exception Occurred:",e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto<>(HttpStatus.BAD_REQUEST.toString(),400,"Custom Exception",e.getMessage()));
    }
}
