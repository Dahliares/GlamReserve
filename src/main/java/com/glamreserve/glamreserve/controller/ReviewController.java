package com.glamreserve.glamreserve.controller;

import com.glamreserve.glamreserve.entities.interceptor.ForbiddenAccessException;
import com.glamreserve.glamreserve.entities.review.Review;
import com.glamreserve.glamreserve.entities.review.ReviewRepository;
import com.glamreserve.glamreserve.entities.user.User;
import com.glamreserve.glamreserve.entities.user.UserRepository;
import com.glamreserve.glamreserve.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    // Create
    @PostMapping("/create")
    public ResponseEntity<Review> createReview(@RequestHeader("Authorization") String token, @RequestBody Review newReview) {
        User user = AuthenticationService.getUserFromToken(token, userRepository);
        Review savedReview = reviewRepository.save(newReview);
        return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
    }

    // Read by company id
    @GetMapping("/company/{company_id}")
    public ResponseEntity<List<Review>> getReviewsByCompanyId(@RequestHeader("Authorization") String token, @PathVariable("company_id") Long companyId) {
        User user = AuthenticationService.getUserFromToken(token, userRepository);
        List<Review> reviews = reviewRepository.findByCompanyId(companyId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    // Update
    @PutMapping("/update/{id}")
    public ResponseEntity<Review> updateReview(@RequestHeader("Authorization") String token, @PathVariable("id") Long id, @RequestBody Review updatedReview) {
        User user = AuthenticationService.getUserFromToken(token, userRepository);
        Review existingReview = reviewRepository.findById(id).orElse(null);
        if (existingReview == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        if(!Objects.equals(existingReview.getUser().getId(), user.getId())){
            throw new ForbiddenAccessException("Acceso denegado.");
        }

        existingReview.setCompany(updatedReview.getCompany());
        existingReview.setUser(updatedReview.getUser());
        existingReview.setScore(updatedReview.getScore());
        existingReview.setComment(updatedReview.getComment());

        Review savedReview = reviewRepository.save(existingReview);
        return new ResponseEntity<>(savedReview, HttpStatus.OK);
    }

    // Delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReview(@RequestHeader("Authorization") String token, @PathVariable("id") Long id) {
        User user = AuthenticationService.getUserFromToken(token, userRepository);
        Review existingReview = reviewRepository.findById(id).orElse(null);
        if (existingReview == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        if(!Objects.equals(existingReview.getUser().getId(), user.getId())){
            throw new ForbiddenAccessException("Acceso denegado.");
        }

        reviewRepository.delete(existingReview);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}