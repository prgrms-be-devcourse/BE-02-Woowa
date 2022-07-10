package com.example.woowa.order.review.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.customer.service.CustomerService;
import com.example.woowa.delivery.entity.Delivery;
import com.example.woowa.delivery.enums.DeliveryStatus;
import com.example.woowa.order.order.entity.Order;
import com.example.woowa.order.order.service.OrderService;
import com.example.woowa.order.review.dto.ReviewCreateRequest;
import com.example.woowa.order.review.dto.ReviewFindResponse;
import com.example.woowa.order.review.dto.ReviewUpdateRequest;
import com.example.woowa.order.review.entity.Review;
import com.example.woowa.order.review.enums.ScoreType;
import com.example.woowa.order.review.repository.ReviewRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
  @Autowired
  private ReviewService reviewService;

  @MockBean
  private CustomerService customerService;

  @MockBean
  private OrderService orderService;

  @MockBean
  private Order order;

  @MockBean
  private Delivery delivery;

  @MockBean
  private ReviewRepository reviewRepository;

  @Test
  @DisplayName("리뷰 생성")
  void createReview() {
    CustomerGrade customerGrade = new CustomerGrade(5, "일반", 3000, 2);
    Customer customer = new Customer("dev12","Programmers123!", LocalDate.of(2000,1,1), customerGrade);
    Review review = new Review("정말정말 맛있습니다.", ScoreType.FIVE, null, null);
    given(customerService.findCustomerEntity(anyString())).willReturn(customer);
    given(delivery.getDeliveryStatus()).willReturn(DeliveryStatus.DELIVERY_FINISH);
    given(order.getDelivery()).willReturn(delivery);
    given(orderService.findOrderById(anyLong())).willReturn(order);
    given(reviewRepository.save(any())).willReturn(review);

    ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("정말정말 맛있습니다.", 5);
    ReviewFindResponse reviewFindResponse = reviewService.createReview("dev12", 1l, reviewCreateRequest);

    Assertions.assertThat(reviewFindResponse.getContent()).isEqualTo("정말정말 맛있습니다.");
    Assertions.assertThat(reviewFindResponse.getScoreType()).isEqualTo(ScoreType.FIVE.getValue());
  }

  @Test
  @DisplayName("리뷰 조회")
  void findReview() {
    given(reviewRepository.findById(anyLong())).willReturn(Optional.of(new Review("정말정말 맛있습니다.",
        ScoreType.FIVE, null, null)));

    ReviewFindResponse reviewFindResponse = reviewService.findReview(1l);

    Assertions.assertThat(reviewFindResponse.getContent()).isEqualTo("정말정말 맛있습니다.");
    Assertions.assertThat(reviewFindResponse.getScoreType()).isEqualTo(ScoreType.FIVE.getValue());
  }

  @Test
  @DisplayName("유저 리뷰 목록 조회")
  void findUserReview() {
    CustomerGrade customerGrade = new CustomerGrade(5, "일반", 3000, 2);
    Customer customer = new Customer("dev12","Programmers123!", LocalDate.of(2000,1,1), customerGrade);
    customer.addReview(new Review("정말정말 맛있습니다.",
        ScoreType.FIVE, null, null));
    given(customerService.findCustomerEntity(anyString())).willReturn(customer);

    List<ReviewFindResponse> reviews = reviewService.findUserReview("dev12");

    Assertions.assertThat(reviews.get(0).getContent()).isEqualTo("정말정말 맛있습니다.");
    Assertions.assertThat(reviews.get(0).getScoreType()).isEqualTo(ScoreType.FIVE.getValue());
  }

  @Test
  @DisplayName("리뷰 업데이트")
  void updateReview() {
    given(reviewRepository.findById(anyLong())).willReturn(Optional.of(new Review("정말정말 맛있습니다.",
        ScoreType.FIVE, null, null)));

    ReviewUpdateRequest reviewUpdateRequest = new ReviewUpdateRequest("정말정말 맛없습니다.", 1);
    ReviewFindResponse reviewFindResponse = reviewService.updateReview(1l, reviewUpdateRequest);

    Assertions.assertThat(reviewFindResponse.getContent()).isEqualTo("정말정말 맛없습니다.");
    Assertions.assertThat(reviewFindResponse.getScoreType()).isEqualTo(ScoreType.ONE.getValue());
  }

  @Test
  @DisplayName("리뷰 삭제")
  void deleteReview() {
    CustomerGrade customerGrade = new CustomerGrade(5, "일반", 3000, 2);
    Customer customer = new Customer("dev12","Programmers123!", LocalDate.of(2000,1,1), customerGrade);
    Review review = new Review("정말정말 맛있습니다.",
        ScoreType.FIVE, null, null);
    customer.addReview(review);
    given(customerService.findCustomerEntity(anyString())).willReturn(customer);
    given(reviewRepository.findById(anyLong())).willReturn(Optional.of(review));

    reviewService.deleteReview("dev12", 1l);

    verify(reviewRepository).delete(review);
  }
}
