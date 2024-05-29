package com.maveric.tsp.reportService.exception;

import com.maveric.tsp.reportService.dtos.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<ResponseDto> handleNoDataFoundException(NoDataFoundException e){
        log.info("No Users are present: "+e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto(HttpStatus.BAD_REQUEST.toString(),400,"No Content",e.getMessage()));
    }
}
