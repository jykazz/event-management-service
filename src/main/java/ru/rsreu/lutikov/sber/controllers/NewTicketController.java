/**
 * Controller class for handling ticket-related requests.
 *
 * <p>
 * This class is responsible for handling requests related to tickets, such as viewing tickets and buying tickets.
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
 * @see org.springframework.security.core.Authentication
 * @see org.springframework.ui.Model
 * @see org.springframework.web.bind.annotation.GetMapping
 * @see org.springframework.web.bind.annotation.PathVariable
 * @see ru.rsreu.lutikov.sber.domain.Event
 * @see ru.rsreu.lutikov.sber.domain.Ticket
 * @see ru.rsreu.lutikov.sber.domain.User
 * @see ru.rsreu.lutikov.sber.services.EventService
 * @see ru.rsreu.lutikov.sber.services.TicketService
 * @see ru.rsreu.lutikov.sber.services.UserService
 * @since 2023
 */

package ru.rsreu.lutikov.sber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.rsreu.lutikov.sber.domain.Event;
import ru.rsreu.lutikov.sber.domain.Ticket;
import ru.rsreu.lutikov.sber.domain.User;
import ru.rsreu.lutikov.sber.services.EventService;
import ru.rsreu.lutikov.sber.services.TicketService;
import ru.rsreu.lutikov.sber.services.UserService;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Controller
public class NewTicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    /**
     * Handles requests to the "/tickets" path.
     *
     * @param model            the Model object for passing data to the view
     * @param authentication  the Authentication object representing the current authentication
     * @return the name of the view to be rendered
     */
    @GetMapping("/tickets")
    public String tickets(Model model, Authentication authentication) {
        List<Ticket> yourDataList;

        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            // If the user is an admin, retrieve all tickets
            yourDataList = ticketService.getAllTickets();
        } else if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            // If the user is a regular user, retrieve only their own tickets
            String user = authentication.getName();
            Long userId = userService.getUserIdByUsername(user);
            System.out.println(userId);
            yourDataList = ticketService.getTicketsByUserId(userId);
        } else {
            // If the user is not authenticated, handle this situation as desired
            yourDataList = Collections.emptyList();
        }

        model.addAttribute("yourDataList", yourDataList); // Pass data to the view model
        return "tickets"; // Return the name of the view to be rendered
    }

    /**
     * Handles requests to buy a ticket for a specific event.
     *
     * @param eventId     the ID of the event to buy a ticket for
     * @param principal   the Principal object representing the current authenticated user
     * @return the redirect view name after buying the ticket
     */
    @GetMapping("/tickets/{eventId}/buy")
    public String buyTicket(@PathVariable("eventId") Long eventId, Principal principal) {
        // Get the name of the authenticated user from Principal
        String username = principal.getName();

        // Get the user ID by their username
        Long userId = userService.getUserIdByUsername(username);

        User user = userService.getUserById(userId);

        Event event = eventService.getEventById(eventId);

        Ticket ticket = new Ticket(user, event);

        ticketService.createTicket(ticket);

        // Redirect the user to the tickets page or another page
        return "redirect:/tickets";
    }

}
