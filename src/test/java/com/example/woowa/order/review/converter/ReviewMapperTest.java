package com.example.woowa.order.review.converter;

import static org.junit.jupiter.api.Assertions.*;

import com.example.woowa.customer.voucher.converter.VoucherMapper;
import com.example.woowa.customer.voucher.dto.VoucherCreateRequest;
import com.example.woowa.customer.voucher.dto.VoucherFindResponse;
import com.example.woowa.customer.voucher.entity.Voucher;
import com.example.woowa.customer.voucher.enums.EventType;
import com.example.woowa.customer.voucher.enums.VoucherType;
import com.example.woowa.order.order.entity.Order;
import com.example.woowa.order.review.dto.ReviewCreateRequest;
import com.example.woowa.order.review.dto.ReviewFindResponse;
import com.example.woowa.order.review.entity.Review;
import com.example.woowa.order.review.enums.ScoreType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ReviewMapperTest {

  @Autowired
  private ReviewMapper reviewMapper;

  @Test
  @DisplayName("리뷰 dto 변환")
  void toReviewDto() {
    Review review = new Review("너무너무 맛있어요~~", ScoreType.FIVE, null, null);

    ReviewFindResponse reviewFindResponse = reviewMapper.toReviewDto(review);

    Assertions.assertThat(reviewFindResponse.getContent()).isEqualTo("너무너무 맛있어요~~");
    Assertions.assertThat(reviewFindResponse.getScoreType()).isEqualTo(5);
  }

  @Test
  @DisplayName("리뷰 엔티티 변환")
  void toReview() {
    ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("너무너무 맛있어요~~", ScoreType.FIVE.getValue());

    Review review = reviewMapper.toReview(reviewCreateRequest, null, null);

    Assertions.assertThat(review.getContent()).isEqualTo("너무너무 맛있어요~~");
    Assertions.assertThat(review.getScoreType()).isEqualTo(ScoreType.FIVE);
  }
}
