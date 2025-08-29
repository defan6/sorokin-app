package my.ddos.service.venue;

import my.ddos.model.dto.venue.PatchVenueRequest;
import my.ddos.model.dto.venue.VenueRequest;
import my.ddos.model.dto.venue.VenueResponse;

import java.util.List;

public interface VenueService {
    List<VenueResponse> getAllVenues();


    VenueResponse create(VenueRequest venueRequest);


    VenueResponse getVenue(Long id);


    VenueResponse patchVenue(Long id, PatchVenueRequest patchVenueRequest);


    void deleteVenue(Long id);
}
