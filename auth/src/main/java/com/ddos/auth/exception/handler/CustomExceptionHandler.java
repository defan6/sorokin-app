package com.ddos.auth.exception.handler;


import com.ddos.auth.exception.LoginValidateException;
import com.ddos.auth.exception.RegisterValidateException;
import com.ddos.auth.exception.TokenBlacklistedException;
import com.ddos.auth.exception.UserAlreadyExistsException;
import com.ddos.auth.model.dto.ExceptionBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler(RegisterValidateException.class)
    ResponseEntity<ExceptionBody> handleRegisterValidate(RegisterValidateException e){
        ExceptionBody exceptionBody = new ExceptionBody("Registered failed", e.getErrors());
        return ResponseEntity.badRequest().body(exceptionBody);
    }


    @ExceptionHandler(LoginValidateException.class)
    ResponseEntity<ExceptionBody> handleLoginValidate(LoginValidateException e){
        ExceptionBody exceptionBody = new ExceptionBody("Login failed", e.getErrors());
        return ResponseEntity.badRequest().body(exceptionBody);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }


    @ExceptionHandler(TokenBlacklistedException.class)
    ResponseEntity<String> handleTokenIsBlacklistedException(TokenBlacklistedException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}
