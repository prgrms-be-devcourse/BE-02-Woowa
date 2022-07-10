package com.example.woowa.order.review.service;

import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.service.CustomerService;
import com.example.woowa.delivery.enums.DeliveryStatus;
import com.example.woowa.order.order.entity.Order;
import com.example.woowa.order.order.service.OrderService;
import com.example.woowa.order.review.converter.ReviewMapper;
import com.example.woowa.order.review.dto.ReviewCreateRequest;
import com.example.woowa.order.review.dto.ReviewFindResponse;
import com.example.woowa.order.review.dto.ReviewUpdateRequest;
import com.example.woowa.order.review.entity.Review;
import com.example.woowa.order.review.repository.ReviewRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CustomerService customerService;
    private final OrderService orderService;
    private final ReviewMapper reviewMapper;

    @Transactional
    public ReviewFindResponse createReview(String loginId, Long orderId,
        ReviewCreateRequest reviewCreateRequest) {
        Customer customer = customerService.findCustomerEntity(loginId);
        Order order = orderService.findOrderById(orderId);
        if (order.getDelivery().getDeliveryStatus() == DeliveryStatus.DELIVERY_FINISH) {
            Review review = reviewMapper.toReview(reviewCreateRequest, customer, order);
            review = reviewRepository.save(review);
            customer.addReview(review);
            return reviewMapper.toReviewDto(review);
        } else {
            throw new RuntimeException("배달 완료된 주문에 대해서만 리뷰가 가능합니다.");
        }
    }

    public ReviewFindResponse findReview(Long id) {
        Review review = findReviewEntity(id);
        return reviewMapper.toReviewDto(review);
    }

    public List<ReviewFindResponse> findUserReview(String loginId) {
        Customer customer = customerService.findCustomerEntity(loginId);
        return customer.getReviews().stream().map(reviewMapper::toReviewDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public ReviewFindResponse updateReview(Long id, ReviewUpdateRequest reviewUpdateRequest) {
        Review review = findReviewEntity(id);
        if (reviewUpdateRequest.getContent() != null) {
            review.setContent(reviewUpdateRequest.getContent());
        }
        if (reviewUpdateRequest.getScoreType() != null) {
            review.setScoreType(reviewUpdateRequest.getScoreType());
        }
        return reviewMapper.toReviewDto(review);
    }

    @Transactional
    public void deleteReview(Long id) {
        Review review = findReviewEntity(id);
        review.getCustomer().removeReview(review);
        reviewRepository.delete(review);
    }

    private Review findReviewEntity(Long id) {
        return reviewRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("review not existed"));
    }
}
