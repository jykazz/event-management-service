package ru.rsreu.lutikov.sber.services;

import org.springframework.stereotype.Service;
import ru.rsreu.lutikov.sber.domain.Event;
import ru.rsreu.lutikov.sber.repositories.EventRepository;

import java.util.List;


@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event updateEvent(Long id, Event event) {
        Event existingEvent = eventRepository.findById(id).orElse(null);
        if (existingEvent != null) {
            existingEvent.setEventName(event.getEventName());
            existingEvent.setEventDescription(event.getEventDescription());
            existingEvent.setEventDateTime(event.getEventDateTime());
            return eventRepository.save(existingEvent);
        }
        return null;
    }

    public boolean deleteEvent(Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Другие методы

}
