package com.example.woowa.customer.customer.dto;

import com.example.woowa.order.review.dto.ReviewDto;
import com.example.woowa.customer.voucher.dto.VoucherDto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerDto {
    private String loginId;
    private String birthdate;
    private Integer orderPerMonth;
    private Integer point;
    private CustomerGradeDto customerGrade;
    private List<ReviewDto> reviews;
    private List<CustomerAddressDto> customerAddresses;
    private List<VoucherDto> vouchers;
}
