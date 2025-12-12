package my.ddos.controller.rest;

import lombok.RequiredArgsConstructor;
import my.ddos.model.dto.event.EventRequest;
import my.ddos.model.dto.event.EventResponse;
import my.ddos.model.dto.event.PatchEventRequest;
import my.ddos.service.event.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager/events")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'SINGER')")
    public ResponseEntity<EventResponse> createEvent(@RequestBody EventRequest eventRequest,
                                                     @RequestHeader("X-Username") String username){
        EventResponse created = eventService.createEvent(eventRequest, username);
        URI location = URI.create("/api/events/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @PatchMapping("/admin/{id}")
    public ResponseEntity<EventResponse> patchEvent(@PathVariable("id") Long id,
                                                    @RequestBody PatchEventRequest patchEventRequest,
                                                    @RequestHeader("X-Username") String username){
        return ResponseEntity.ok(eventService.patchEvent(id, patchEventRequest, username));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable("id") Long id){
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

}
