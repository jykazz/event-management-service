package ru.rsreu.lutikov.sber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.rsreu.lutikov.sber.domain.Event;
import ru.rsreu.lutikov.sber.domain.Review;
import ru.rsreu.lutikov.sber.domain.Ticket;
import ru.rsreu.lutikov.sber.domain.User;
import ru.rsreu.lutikov.sber.services.EventService;
import ru.rsreu.lutikov.sber.services.ReviewService;
import ru.rsreu.lutikov.sber.services.UserService;

import java.security.Principal;
import java.util.List;

@Controller
public class NewReviewController {

    @Autowired
    EventService eventService;

    @Autowired
    UserService userService;

    @Autowired
    ReviewService reviewService;

    @GetMapping("/reviews/{eventId}")
    public String vievReviewForm(@PathVariable Long eventId, Model model) {
        List<Review> yourDataList = reviewService.getReviewsByEventId(eventId);
        model.addAttribute("yourDataList", yourDataList); // Передача данных в модель представления
        return "reviews";
    }

    @GetMapping("/reviews/{eventId}/create")
    public String createReviewForm(@PathVariable Long eventId, Model model) {
        model.addAttribute("review", new Review());
        return "newReview";
    }

    @PostMapping("/reviews/{eventId}/create")
    public String createReview(@ModelAttribute("review") Review review, @PathVariable("eventId") Long eventId, Principal principal) {
        System.out.println("Error!");

        String username = principal.getName();

        Long userId = userService.getUserIdByUsername(username);

        User user = userService.getUserById(userId);

        Event event = eventService.getEventById(eventId);

        review.setEvent(event);
        review.setUser(user);

        reviewService.createReview(review);

        // Перенаправить пользователя на страницу со списком билетов или другую страницу
        return "redirect:/events";
    }

}
