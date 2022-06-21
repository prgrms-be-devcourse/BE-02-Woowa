package com.example.woowa.customer.converter;

import com.example.woowa.customer.dto.CreateCustomerGradeDto;
import com.example.woowa.customer.dto.CustomerGradeDto;
import com.example.woowa.customer.entity.CustomerGrade;

public class CustomerGradeConverter {

  public static CustomerGradeDto toCustomerGradeDto(CustomerGrade customerGrade) {
    return new CustomerGradeDto(customerGrade.getOrderCount(), customerGrade.getGrade(),
        customerGrade.getDiscountPrice(), customerGrade.getVoucherCount());
  }

  public static CustomerGrade toCustomerGrade(CreateCustomerGradeDto createCustomerGradeDto) {
    return new CustomerGrade(createCustomerGradeDto.getOrderCount(),
        createCustomerGradeDto.getGrade(), createCustomerGradeDto.getDiscountPrice(),
        createCustomerGradeDto.getVoucherCount());
  }
}
