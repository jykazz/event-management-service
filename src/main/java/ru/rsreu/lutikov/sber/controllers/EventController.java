//package ru.rsreu.lutikov.sber.controllers;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import ru.rsreu.lutikov.sber.domain.Event;
//import ru.rsreu.lutikov.sber.services.EventService;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/events")
//public class EventController {
//
//    private final EventService eventService;
//
//    public EventController(EventService eventService) {
//        this.eventService = eventService;
//    }
//
//    @PostMapping
//    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
//        Event createdEvent = eventService.createEvent(event);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
//        Event event = eventService.getEventById(id);
//        if (event != null) {
//            return ResponseEntity.ok(event);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Event>> getAllEvents() {
//        List<Event> events = eventService.getAllEvents();
//        return ResponseEntity.ok(events);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
//        Event updatedEvent = eventService.updateEvent(id, event);
//        if (updatedEvent != null) {
//            return ResponseEntity.ok(updatedEvent);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
//        boolean deleted = eventService.deleteEvent(id);
//        if (deleted) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//}
