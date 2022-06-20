package com.example.woowa.review.service.impl;

import com.example.woowa.review.repository.ReviewRepository;
import com.example.woowa.review.service.ReviewService;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {
  private ReviewRepository reviewRepository;

  public ReviewServiceImpl(ReviewRepository reviewRepository) {
    this.reviewRepository = reviewRepository;
  }
}
