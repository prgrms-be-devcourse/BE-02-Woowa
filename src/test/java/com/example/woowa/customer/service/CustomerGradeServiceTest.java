package com.example.woowa.customer.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.example.woowa.customer.dto.CreateCustomerGradeDto;
import com.example.woowa.customer.dto.CustomerGradeDto;
import com.example.woowa.customer.dto.UpdateCustomerGradeDto;
import com.example.woowa.customer.entity.CustomerGrade;
import com.example.woowa.customer.repository.CustomerGradeRepository;
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

    CustomerGradeDto customerGradeDto1 = customerGradeService.readCustomerGrade(customerGradeDto.getId());

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

  @Test
  void 고객_등급_기본_등급으로_초기화() {
    CreateCustomerGradeDto createCustomerGradeDto = new CreateCustomerGradeDto(5, "일반", 3000, 2);
    customerGradeService.createCustomerGrade(createCustomerGradeDto);
    CreateCustomerGradeDto createCustomerGradeDto1 = new CreateCustomerGradeDto(10, "실버", 3000, 2);
    customerGradeService.createCustomerGrade(createCustomerGradeDto1);

    CustomerGrade customerGrade = customerGradeService.findDefaultCustomerGrade();

    assertThat(customerGrade.getOrderCount(), is(5));
    assertThat(customerGrade.getGrade(), is("일반"));
    assertThat(customerGrade.getDiscountPrice(), is(3000));
    assertThat(customerGrade.getVoucherCount(), is(2));
  }

  @Test
  void 주문_횟수를_반영한_고객_등급_갱신() {
    CreateCustomerGradeDto createCustomerGradeDto = new CreateCustomerGradeDto(5, "일반", 3000, 2);
    customerGradeService.createCustomerGrade(createCustomerGradeDto);
    CreateCustomerGradeDto createCustomerGradeDto1 = new CreateCustomerGradeDto(10, "실버", 3000, 2);
    customerGradeService.createCustomerGrade(createCustomerGradeDto1);
    CreateCustomerGradeDto createCustomerGradeDto2 = new CreateCustomerGradeDto(15, "골드", 3000, 2);
    customerGradeService.createCustomerGrade(createCustomerGradeDto2);

    CustomerGrade customerGrade = customerGradeService.findCustomerGrade(4);

    assertThat(customerGrade.getOrderCount(), is(5));
    assertThat(customerGrade.getGrade(), is("일반"));
    assertThat(customerGrade.getDiscountPrice(), is(3000));
    assertThat(customerGrade.getVoucherCount(), is(2));
  }
}