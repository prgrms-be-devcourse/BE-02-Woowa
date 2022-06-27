package com.example.woowa.customer.customer.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.example.woowa.customer.customer.dto.CustomerGradeCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerGradeFindResponse;
import com.example.woowa.customer.customer.dto.CustomerGradeUpdateRequest;
import com.example.woowa.customer.customer.repository.CustomerGradeRepository;
import com.example.woowa.customer.customer.service.CustomerGradeService;
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

    assertThat(customerGradeFindResponse.getOrderCount(), is(5));
    assertThat(customerGradeFindResponse.getGrade(), is("일반"));
    assertThat(customerGradeFindResponse.getDiscountPrice(), is(3000));
    assertThat(customerGradeFindResponse.getVoucherCount(), is(2));
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

    assertThat(customerGradeFindResponse1.getOrderCount(), is(5));
    assertThat(customerGradeFindResponse1.getGrade(), is("일반"));
    assertThat(customerGradeFindResponse1.getDiscountPrice(), is(3000));
    assertThat(customerGradeFindResponse1.getVoucherCount(), is(2));
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

    assertThat(customerGradeFindResponse1.getOrderCount(), is(10));
    assertThat(customerGradeFindResponse1.getGrade(), is("실버"));
    assertThat(customerGradeFindResponse1.getDiscountPrice(), is(2000));
    assertThat(customerGradeFindResponse1.getVoucherCount(), is(2));
  }
}