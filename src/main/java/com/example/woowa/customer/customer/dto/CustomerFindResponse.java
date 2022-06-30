package com.example.woowa.customer.customer.dto;

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
}
