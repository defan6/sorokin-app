package com.ddos.auth.exception.handler;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler
    ResponseEntity<String> handleUserAlreadyExist(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
