package com.example.woowa.customer.customer.converter;

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
import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.order.review.dto.ReviewCreateRequest;
import com.example.woowa.order.review.dto.ReviewFindResponse;
import com.example.woowa.order.review.entity.Review;

//@Mapper
//public interface CustomerMapper {
//  @Mapping(target = "birthdate", source = "customerCreateRequest.birthdate", dateFormat = "yyyy-mm-dd")
//  Customer toCustomer(CustomerCreateRequest customerCreateRequest, CustomerGrade customerGrade);
//  CustomerFindResponse toCustomerDto(Customer customer);
//
//  CustomerGradeFindResponse toCustomerGradeDto(CustomerGrade customerGrade);
//  CustomerGrade toCustomerGrade(CustomerGradeCreateRequest customerGradeCreateRequest);
//
//  VoucherFindResponse toVoucherDto(Voucher voucher);
//  Voucher toVoucher(VoucherCreateRequest voucherCreateRequest);
//
//  ReviewFindResponse toReviewDto(Review review);
//  Review toReview(ReviewCreateRequest reviewCreateRequest);
//
//  CustomerAddressFindResponse toCustomerAddressDto(CustomerAddress customerAddress);
//  CustomerAddress toCustomerAddress(
//      AreaCode areaCode, CustomerAddressCreateRequest customerAddressCreateRequest, Customer customer);
//}