package my.ddos.service.venue;

import lombok.RequiredArgsConstructor;
import my.ddos.exception.VenueNotFoundException;
import my.ddos.mapper.VenueMapper;
import my.ddos.model.dto.venue.PatchVenueRequest;
import my.ddos.model.dto.venue.VenueRequest;
import my.ddos.model.dto.venue.VenueResponse;
import my.ddos.model.entity.Venue;
import my.ddos.repository.VenueRepository;
import my.ddos.validator.VenueValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;

    private final VenueMapper venueMapper;

    private final VenueValidator venueValidator;
    @Override
    public List<VenueResponse> getAllVenues() {
        return venueRepository.findAll().stream().map(venueMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public VenueResponse create(VenueRequest venueRequest) {
        venueValidator.validateVenueRequest(venueRequest);
        Venue venue = venueMapper.toEntity(venueRequest);
        return venueMapper.toResponse(venueRepository.save(venue));
    }



    @Override
    public VenueResponse getVenue(Long id) {
        return venueRepository.findById(id).map(venueMapper::toResponse)
                .orElseThrow(() -> new VenueNotFoundException("Venue with id " + id + " not found"));
    }


    @Override
    @Transactional
    public VenueResponse patchVenue(Long id, PatchVenueRequest patchVenueRequest) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new VenueNotFoundException("Venue with id " + id + " not found"));
        patchVenueRequest.address().ifPresent(venue::setAddress);
        patchVenueRequest.name().ifPresent(venue::setName);
        patchVenueRequest.capacity().ifPresent(venue::setCapacity);
        return venueMapper.toResponse(venueRepository.save(venue));
    }

    @Override
    @Transactional
    public void deleteVenue(Long id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new VenueNotFoundException("Venue with id " + id + " not found"));
        venueRepository.deleteById(id);
    }
}
