/**
 * Controller class for handling event-related requests.
 *
 * <p>
 * This class is responsible for handling requests related to events, such as displaying events,
 * creating new events, editing events, and deleting events.
 * </p>
 *
 * <p>
 * <strong>Author:</strong> Vadim
 * <br>
 * <strong>Email:</strong> blinvadik@mail.ru
 * </p>
 *
 * @see org.springframework.stereotype.Controller
 * @see org.springframework.beans.factory.annotation.Autowired
 * @see org.springframework.data.domain.Page
 * @see org.springframework.data.domain.PageRequest
 * @see org.springframework.ui.Model
 * @see org.springframework.validation.BindingResult
 * @see org.springframework.web.bind.annotation
 * @see ru.rsreu.lutikov.sber.domain.Event
 * @see ru.rsreu.lutikov.sber.repositories.EventRepository
 * @see ru.rsreu.lutikov.sber.services.EventService
 *
 * @since 2023
 */

package ru.rsreu.lutikov.sber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    /**
     * Handles requests to the "/events" path.
     *
     * @param page    the page number
     * @param search  the search query string (optional)
     * @param model   the Model object for passing data to the view
     * @return the name of the view to be rendered
     */
    @GetMapping("/events")
    public String events(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "search", required = false) String search,
                         Model model) {
        int pageSize = 9; // Number of cards per page
        Page<Event> eventPage;

        if (search != null && !search.isEmpty()) {
            eventPage = eventRepository.findByEventNameContainingIgnoreCase(search, PageRequest.of(page - 1, pageSize));
        } else {
            eventPage = eventRepository.findAll(PageRequest.of(page - 1, pageSize));
        }

        List<Event> yourDataList = eventPage.getContent(); // Get JSON data
        model.addAttribute("yourDataList", yourDataList); // Pass data to the view model
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", eventPage.getTotalPages());
        return "events"; // Return the view name
    }

    /**
     * Handles requests to the "/events/new" path.
     *
     * @param model the Model object for passing data to the view
     * @return the name of the view to be rendered
     */
    @GetMapping("/events/new")
    public String createEventForm(Model model) {
        model.addAttribute("event", new Event());
        return "newEvent";
    }

    /**
     * Handles requests to create a new event.
     *
     * @param event          the Event object containing the event data
     * @param bindingResult  the BindingResult object for validation errors
     * @return the redirect view name after creating the event
     */
    @PostMapping("/events/new")
    public String createEvent(@ModelAttribute("event") Event event, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println("ERROR");
            return "newEvent";
        }
        eventRepository.save(event);
        return "redirect:/events";
    }

    /**
     * Handles requests to the "/events/{eventId}/edit" path.
     *
     * @param eventId  the ID of the event to be edited
     * @param model    the Model object for passing data to the view
     * @return the name of the view to be

    rendered
     */
    @GetMapping("/events/{eventId}/edit")
    public String editEventForm(@PathVariable Long eventId, Model model) {
        Event event = eventService.getEventById(eventId);
        model.addAttribute("event", event);
        return "editEvent";
    }

    /**
     * Handles requests to edit an event.
     *
     * @param eventId        the ID of the event to be edited
     * @param event          the Event object containing the updated event data
     * @param bindingResult  the BindingResult object for validation errors
     * @return the redirect view name after editing the event
     */
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

    /**
     * Handles requests to the "/events/{eventId}/delete" path.
     *
     * @param eventId  the ID of the event to be deleted
     * @param model    the Model object for passing data to the view
     * @return the name of the view to be rendered
     */
    @GetMapping("/events/{eventId}/delete")
    public String deleteEventForm(@PathVariable Long eventId, Model model) {
        Event event = eventService.getEventById(eventId);
        model.addAttribute("event", event);
        return "deleteEvent";
    }

    /**
     * Handles requests to delete an event.
     *
     * @param eventId  the ID of the event to be deleted
     * @return the redirect view name after deleting the event
     */
    @PostMapping("/events/{eventId}/delete")
    public String deleteEventForm(@PathVariable("eventId") Long eventId) {
        eventService.deleteEvent(eventId);

        return "redirect:/events"; // Redirect to the events page after deleting
    }
}