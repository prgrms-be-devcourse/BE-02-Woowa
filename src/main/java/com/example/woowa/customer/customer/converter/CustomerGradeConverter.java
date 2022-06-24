package com.example.woowa.customer.customer.converter;

import com.example.woowa.customer.customer.dto.CreateCustomerGradeDto;
import com.example.woowa.customer.customer.dto.CustomerGradeDto;
import com.example.woowa.customer.customer.entity.CustomerGrade;

public class CustomerGradeConverter {

    public static CustomerGradeDto toCustomerGradeDto(CustomerGrade customerGrade) {
        return new CustomerGradeDto(customerGrade.getId(), customerGrade.getOrderCount(), customerGrade.getGrade(),
                customerGrade.getDiscountPrice(), customerGrade.getVoucherCount());
    }

    public static CustomerGrade toCustomerGrade(CreateCustomerGradeDto createCustomerGradeDto) {
        return new CustomerGrade(createCustomerGradeDto.getOrderCount(),
                createCustomerGradeDto.getGrade(), createCustomerGradeDto.getDiscountPrice(),
                createCustomerGradeDto.getVoucherCount());
    }
}
