package my.ddos.controller.rest;

import lombok.RequiredArgsConstructor;
import my.ddos.model.dto.event.EventRequest;
import my.ddos.model.dto.event.EventResponse;
import my.ddos.model.dto.event.PatchEventRequest;
import my.ddos.service.event.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable("id") Long id){
        return ResponseEntity.ok(eventService.getEvent(id));
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents(){
        return ResponseEntity.ok(eventService.getAllEvents());
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponse> createEvent(@RequestBody EventRequest eventRequest){
        EventResponse created = eventService.createEvent(eventRequest);
        URI location = URI.create("/api/events/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponse> patchEvent(@PathVariable("id") Long id, @RequestBody PatchEventRequest patchEventRequest){
        return ResponseEntity.ok(eventService.patchEvent(id, patchEventRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable("id") Long id){
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

}
