package com.example.woowa.order.review.converter;

import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.order.order.entity.Order;
import com.example.woowa.order.review.dto.ReviewCreateRequest;
import com.example.woowa.order.review.dto.ReviewFindResponse;
import com.example.woowa.order.review.entity.Review;
import com.example.woowa.order.review.enums.ScoreType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL, componentModel = "spring", imports = {ScoreType.class})
public interface ReviewMapper {

  @Mapping(target = "scoreType", expression = "java(review.getScoreType().getValue())")
  ReviewFindResponse toReviewDto(Review review);
  @Mappings({
      @Mapping(target = "customer", source = "customer"),
      @Mapping(target = "scoreType", expression = "java(ScoreType.find(reviewCreateRequest.getScoreType()))"),
      @Mapping(target = "order", ignore = true)
  })
  Review toReview(ReviewCreateRequest reviewCreateRequest, Customer customer, Order order);
}
