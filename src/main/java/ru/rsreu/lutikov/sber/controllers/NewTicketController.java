package ru.rsreu.lutikov.sber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.rsreu.lutikov.sber.domain.Ticket;
import ru.rsreu.lutikov.sber.domain.Ticket;
import ru.rsreu.lutikov.sber.services.TicketService;

import java.util.List;

@Controller
public class NewTicketController {
    @Autowired
    private TicketService ticketService;

    @GetMapping("/tickets")
    public String events(Model model) {
        List<Ticket> yourDataList = ticketService.getAllTickets(); // Получение данных JSON
        model.addAttribute("yourDataList", yourDataList); // Передача данных в модель представления
        return "tickets"; // Возвращаем имя представления
    }
}

