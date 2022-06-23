package com.example.woowa.order.review.converter;

import com.example.woowa.order.review.dto.ReviewDto;
import com.example.woowa.order.review.entity.Review;

public class ReviewConverter {
    public static ReviewDto toReviewDto(Review review) {
        return new ReviewDto(review.getContent(), review.getScoreType());
    }
}
