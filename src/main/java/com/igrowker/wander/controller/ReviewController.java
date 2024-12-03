package com.igrowker.wander.controller;

import javax.validation.Valid;

import com.igrowker.wander.dto.review.ResponseReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.igrowker.wander.dto.review.RequestReviewDto;

import com.igrowker.wander.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /**
     * Get all the reviews for a specific experience
     *
     * @param idExperience experience identifier
     * @return list of reviews by experience id
     */
    @GetMapping("/experience/{idExperience}")
    public ResponseEntity<List<ResponseReviewDto>> getReviewsByExperience(@PathVariable String idExperience) {
        List<ResponseReviewDto> reviews = reviewService.getReviewsByExperience(idExperience);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Endpoint to add a new review
     * @param review The review data to be added
     * @return Response indicating if the review was added successfully
     */
    @PostMapping
    public ResponseEntity<String> addReview(@Valid @RequestBody RequestReviewDto reviewDto) {
        try {
            reviewService.addReview(reviewDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Review added successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error in the review data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding the review: " + e.getMessage());
        }
    }

    /**
     * Endpoint to delete a review by id
     * @param id id of the review to be deleted
     * @return Response indicating if the review was deleted successfully
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseReviewDto> deleteReview(@PathVariable String id) {
        ResponseReviewDto deletedReview = reviewService.deleteReview(id);
        return ResponseEntity.ok(deletedReview);
    }
}	
