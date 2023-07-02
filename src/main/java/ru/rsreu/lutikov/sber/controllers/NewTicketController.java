package ru.rsreu.lutikov.sber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.rsreu.lutikov.sber.domain.Event;
import ru.rsreu.lutikov.sber.domain.Ticket;
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

//    @GetMapping("/tickets")
//    public String events(Model model) {
//        List<Ticket> yourDataList = ticketService.getAllTickets(); // Получение данных JSON
//        model.addAttribute("yourDataList", yourDataList); // Передача данных в модель представления
//        return "tickets"; // Возвращаем имя представления
//    }

    @GetMapping("/tickets")
    public String tickets(Model model, Authentication authentication) {
        List<Ticket> yourDataList;

        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            // Если пользователь является администратором, получите все билеты
            yourDataList = ticketService.getAllTickets();
        } else if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            // Если пользователь является обычным пользователем, получите только его собственные билеты
            String user = authentication.getName();
            Long userId = userService.getUserIdByUsername(user);
            System.out.println(userId);
            yourDataList = ticketService.getTicketsByUserId(userId);
        } else {
            // Если пользователь не аутентифицирован, обработайте эту ситуацию по вашему усмотрению
            yourDataList = Collections.emptyList();
        }

        model.addAttribute("yourDataList", yourDataList); // Передача данных в модель представления
        return "tickets"; // Возвращаем имя представления
    }

    @GetMapping("/tickets/{eventId}/buy")
    public String buyTicket(@PathVariable("eventId") Long eventId, Principal principal) {
        // Получить имя авторизованного пользователя из Principal
        String username = principal.getName();

        // Получить идентификатор пользователя по его имени
        Long userId = userService.getUserIdByUsername(username);

        User user = userService.getUserById(userId);

        Event event = eventService.getEventById(eventId);

        Ticket ticket = new Ticket(user, event);

        ticketService.createTicket(ticket);


//        // Проверить, что пользователь существует
//        if (userId == null) {
//            // Обработка ошибки - пользователь не найден
//            return "error";
//        }
//
//        // Получить билет по его идентификатору
//        Ticket ticket = ticketService.getTicketById(ticketId);
//
//        // Проверить, что билет существует
//        if (ticket == null) {
//            // Обработка ошибки - билет не найден
//            return "error";
//        }
//
//        // Назначить идентификатор пользователя для билета
//        ticket.setUser(user);
//
//        // Сохранить обновленный билет
//        ticketService.updateTicket(ticketId, ticket);

        // Перенаправить пользователя на страницу со списком билетов или другую страницу
        return "redirect:/tickets";
    }

}

