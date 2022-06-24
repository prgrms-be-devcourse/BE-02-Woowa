package com.example.woowa.customer.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.example.woowa.customer.customer.dto.CreateCustomerGradeDto;
import com.example.woowa.customer.customer.dto.CustomerGradeDto;
import com.example.woowa.customer.customer.dto.UpdateCustomerGradeDto;
import com.example.woowa.customer.customer.repository.CustomerGradeRepository;
import com.example.woowa.customer.customer.service.CustomerGradeService;
import org.junit.jupiter.api.AfterEach;
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
  void 고객_등급_생성() {
    CreateCustomerGradeDto createCustomerGradeDto = new CreateCustomerGradeDto(5, "일반", 3000, 2);

    CustomerGradeDto customerGradeDto = customerGradeService.createCustomerGrade(createCustomerGradeDto);

    assertThat(customerGradeDto.getOrderCount(), is(5));
    assertThat(customerGradeDto.getGrade(), is("일반"));
    assertThat(customerGradeDto.getDiscountPrice(), is(3000));
    assertThat(customerGradeDto.getVoucherCount(), is(2));
  }

  @AfterEach
  void 고객_등급_삭제() {
    customerGradeRepository.deleteAll();
  }

  @Test
  void 고객_등급_조회() {
    CreateCustomerGradeDto createCustomerGradeDto = new CreateCustomerGradeDto(5, "일반", 3000, 2);
    CustomerGradeDto customerGradeDto = customerGradeService.createCustomerGrade(createCustomerGradeDto);

    CustomerGradeDto customerGradeDto1 = customerGradeService.findCustomerGrade(customerGradeDto.getId());

    assertThat(customerGradeDto1.getOrderCount(), is(5));
    assertThat(customerGradeDto1.getGrade(), is("일반"));
    assertThat(customerGradeDto1.getDiscountPrice(), is(3000));
    assertThat(customerGradeDto1.getVoucherCount(), is(2));
  }

  @Test
  void 고객_등급_수정() {
    CreateCustomerGradeDto createCustomerGradeDto = new CreateCustomerGradeDto(5, "일반", 3000, 2);
    CustomerGradeDto customerGradeDto = customerGradeService.createCustomerGrade(createCustomerGradeDto);

    UpdateCustomerGradeDto updateCustomerGradeDto = new UpdateCustomerGradeDto(10, "실버", 2000, 2);
    CustomerGradeDto customerGradeDto1 = customerGradeService.updateCustomerGrade(customerGradeDto.getId(), updateCustomerGradeDto);

    assertThat(customerGradeDto1.getOrderCount(), is(10));
    assertThat(customerGradeDto1.getGrade(), is("실버"));
    assertThat(customerGradeDto1.getDiscountPrice(), is(2000));
    assertThat(customerGradeDto1.getVoucherCount(), is(2));
  }
}