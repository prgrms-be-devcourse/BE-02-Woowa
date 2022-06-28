package com.example.woowa.customer.customer.converter;

import com.example.woowa.customer.customer.dto.CustomerGradeCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerGradeFindResponse;
import com.example.woowa.customer.customer.entity.CustomerGrade;

public class CustomerGradeConverter {

    public static CustomerGradeFindResponse toCustomerGradeDto(CustomerGrade customerGrade) {
        return new CustomerGradeFindResponse(customerGrade.getId(), customerGrade.getOrderCount(), customerGrade.getGrade(),
                customerGrade.getDiscountPrice(), customerGrade.getVoucherCount());
    }

    public static CustomerGrade toCustomerGrade(
        CustomerGradeCreateRequest customerGradeCreateRequest) {
        return new CustomerGrade(customerGradeCreateRequest.getOrderCount(),
                customerGradeCreateRequest.getGrade(), customerGradeCreateRequest.getDiscountPrice(),
                customerGradeCreateRequest.getVoucherCount());
    }
}
