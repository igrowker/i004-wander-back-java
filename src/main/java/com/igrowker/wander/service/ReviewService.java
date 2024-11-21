package com.igrowker.wander.service;

import com.igrowker.wander.dto.review.RequestReviewDto;
import com.igrowker.wander.entity.ReviewEntity;

public interface ReviewService {

    ReviewEntity addReview(RequestReviewDto review);
}
