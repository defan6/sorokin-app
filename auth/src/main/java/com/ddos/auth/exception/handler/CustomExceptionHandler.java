package com.ddos.auth.exception.handler;


import com.ddos.auth.exception.LoginValidateException;
import com.ddos.auth.exception.RegisterValidateException;
import com.ddos.auth.exception.UserAlreadyExistsException;
import com.ddos.auth.model.dto.ExceptionBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler
    ResponseEntity<ExceptionBody> handleRegisterValidate(RegisterValidateException e){
        ExceptionBody exceptionBody = new ExceptionBody("Registered failed", e.getErrors());
        return ResponseEntity.badRequest().body(exceptionBody);
    }


    @ExceptionHandler
    ResponseEntity<ExceptionBody> handleLoginValidate(LoginValidateException e){
        ExceptionBody exceptionBody = new ExceptionBody("Login failed", e.getErrors());
        return ResponseEntity.badRequest().body(exceptionBody);
    }

    @ExceptionHandler
    ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
