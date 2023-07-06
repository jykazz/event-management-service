/**
 * Controller class for handling review-related requests.
 *
 * <p>
 * This class is responsible for handling requests related to reviews, such as viewing reviews,
 * creating new reviews, editing reviews, and deleting reviews.
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
 * @see org.springframework.ui.Model
 * @see org.springframework.web.bind.annotation.GetMapping
 * @see org.springframework.web.bind.annotation.ModelAttribute
 * @see org.springframework.web.bind.annotation.PathVariable
 * @see org.springframework.web.bind.annotation.PostMapping
 * @see ru.rsreu.lutikov.sber.domain.Event
 * @see ru.rsreu.lutikov.sber.domain.Review
 * @see ru.rsreu.lutikov.sber.domain.User
 * @see ru.rsreu.lutikov.sber.services.EventService
 * @see ru.rsreu.lutikov.sber.services.ReviewService
 * @see ru.rsreu.lutikov.sber.services.UserService
 * @since 2023-07-04
 */

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

    /**
     * Handles requests to the "/reviews/{eventId}" path.
     *
     * @param eventId  the ID of the event to view reviews for
     * @param model    the Model object for passing data to the view
     * @return the name of the view to be rendered
     */
    @GetMapping("/reviews/{eventId}")
    public String viewReviewForm(@PathVariable Long eventId, Model model) {
        List<Review> yourDataList = reviewService.getReviewsByEventId(eventId);
        model.addAttribute("yourDataList", yourDataList); // Pass data to the view model
        return "reviews";
    }

    /**
     * Handles requests to the "/reviews/{eventId}/create" path.
     *
     * @param eventId  the ID of the event to create a review for
     * @param model    the Model object for passing data to the view
     * @return the name of the view to be rendered
     */
    @GetMapping("/reviews/{eventId}/create")
    public String createReviewForm(@PathVariable Long eventId, Model model) {
        model.addAttribute("review", new Review());
        return "newReview";
    }

    /**
     * Handles requests to create a new review.
     *
     * @param review     the Review object containing the review data
     * @param eventId    the ID of the event the review belongs to
     * @param principal  the Principal object representing the current authenticated user
     * @return the redirect view name after creating the review
     */
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

        // Redirect the user to the tickets page or another page
        return "redirect:/events";
    }

    /**
     * Handles requests to the "/user/reviews" path.
     *
     * @param model      the Model object for passing data to the view
     *

     @param principal  the Principal object representing the current authenticated user
      * @return the name of the view to be rendered
     */
    @GetMapping("/user/reviews")
    public String viewUserReviews(Model model, Principal principal) {
        String username = principal.getName();
        Long id = userService.getUserIdByUsername(username);

        List<Review> userReviews = reviewService.getReviewsByUserId(id);
        model.addAttribute("userReviews", userReviews);

        return "userReviews";
    }

    /**
     * Handles requests to the "/user/reviews/{reviewId}/edit" path.
     *
     * @param reviewId  the ID of the review to be edited
     * @param model     the Model object for passing data to the view
     * @return the name of the view to be rendered
     */
    @GetMapping("/user/reviews/{reviewId}/edit")
    public String editReviewForm(@PathVariable Long reviewId, Model model, Principal principal) {
        Review review = reviewService.getReviewById(reviewId);
        if (review != null && review.getUser().getUsername().equals(principal.getName())) {
            model.addAttribute("review", review);
            return "editReview";
        } else {
            return "redirect:/user/reviews";
        }
    }

    /**
     * Handles requests to edit a review.
     *
     * @param editedReview  the Review object containing the edited review data
     * @param reviewId      the ID of the review to be edited
     * @return the redirect view name after editing the review
     */
    @PostMapping("/user/reviews/{reviewId}/edit")
    public String editReview(@ModelAttribute("review") Review editedReview, @PathVariable("reviewId") Long reviewId) {
        Review review = reviewService.getReviewById(reviewId);
        if (review != null) {
            review.setComment(editedReview.getComment());
            reviewService.updateReview(reviewId, review);
        }
        return "redirect:/user/reviews";
    }

    /**
     * Handles requests to the "/user/reviews/{reviewId}/delete" path.
     *
     * @param reviewId  the ID of the review to be deleted
     * @param model     the Model object for passing data to the view
     * @return the name of the view to be rendered
     */
    @GetMapping("/user/reviews/{reviewId}/delete")
    public String deleteReviewForm(@PathVariable Long reviewId, Model model) {
        Review review = reviewService.getReviewById(reviewId);
        model.addAttribute("review", review);
        return "deleteReview";
    }

    /**
     * Handles requests to delete a review.
     *
     * @param reviewId  the ID of the review to be deleted
     * @return the redirect view name after deleting the review
     */
    @PostMapping("/user/reviews/{reviewId}/delete")
    public String deleteReview(@PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReview(reviewId);

        return "redirect:/user/reviews"; // Redirect to the users page after deleting
    }

}
