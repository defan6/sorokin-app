package my.ddos.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import my.ddos.exception.LoginValidateException;
import my.ddos.exception.RegisterValidateException;
import my.ddos.exception.UserAlreadyExistsException;
import my.ddos.model.dto.login.LoginRequest;
import my.ddos.model.dto.register.RegisterRequest;
import my.ddos.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuthValidator {
    private final Validator validator;

    private final UserRepository userRepository;

    public void validateRegisterRequest(RegisterRequest registerRequest){
        Set<ConstraintViolation<RegisterRequest>> violates = validator.validate(registerRequest);
        List<String> errors = new ArrayList<>(violates.stream()
                .map(ConstraintViolation::getMessage)
                .toList());
        if(!errors.isEmpty()){
            throw new RegisterValidateException(errors);
        }
        if(userRepository.existsByUsername(registerRequest.getUsername())){
            throw new UserAlreadyExistsException("User with username " + registerRequest.getUsername() + " already exists");
        }
    }


    public void validateLoginRequest(LoginRequest loginRequest){
        Set<ConstraintViolation<LoginRequest>> violates = validator.validate(loginRequest);
        List<String> errors = new ArrayList<>(violates.stream().map(ConstraintViolation::getMessage).toList());
        if(!errors.isEmpty()){
            throw new LoginValidateException(errors);
        }
        if(!userRepository.existsByUsername(loginRequest.getUsername())){
            throw new UsernameNotFoundException("User with username " + loginRequest.getUsername() + " does not exist");
        }
    }
}
