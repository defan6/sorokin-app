package com.ddos.auth.validator;

import com.ddos.auth.exception.LoginValidateException;
import com.ddos.auth.exception.RegisterValidateException;
import com.ddos.auth.exception.UserAlreadyExistsException;
import com.ddos.auth.model.dto.login.LoginRequest;
import com.ddos.auth.model.dto.register.RegisterRequest;
import com.ddos.auth.repository.AuthRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuthValidator {
    private final Validator validator;

    private final AuthRepository authRepository;

    public void validateRegisterRequest(RegisterRequest registerRequest){
        Set<ConstraintViolation<RegisterRequest>> violates = validator.validate(registerRequest);
        List<String> errors = new ArrayList<>(violates.stream()
                .map(ConstraintViolation::getMessage)
                .toList());
        if(!errors.isEmpty()){
            throw new RegisterValidateException(errors);
        }
        if(authRepository.existsByUsername(registerRequest.getUsername())){
            throw new UserAlreadyExistsException("User with username " + registerRequest.getUsername() + " already exists");
        }
    }


    public void validateLoginRequest(LoginRequest loginRequest){
        Set<ConstraintViolation<LoginRequest>> violates = validator.validate(loginRequest);
        List<String> errors = new ArrayList<>(violates.stream().map(ConstraintViolation::getMessage).toList());
        if(!errors.isEmpty()){
            throw new LoginValidateException(errors);
        }
        if(!authRepository.existsByUsername(loginRequest.getUsername())){
            throw new UsernameNotFoundException("User with username " + loginRequest.getUsername() + " does not exist");
        }
    }
}
