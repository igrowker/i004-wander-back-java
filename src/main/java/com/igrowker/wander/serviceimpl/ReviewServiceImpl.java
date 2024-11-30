package com.igrowker.wander.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import com.igrowker.wander.dto.review.ResponseReviewDto;
import com.igrowker.wander.entity.User;
import com.igrowker.wander.exception.InvalidUserCredentialsException;
import com.igrowker.wander.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.igrowker.wander.dto.review.RequestReviewDto;
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
    public List<ResponseReviewDto> getReviewsByExperience(String experienceId) {
        if (experienceId == null || experienceId.isEmpty()) {
            throw new IllegalArgumentException("Experience id can't be null or empty.");
        }
        List<ReviewEntity> reviews = reviewRepository.findByExperienceIdOrderByCreatedAtDesc(experienceId);

        return reviews.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewEntity addReview(@Valid RequestReviewDto reviewDto) {
        ReviewEntity review = new ReviewEntity();
        review.setExperienceId(reviewDto.getExperienceId());
        review.setUserId(reviewDto.getUserId());
        review.setRating(reviewDto.getRating());
        review.setComment(reviewDto.getComment());
        review.setCreatedAt(reviewDto.getCreatedAt());

        ReviewEntity savedReview = reviewRepository.save(review);

        List<ReviewEntity> allReviews = reviewRepository.findByExperienceId(review.getExperienceId());
        
        double sumRatings = allReviews.stream()
                .mapToDouble(ReviewEntity::getRating)
                .sum();
        double averageRating = sumRatings / allReviews.size();

        ExperienceEntity experience = experienceRepository.findById(review.getExperienceId())
                .orElseThrow(() -> new IllegalArgumentException("Experience not found with ID: " + review.getExperienceId()));
        experience.setRating(averageRating);
        experienceRepository.save(experience);

        return savedReview;
    }

    @Override
    public ResponseReviewDto deleteReview(String id) {
        ReviewEntity review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró reseña con id: " + id));

        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!review.getUserId().equals(authenticatedUser.getId())) {
            throw new IllegalArgumentException("Solo puedes eliminar tus propias reseñas");
        }

        if (authenticatedUser.getRole().equals("PROVIDER")) {
            throw new InvalidUserCredentialsException("Proveedores no pueden eliminar reseñas");
        }

        reviewRepository.delete(review);
        return convertToResponseDto(review);
    }


    private ResponseReviewDto convertToResponseDto(ReviewEntity review) {
        ResponseReviewDto responseDto = new ResponseReviewDto();
        responseDto.setId(review.getId());
        responseDto.setUserId(review.getUserId());
        responseDto.setRating(review.getRating());
        responseDto.setComment(review.getComment());
        responseDto.setCreatedAt(review.getCreatedAt());
        return responseDto;
    }

}
