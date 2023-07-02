package ru.rsreu.lutikov.sber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.rsreu.lutikov.sber.domain.Event;
import ru.rsreu.lutikov.sber.repositories.EventRepository;
import ru.rsreu.lutikov.sber.services.EventService;

import java.util.List;

@Controller
public class NewEventController {
    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/events")
    public String events(Model model) {
        List<Event> yourDataList = eventService.getAllEvents(); // Получение данных JSON
        model.addAttribute("yourDataList", yourDataList); // Передача данных в модель представления
        return "events"; // Возвращаем имя представления
    }

    @GetMapping("/events/new")
    public String createEventForm(Model model) {
        model.addAttribute("event", new Event());
        return "newEvent";
    }

    @PostMapping("/events/new")
    public String createEvent(@ModelAttribute("event") Event event, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println("ERROR");
            return "newEvent";
        }
        eventRepository.save(event);
        return "redirect:/events";
    }

    @GetMapping("/events/{eventId}/edit")
    public String editEventForm(@PathVariable Long eventId, Model model) {
        Event event = eventService.getEventById(eventId);
        model.addAttribute("event", event);
        return "editEvent";
    }

    @PostMapping("/events/{eventId}/edit")
    public String editEvent(@PathVariable String eventId, @ModelAttribute("event") Event event,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editEvent";
        }
        Long eventIdLong = Long.parseLong(eventId);
        event.setId(eventIdLong);
        eventService.updateEvent(event.getId(), event);
        return "redirect:/events";
    }

    @GetMapping("/events/{eventId}/delete")
    public String deleteEventForm(@PathVariable Long eventId, Model model) {
        Event event = eventService.getEventById(eventId);
        model.addAttribute("event", event);
        return "deleteEvent";
    }

    @PostMapping("/events/{eventId}/delete")
    public String deleteEventForm(@PathVariable("eventId") Long eventId) {
        eventService.deleteEvent(eventId);

        return "redirect:/events"; // Перенаправление на страницу с мероприятиями после удаления
    }



}
