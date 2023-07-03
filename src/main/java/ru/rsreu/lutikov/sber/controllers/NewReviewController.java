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

    @GetMapping("/user/reviews")
    public String viewUserReviews(Model model, Principal principal) {
        String username = principal.getName();
        Long id = userService.getUserIdByUsername(username);

        List<Review> userReviews = reviewService.getReviewsByUserId(id);
        model.addAttribute("userReviews", userReviews);

        return "userReviews";
    }


    @GetMapping("/user/reviews/{reviewId}/edit")
    public String editReviewForm(@PathVariable Long reviewId, Model model) {
        Review review = reviewService.getReviewById(reviewId);
        if (review != null) {
            model.addAttribute("review", review);
            return "editReview";
        } else {
            return "redirect:/user/reviews";
        }
    }

    @PostMapping("/user/reviews/{reviewId}/edit")
    public String editReview(@ModelAttribute("review") Review editedReview, @PathVariable("reviewId") Long reviewId) {
        Review review = reviewService.getReviewById(reviewId);
        if (review != null) {
            review.setComment(editedReview.getComment());
            reviewService.updateReview(reviewId, review);
        }
        return "redirect:/user/reviews";
    }

    @GetMapping("/user/reviews/{reviewId}/delete")
    public String deleteReviewForm(@PathVariable Long reviewId, Model model) {
        Review review = reviewService.getReviewById(reviewId);
        model.addAttribute("review", review);
        return "deleteReview";
    }

    @PostMapping("/user/reviews/{reviewId}/delete")
    public String deleteReviewForm(@PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReview(reviewId);

        return "redirect:/user/reviews"; // Перенаправление на страницу с пользователями после удаления
    }

}
