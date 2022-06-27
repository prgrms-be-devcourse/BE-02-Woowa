package com.example.woowa.customer.customer.dto;

import com.example.woowa.order.review.dto.ReviewFindResponse;
import com.example.woowa.customer.voucher.dto.VoucherFindResponse;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerFindResponse {
    private String loginId;
    private String birthdate;
    private Integer orderPerMonth;
    private Integer point;
    private Boolean isIssued;
    private CustomerGradeFindResponse customerGrade;
    private List<ReviewFindResponse> reviews;
    private List<CustomerAddressFindResponse> customerAddresses;
    private List<VoucherFindResponse> vouchers;
}
