//package ru.rsreu.lutikov.sber.controllers;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import ru.rsreu.lutikov.sber.domain.Review;
//import ru.rsreu.lutikov.sber.services.ReviewService;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/reviews")
//public class ReviewController {
//
//    private final ReviewService reviewService;
//
//    public ReviewController(ReviewService reviewService) {
//        this.reviewService = reviewService;
//    }
//
//    @PostMapping
//    public ResponseEntity<Review> createReview(@RequestBody Review review) {
//        Review createdReview = reviewService.createReview(review);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
//        Review review = reviewService.getReviewById(id);
//        if (review != null) {
//            return ResponseEntity.ok(review);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Review>> getAllReviews() {
//        List<Review> reviews = reviewService.getAllReviews();
//        return ResponseEntity.ok(reviews);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review review) {
//        Review updatedReview = reviewService.updateReview(id, review);
//        if (updatedReview != null) {
//            return ResponseEntity.ok(updatedReview);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
//        boolean deleted = reviewService.deleteReview(id);
//        if (deleted) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//}
//
