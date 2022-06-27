package com.example.woowa.order.review.service;

import com.example.woowa.customer.customer.dto.CustomerAddressCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerFindResponse;
import com.example.woowa.customer.customer.dto.CustomerGradeCreateRequest;
import com.example.woowa.customer.customer.repository.CustomerAddressRepository;
import com.example.woowa.customer.customer.repository.CustomerGradeRepository;
import com.example.woowa.customer.customer.repository.CustomerRepository;
import com.example.woowa.customer.customer.service.CustomerGradeService;
import com.example.woowa.customer.customer.service.CustomerService;
import com.example.woowa.order.review.dto.ReviewCreateRequest;
import com.example.woowa.order.review.dto.ReviewFindResponse;
import com.example.woowa.order.review.dto.ReviewUpdateRequest;
import com.example.woowa.order.review.enums.ScoreType;
import com.example.woowa.order.review.repository.ReviewRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReviewServiceTest {
  @Autowired
  private CustomerService customerService;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private CustomerGradeService customerGradeService;

  @Autowired
  private CustomerGradeRepository customerGradeRepository;

  @Autowired
  private CustomerAddressRepository customerAddressRepository;

  @Autowired
  private ReviewService reviewService;

  @Autowired
  private ReviewRepository reviewRepository;

  public void makeDefaultCustomerGrade() {
    CustomerGradeCreateRequest customerGradeCreateRequest = new CustomerGradeCreateRequest(5, "일반", 3000, 2);
    customerGradeService.createCustomerGrade(customerGradeCreateRequest);
  }

  public String getCustomerLoginId() {
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울특별시 동작구 상도동","빌라 101호","집");
    CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("dev12","Programmers123!", "2000-01-01", customerAddressCreateRequest);
    CustomerFindResponse customerFindResponse = customerService.createCustomer(
        customerCreateRequest);
    return customerFindResponse.getLoginId();
  }

  @BeforeEach
  void settingBeforeTest() {
    makeDefaultCustomerGrade();
  }

  @AfterEach
  void settingAfterTest() {
    customerAddressRepository.deleteAll();
    reviewRepository.deleteAll();
    customerRepository.deleteAll();
    customerGradeRepository.deleteAll();
  }

  @Test
  @DisplayName("리뷰 생성")
  void createReview() {
    String customerId = getCustomerLoginId();
    ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("정말정말 맛있습니다.", 5);

    ReviewFindResponse reviewFindResponse = reviewService.createReview(customerId, null, reviewCreateRequest);

    Assertions.assertThat(reviewFindResponse.getContent()).isEqualTo("정말정말 맛있습니다.");
    Assertions.assertThat(reviewFindResponse.getScoreType()).isEqualTo(ScoreType.FIVE.getValue());
  }

  @Test
  @DisplayName("리뷰 조회")
  void findReview() {
    String customerId = getCustomerLoginId();
    ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("정말정말 맛있습니다.", 5);
    ReviewFindResponse reviewFindResponse = reviewService.createReview(customerId, null, reviewCreateRequest);

    reviewFindResponse = reviewService.findReview(reviewFindResponse.getId());

    Assertions.assertThat(reviewFindResponse.getContent()).isEqualTo("정말정말 맛있습니다.");
    Assertions.assertThat(reviewFindResponse.getScoreType()).isEqualTo(ScoreType.FIVE.getValue());
  }

  @Test
  @DisplayName("유저 리뷰 목록 조회")
  void findUserReview() {
    String customerId = getCustomerLoginId();
    ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("정말정말 맛있습니다.", 5);
    reviewService.createReview(customerId, null, reviewCreateRequest);

    List<ReviewFindResponse> reviews = reviewService.findUserReview(customerId);

    Assertions.assertThat(reviews.get(0).getContent()).isEqualTo("정말정말 맛있습니다.");
    Assertions.assertThat(reviews.get(0).getScoreType()).isEqualTo(ScoreType.FIVE.getValue());
  }

  @Test
  @DisplayName("리뷰 업데이트")
  void updateReview() {
    String customerId = getCustomerLoginId();
    ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("정말정말 맛있습니다.", 5);
    ReviewFindResponse reviewFindResponse = reviewService.createReview(customerId, null, reviewCreateRequest);
    ReviewUpdateRequest reviewUpdateRequest = new ReviewUpdateRequest("정말정말 맛없습니다.", 1);

    reviewFindResponse = reviewService.updateReview(reviewFindResponse.getId(), reviewUpdateRequest);
    reviewFindResponse = reviewService.findReview(reviewFindResponse.getId());

    Assertions.assertThat(reviewFindResponse.getContent()).isEqualTo("정말정말 맛없습니다.");
    Assertions.assertThat(reviewFindResponse.getScoreType()).isEqualTo(ScoreType.ONE.getValue());
  }

  @Test
  @DisplayName("리뷰 삭제")
  void deleteReview() {
    String customerId = getCustomerLoginId();
    ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("정말정말 맛있습니다.", 5);
    ReviewFindResponse reviewFindResponse = reviewService.createReview(customerId, null, reviewCreateRequest);

    reviewService.deleteReview(reviewFindResponse.getId());

    Assertions.assertThat(reviewRepository.count()).isEqualTo(0l);
  }
}
