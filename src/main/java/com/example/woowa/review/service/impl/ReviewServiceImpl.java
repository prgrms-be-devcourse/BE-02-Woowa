package com.example.woowa.review.service.impl;

import com.example.woowa.review.repository.ReviewRepository;
import com.example.woowa.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
  private final ReviewRepository reviewRepository;
}
