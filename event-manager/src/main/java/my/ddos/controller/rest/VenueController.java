package my.ddos.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.ddos.model.dto.venue.PatchVenueRequest;
import my.ddos.model.dto.venue.VenueRequest;
import my.ddos.model.dto.venue.VenueResponse;
import my.ddos.service.venue.VenueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager/venues/admin")
public class VenueController {

    private final VenueService venueService;

    @GetMapping
    public ResponseEntity<List<VenueResponse>> getAll(){
        return ResponseEntity.ok(venueService.getAllVenues());
    }


    @PostMapping
    public ResponseEntity<VenueResponse> createVenue(@RequestBody VenueRequest venueRequest){
        VenueResponse created = venueService.create(venueRequest);
        URI location = URI.create("api/venues/admin/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }


    @GetMapping("/{id}")
    public ResponseEntity<VenueResponse> getById(@PathVariable("id") Long id){
        return ResponseEntity.ok(venueService.getVenue(id));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<VenueResponse> patchVenue(@PathVariable("id") Long id, @RequestBody @Valid PatchVenueRequest patchVenueRequest){
        return ResponseEntity.ok(venueService.patchVenue(id, patchVenueRequest));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable("id") Long id){
        venueService.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }
}
