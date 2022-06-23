package com.example.woowa.review.converter;

import com.example.woowa.review.dto.ReviewDto;
import com.example.woowa.review.entity.Review;

public class ReviewConverter {
    public static ReviewDto toReviewDto(Review review) {
        return new ReviewDto(review.getContent(), review.getScoreType());
    }
}
