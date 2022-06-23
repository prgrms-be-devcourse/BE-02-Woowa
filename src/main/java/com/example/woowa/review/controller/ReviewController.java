package com.example.woowa.review.controller;

import com.example.woowa.review.dto.CreateReviewDto;
import com.example.woowa.review.dto.ReviewDto;
import com.example.woowa.review.service.ReviewService;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private ReviewService reviewService;

    @PostMapping
    public ReviewDto createReview(@RequestBody @Valid CreateReviewDto createReviewDto) {
        return null;
    }

    @GetMapping("/{id}")
    public ReviewDto readReview(@PathVariable Long id) {
        return null;
    }

    @PutMapping
    public ReviewDto updateReview(@RequestBody @Valid CreateReviewDto createReviewDto) {
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        return;
    }
}
