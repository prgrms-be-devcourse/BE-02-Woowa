package com.example.woowa.customer.customer.converter;

import com.example.woowa.common.EnumFindable;
import com.example.woowa.customer.customer.dto.CustomerAddressCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerAddressFindResponse;
import com.example.woowa.customer.customer.dto.CustomerCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerFindResponse;
import com.example.woowa.customer.customer.dto.CustomerGradeCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerGradeFindResponse;
import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerAddress;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.voucher.dto.VoucherCreateRequest;
import com.example.woowa.customer.voucher.dto.VoucherFindResponse;
import com.example.woowa.customer.voucher.entity.Voucher;
import com.example.woowa.customer.voucher.enums.EventType;
import com.example.woowa.customer.voucher.enums.VoucherType;
import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.order.review.dto.ReviewCreateRequest;
import com.example.woowa.order.review.dto.ReviewFindResponse;
import com.example.woowa.order.review.entity.Review;
import com.example.woowa.order.review.enums.ScoreType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", imports = {EnumFindable.class, VoucherType.class, EventType.class,
    ScoreType.class})
public interface CustomerMapper {
  @Mapping(target = "birthdate", expression = "java(LocalDate.parse(customerCreateRequest.getBirthdate(), DateTimeFormatter.ISO_DATE))")
  Customer toCustomer(CustomerCreateRequest customerCreateRequest, CustomerGrade customerGrade);
  CustomerFindResponse toCustomerDto(Customer customer);

  CustomerGradeFindResponse toCustomerGradeDto(CustomerGrade customerGrade);
  CustomerGrade toCustomerGrade(CustomerGradeCreateRequest customerGradeCreateRequest);

  @Mappings({
      @Mapping(target = "voucherType", expression = "java(voucher.getVoucherType().toString())"),
      @Mapping(target = "eventType", expression = "java(voucher.getEventType().toString())")
  })
  VoucherFindResponse toVoucherDto(Voucher voucher);

  @Mappings({
      @Mapping(target = "voucherType", expression = "java(EnumFindable.find(voucherCreateRequest.getVoucherType(), VoucherType.values()))"),
      @Mapping(target = "eventType", expression = "java(EnumFindable.find(voucherCreateRequest.getEventType(), EventType.values()))"),
      @Mapping(target = "expirationDate", expression = "java(LocalDateTime.parse(voucherCreateRequest.getExpirationDate(), DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm\")))")
  })
  Voucher toVoucher(VoucherCreateRequest voucherCreateRequest);

  @Mapping(target = "scoreType", expression = "java(review.getScoreType().getValue())")
  ReviewFindResponse toReviewDto(Review review);
  @Mapping(target = "scoreType", expression = "java(ScoreType.find(reviewCreateRequest.getScoreType()))")
  Review toReview(ReviewCreateRequest reviewCreateRequest, Customer customer);

  @Mapping(target = "address", expression = "java(customerAddress.getAddress())")
  CustomerAddressFindResponse toCustomerAddressDto(CustomerAddress customerAddress);
  CustomerAddress toCustomerAddress(
      AreaCode areaCode, CustomerAddressCreateRequest customerAddressCreateRequest, Customer customer);
}