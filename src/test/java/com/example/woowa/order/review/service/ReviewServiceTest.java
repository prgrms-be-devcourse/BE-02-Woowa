package com.example.woowa.order.review.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.woowa.customer.customer.dto.CustomerAddressCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerFindResponse;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.customer.repository.CustomerAddressRepository;
import com.example.woowa.customer.customer.repository.CustomerRepository;
import com.example.woowa.customer.customer.service.CustomerGradeService;
import com.example.woowa.customer.customer.service.CustomerService;
import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.delivery.service.AreaCodeService;
import com.example.woowa.order.order.service.OrderService;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
  @Autowired
  private CustomerService customerService;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private CustomerAddressRepository customerAddressRepository;

  @Autowired
  private ReviewService reviewService;

  @Autowired
  private ReviewRepository reviewRepository;

  @MockBean
  private OrderService orderService;

  @MockBean
  private CustomerGradeService customerGradeService;

  @MockBean
  private AreaCodeService areaCodeService;

  public String getCustomerLoginId() {
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울특별시 동작구 상도동","빌라 101호","집");
    CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("dev12","Programmers123!", "2000-01-01", customerAddressCreateRequest);
    CustomerFindResponse customerFindResponse = customerService.createCustomer(
        customerCreateRequest);
    return customerFindResponse.getLoginId();
  }

  @BeforeEach
  void settingBeforeTest() {
    reviewRepository.deleteAll();
    customerAddressRepository.deleteAll();
    customerRepository.deleteAll();
    given(customerGradeService.findDefaultCustomerGrade()).willReturn(new CustomerGrade(1, "일반",3000, 2));
    given(areaCodeService.findByAddress(any())).willReturn(new AreaCode("1", "서울특별시 동작구", false));
    given(orderService.findOrderById(any())).willReturn(null);
  }

  @AfterEach
  void settingAfterTest() {
    reviewRepository.deleteAll();
    customerAddressRepository.deleteAll();
    customerRepository.deleteAll();
  }

  @Test
  @DisplayName("리뷰 생성")
  void createReview() {
    String customerId = getCustomerLoginId();
    ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("정말정말 맛있습니다.", 5);
    given(orderService.findOrderById(any())).willReturn(null);

    ReviewFindResponse reviewFindResponse = reviewService.createReview(customerId, null, reviewCreateRequest);

    Assertions.assertThat(reviewFindResponse.getContent()).isEqualTo("정말정말 맛있습니다.");
    Assertions.assertThat(reviewFindResponse.getScoreType()).isEqualTo(ScoreType.FIVE.getValue());
  }

  @Test
  @DisplayName("리뷰 조회")
  void findReview() {
    String customerId = getCustomerLoginId();
    ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("정말정말 맛있습니다.", 5);
    given(orderService.findOrderById(any())).willReturn(null);
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
    given(orderService.findOrderById(any())).willReturn(null);
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
    given(orderService.findOrderById(any())).willReturn(null);
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
    given(orderService.findOrderById(any())).willReturn(null);
    ReviewFindResponse reviewFindResponse = reviewService.createReview(customerId, null, reviewCreateRequest);

    reviewService.deleteReview(reviewFindResponse.getId());

    Assertions.assertThat(reviewRepository.count()).isEqualTo(0l);
  }
}
