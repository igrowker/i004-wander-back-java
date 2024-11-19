package com.igrowker.wander.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.igrowker.wander.entity.ExperienceEntity;
import com.igrowker.wander.entity.ReviewEntity;
import com.igrowker.wander.repository.ExperienceRepository;
import com.igrowker.wander.repository.ReviewRepository;
import com.igrowker.wander.service.ReviewService;

import jakarta.validation.Valid;

@Service
public class ReviewServiceImpl implements ReviewService{

	@Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ExperienceRepository experienceRepository;

    @Override
    public ReviewEntity addReview(@Valid ReviewEntity review) {
        // Step 1: Save the review to the database
        ReviewEntity savedReview = reviewRepository.save(review);

        // Step 2: Retrieve all reviews for the experience to calculate the new average rating
        List<ReviewEntity> allReviews = reviewRepository.findByExperienceId(review.getExperienceId());
        
        double sumRatings = allReviews.stream()
                .mapToDouble(ReviewEntity::getRating)
                .sum();
        double averageRating = sumRatings / allReviews.size();

        // Step 3: Update the experience's rating
        ExperienceEntity experience = experienceRepository.findById(review.getExperienceId())
                .orElseThrow(() -> new IllegalArgumentException("Experience not found with ID: " + review.getExperienceId()));
        experience.setRating(averageRating);
        experienceRepository.save(experience);

        return savedReview;
    }
}
