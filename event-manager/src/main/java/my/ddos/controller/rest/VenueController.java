package my.ddos.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.ddos.model.dto.venue.PatchVenueRequest;
import my.ddos.model.dto.venue.VenueRequest;
import my.ddos.model.dto.venue.VenueResponse;
import my.ddos.service.venue.VenueService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager/venues")
public class VenueController {

    private final VenueService venueService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<VenueResponse>> getAll() {
        boolean isAuthenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(venueService.getAllVenues());
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VenueResponse> createVenue(@RequestBody VenueRequest venueRequest) {
        VenueResponse created = venueService.create(venueRequest);
        URI location = URI.create("api/venues/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VenueResponse> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(venueService.getVenue(id));
    }


    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VenueResponse> patchVenue(@PathVariable("id") Long id, @RequestBody @Valid PatchVenueRequest patchVenueRequest) {
        return ResponseEntity.ok(venueService.patchVenue(id, patchVenueRequest));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVenue(@PathVariable("id") Long id) {
        venueService.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }
}
