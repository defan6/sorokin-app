package com.ddos.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class LoginValidateException extends RuntimeException {
   private final List<String> errors;
}
