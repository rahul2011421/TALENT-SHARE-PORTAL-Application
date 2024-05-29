package com.maveric.tsp.registerservice.exception;

import com.maveric.tsp.registerservice.dto.ResponseDto;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class UserExceptionsHandler {

    private static final Logger Log=LoggerFactory.getLogger(UserExceptionsHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseDto> handleUserNotFoundException(Exception e){
        Log.error("User Not Found:",e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto(HttpStatus.BAD_REQUEST.toString(),400,"User Not Found",e.getMessage() ));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ResponseDto> handleInvalidCredentialsException(Exception e){
        Log.error("Invalid Credentials Exception:",e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto<>(HttpStatus.BAD_REQUEST.toString(),400,"Invalid Credentials",e.getMessage()));
    }

    @ExceptionHandler(NoUserGroupFoundException.class)
    public ResponseEntity<ResponseDto> handleNoUserGroupFoundException(Exception e){
        Log.error("NoUserGroupFoundException:",e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                body(new ResponseDto<>(HttpStatus.BAD_REQUEST.toString(),400,"No User Group Found",e.getMessage()));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ResponseDto> handleInvalidInputException(Exception e){
        Log.error("Invalid Input:",e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                body(new ResponseDto<>(HttpStatus.BAD_REQUEST.toString(),400,"Entered Invalid Input",e.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseDto> handleDataIntegrityViolationException(DataIntegrityViolationException e){
        Log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto<>(HttpStatus.BAD_REQUEST.toString(),400,"Data Integrity Violation",e.getMessage()));
    }

    @ExceptionHandler(CustomExceptions.class)
    public ResponseEntity<ResponseDto> handleCustomException(CustomExceptions e){
        Log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto<>(HttpStatus.BAD_REQUEST.toString(),400,"Custom Exception",e.getMessage()));
    }


    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ResponseDto> handleFeignException(FeignException e){
        Log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto<>(HttpStatus.BAD_REQUEST.toString(),400,"Feign Client Exception","Client Error"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        HashMap<String,String> errors=new HashMap<>();
        e.getBindingResult()
                .getAllErrors()
                .stream()
                .forEach(error->errors.put(((FieldError)error).getField(),error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto<>(HttpStatus.BAD_REQUEST.toString(),400,"Method argument not valid",errors));
    }
}
