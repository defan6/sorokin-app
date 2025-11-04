package com.ddos.auth.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
