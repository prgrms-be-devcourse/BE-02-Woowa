package com.example.woowa.customer.customer.converter;

import static org.junit.jupiter.api.Assertions.*;

import com.example.woowa.customer.customer.dto.CustomerGradeCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerGradeFindResponse;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerMapperTest {
  @Autowired
  private CustomerMapper customerMapper;

  @Test
  void toCustomer() {
  }

  @Test
  void toCustomerDto() {
  }

  @Test
  void toCustomerGradeDto() {
    CustomerGrade customerGrade = new CustomerGrade(5, "일반", 3000, 2);

    CustomerGradeFindResponse customerGradeFindResponse = customerMapper.toCustomerGradeDto(customerGrade);

    Assertions.assertThat(customerGradeFindResponse.getOrderCount()).isEqualTo(5);
    Assertions.assertThat(customerGradeFindResponse.getTitle()).isEqualTo("일반");
    Assertions.assertThat(customerGradeFindResponse.getDiscountPrice()).isEqualTo(3000);
    Assertions.assertThat(customerGradeFindResponse.getVoucherCount()).isEqualTo(2);
  }

  @Test
  void toCustomerGrade() {
    CustomerGradeCreateRequest customerGradeCreateRequest = new CustomerGradeCreateRequest(5, "일반", 3000, 2);

    CustomerGrade customerGrade = customerMapper.toCustomerGrade(customerGradeCreateRequest);

    Assertions.assertThat(customerGrade.getOrderCount()).isEqualTo(5);
    Assertions.assertThat(customerGrade.getTitle()).isEqualTo("일반");
    Assertions.assertThat(customerGrade.getDiscountPrice()).isEqualTo(3000);
    Assertions.assertThat(customerGrade.getVoucherCount()).isEqualTo(2);
  }

  @Test
  void toCustomerAddressDto() {
  }

  @Test
  void toCustomerAddress() {
  }
}