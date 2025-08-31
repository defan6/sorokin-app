package my.ddos.controller.rest;

import lombok.RequiredArgsConstructor;
import my.ddos.model.dto.event.EventRequest;
import my.ddos.model.dto.event.EventResponse;
import my.ddos.model.dto.event.PatchEventRequest;
import my.ddos.service.event.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
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


    @PostMapping("/admin")
    public ResponseEntity<EventResponse> createEvent(@RequestBody EventRequest eventRequest, ServerHttpRequest request){
        String username = request.getHeaders().getFirst("X-Username");
        EventResponse created = eventService.createEvent(eventRequest, username);
        URI location = URI.create("/api/events/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @PatchMapping("/admin/{id}")
    public ResponseEntity<EventResponse> patchEvent(@PathVariable("id") Long id, @RequestBody PatchEventRequest patchEventRequest, ServerHttpRequest request){
        String username = request.getHeaders().getFirst("X-Username");
        return ResponseEntity.ok(eventService.patchEvent(id, patchEventRequest, username));
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable("id") Long id){
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

}
