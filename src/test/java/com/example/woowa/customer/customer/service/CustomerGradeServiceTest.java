package com.example.woowa.customer.customer.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.example.woowa.customer.customer.dto.CustomerGradeCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerGradeFindResponse;
import com.example.woowa.customer.customer.dto.CustomerGradeUpdateRequest;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.customer.repository.CustomerGradeRepository;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class CustomerGradeServiceTest {
  @Autowired
  private CustomerGradeService customerGradeService;

  @MockBean
  private CustomerGradeRepository customerGradeRepository;

  @Test
  @DisplayName("고객 등급 생성")
  void createCustomerGrade() {
    CustomerGradeCreateRequest customerGradeCreateRequest = new CustomerGradeCreateRequest(5, "일반", 3000, 2);
    given(customerGradeRepository.save(any())).willReturn(new CustomerGrade(5, "일반", 3000, 2));

    CustomerGradeFindResponse customerGradeFindResponse = customerGradeService.createCustomerGrade(
        customerGradeCreateRequest);

    Assertions.assertThat(customerGradeFindResponse.getOrderCount()).isEqualTo(5);
    Assertions.assertThat(customerGradeFindResponse.getTitle()).isEqualTo("일반");
    Assertions.assertThat(customerGradeFindResponse.getDiscountPrice()).isEqualTo(3000);
    Assertions.assertThat(customerGradeFindResponse.getVoucherCount()).isEqualTo(2);
  }

  @Test
  @DisplayName("고객 등급 조회")
  void findCustomerGrade() {
    given(customerGradeRepository.findById(anyLong())).willReturn(Optional.of(new CustomerGrade(5, "일반", 3000, 2)));

    CustomerGradeFindResponse customerGradeFindResponse1 = customerGradeService.findCustomerGrade(1l);

    Assertions.assertThat(customerGradeFindResponse1.getOrderCount()).isEqualTo(5);
    Assertions.assertThat(customerGradeFindResponse1.getTitle()).isEqualTo("일반");
    Assertions.assertThat(customerGradeFindResponse1.getDiscountPrice()).isEqualTo(3000);
    Assertions.assertThat(customerGradeFindResponse1.getVoucherCount()).isEqualTo(2);
  }

  @Test
  @DisplayName("고객 등급 수정")
  void updateCustomerGrade() {
    given(customerGradeRepository.findById(anyLong())).willReturn(Optional.of(new CustomerGrade(5, "일반", 3000, 2)));

    CustomerGradeUpdateRequest updateCustomerGradeDto = new CustomerGradeUpdateRequest(10, "실버", 2000, 2);
    CustomerGradeFindResponse customerGradeFindResponse1 = customerGradeService.updateCustomerGrade(
        1l, updateCustomerGradeDto);

    Assertions.assertThat(customerGradeFindResponse1.getOrderCount()).isEqualTo(10);
    Assertions.assertThat(customerGradeFindResponse1.getTitle()).isEqualTo("실버");
    Assertions.assertThat(customerGradeFindResponse1.getDiscountPrice()).isEqualTo(2000);
    Assertions.assertThat(customerGradeFindResponse1.getVoucherCount()).isEqualTo(2);
  }
}