package com.igrowker.wander.service;

import com.igrowker.wander.entity.ReviewEntity;

public interface ReviewService {

	// Method to add a review and handle business logic related to reviews
    ReviewEntity addReview(ReviewEntity review);
}
