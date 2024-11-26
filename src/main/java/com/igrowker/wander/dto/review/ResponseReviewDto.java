package com.igrowker.wander.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseReviewDto {
    private String id;
    private String userId;
    private Double rating;
    private String comment;
    private Date createdAt;
}
