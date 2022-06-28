package com.example.woowa.customer.customer.service;

import com.example.woowa.customer.customer.dto.CustomerGradeCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerGradeFindResponse;
import com.example.woowa.customer.customer.dto.CustomerGradeUpdateRequest;
import com.example.woowa.customer.customer.repository.CustomerGradeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerGradeServiceTest {
  @Autowired
  private CustomerGradeService customerGradeService;

  @Autowired
  private CustomerGradeRepository customerGradeRepository;

  @Test
  @DisplayName("고객 등급 생성")
  void createCustomerGrade() {
    CustomerGradeCreateRequest customerGradeCreateRequest = new CustomerGradeCreateRequest(5, "일반", 3000, 2);

    CustomerGradeFindResponse customerGradeFindResponse = customerGradeService.createCustomerGrade(
        customerGradeCreateRequest);

    Assertions.assertThat(customerGradeFindResponse.getOrderCount()).isEqualTo(5);
    Assertions.assertThat(customerGradeFindResponse.getGrade()).isEqualTo("일반");
    Assertions.assertThat(customerGradeFindResponse.getDiscountPrice()).isEqualTo(3000);
    Assertions.assertThat(customerGradeFindResponse.getVoucherCount()).isEqualTo(2);
  }

  @AfterEach
  void settingAfterTest() {
    customerGradeRepository.deleteAll();
  }

  @Test
  @DisplayName("고객 등급 조회")
  void findCustomerGrade() {
    CustomerGradeCreateRequest customerGradeCreateRequest = new CustomerGradeCreateRequest(5, "일반", 3000, 2);
    CustomerGradeFindResponse customerGradeFindResponse = customerGradeService.createCustomerGrade(
        customerGradeCreateRequest);

    CustomerGradeFindResponse customerGradeFindResponse1 = customerGradeService.findCustomerGrade(
        customerGradeFindResponse.getId());

    Assertions.assertThat(customerGradeFindResponse1.getOrderCount()).isEqualTo(5);
    Assertions.assertThat(customerGradeFindResponse1.getGrade()).isEqualTo("일반");
    Assertions.assertThat(customerGradeFindResponse1.getDiscountPrice()).isEqualTo(3000);
    Assertions.assertThat(customerGradeFindResponse1.getVoucherCount()).isEqualTo(2);
  }

  @Test
  @DisplayName("고객 등급 수정")
  void updateCustomerGrade() {
    CustomerGradeCreateRequest customerGradeCreateRequest = new CustomerGradeCreateRequest(5, "일반", 3000, 2);
    CustomerGradeFindResponse customerGradeFindResponse = customerGradeService.createCustomerGrade(
        customerGradeCreateRequest);

    CustomerGradeUpdateRequest updateCustomerGradeDto = new CustomerGradeUpdateRequest(10, "실버", 2000, 2);
    CustomerGradeFindResponse customerGradeFindResponse1 = customerGradeService.updateCustomerGrade(
        customerGradeFindResponse.getId(), updateCustomerGradeDto);

    Assertions.assertThat(customerGradeFindResponse1.getOrderCount()).isEqualTo(10);
    Assertions.assertThat(customerGradeFindResponse1.getGrade()).isEqualTo("실버");
    Assertions.assertThat(customerGradeFindResponse1.getDiscountPrice()).isEqualTo(2000);
    Assertions.assertThat(customerGradeFindResponse1.getVoucherCount()).isEqualTo(2);
  }
}