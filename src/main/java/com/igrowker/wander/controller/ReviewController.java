package com.igrowker.wander.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.igrowker.wander.entity.ReviewEntity;
import com.igrowker.wander.service.ReviewService;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /**
     * Endpoint to add a new review
     * @param review The review data to be added
     * @return Response indicating if the review was added successfully
     */
    @PostMapping
    public ResponseEntity<String> addReview(@Valid @RequestBody ReviewEntity review) {
        try {
            // Delegate adding the review to the service layer
            reviewService.addReview(review);
            return ResponseEntity.status(HttpStatus.CREATED).body("Review added successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error in the review data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding the review: " + e.getMessage());
        }
    }
}	
