package com.example.woowa.order.review.controller;

import com.example.woowa.order.review.dto.ReviewCreateRequest;
import com.example.woowa.order.review.dto.ReviewFindResponse;
import com.example.woowa.order.review.dto.ReviewUpdateRequest;
import com.example.woowa.order.review.service.ReviewService;

import java.util.List;
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
    private final ReviewService reviewService;

    @PostMapping("/{loginId}/{orderId}")
    public ReviewFindResponse createReview(@PathVariable String loginId, @PathVariable Long orderId, @RequestBody @Valid ReviewCreateRequest reviewCreateRequest) {
        return reviewService.createReview(loginId, orderId, reviewCreateRequest);
    }

    @GetMapping("/{id}")
    public ReviewFindResponse findReview(@PathVariable Long id) {
        return reviewService.findReview(id);
    }

    @GetMapping("/{loginId}")
    public List<ReviewFindResponse> findUserReview(@PathVariable String loginId) {
        return reviewService.findUserReview(loginId);
    }

    @PutMapping("/{id}")
    public ReviewFindResponse updateReview(@PathVariable Long id, @RequestBody @Valid ReviewUpdateRequest reviewUpdateRequest) {
        return reviewService.updateReview(id, reviewUpdateRequest);
    }

    @DeleteMapping("/{id}")
    public String deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return "delete id - " + id;
    }
}
