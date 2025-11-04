package my.ddos.validator;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import my.ddos.exception.VenueAlreadyExistsWithThisName;
import my.ddos.exception.VenueValidateException;
import my.ddos.model.dto.venue.VenueRequest;
import my.ddos.repository.VenueRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class VenueValidator {
    private final Validator validator;

    private final VenueRepository venueRepository;


    public void validateVenueRequest(VenueRequest venueRequest){
        Set<ConstraintViolation<VenueRequest>> violates = validator.validate(venueRequest);
        List<String> errors = new ArrayList<>(violates.stream().map(ConstraintViolation::getMessage).toList());
        if(!errors.isEmpty()){
            throw new VenueValidateException(errors);
        }

        if(venueRepository.existsByName(venueRequest.getName())){
            throw new VenueAlreadyExistsWithThisName("Venue with name " + venueRequest.getName() + " already exists");
        }
    }
}
