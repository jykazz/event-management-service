package ru.rsreu.lutikov.sber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.rsreu.lutikov.sber.domain.Event;
import ru.rsreu.lutikov.sber.services.EventService;

import java.util.List;

@Controller
public class NewEventController {
    @Autowired
    private EventService eventService;

    @GetMapping("/events")
    public String events(Model model) {
        List<Event> yourDataList = eventService.getAllEvents(); // Получение данных JSON
        model.addAttribute("yourDataList", yourDataList); // Передача данных в модель представления
        return "events"; // Возвращаем имя представления
    }
}
