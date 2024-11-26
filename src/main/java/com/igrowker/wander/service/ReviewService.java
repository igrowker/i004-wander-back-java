package com.igrowker.wander.service;

import com.igrowker.wander.dto.review.RequestReviewDto;
import com.igrowker.wander.dto.review.ResponseReviewDto;
import com.igrowker.wander.entity.ReviewEntity;

import java.util.List;

public interface ReviewService {

    List<ResponseReviewDto> getReviewsByExperience(String experienceId);
    ReviewEntity addReview(RequestReviewDto review);
}
