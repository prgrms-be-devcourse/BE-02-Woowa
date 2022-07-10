package com.example.woowa.customer.customer.repository;

import com.example.woowa.customer.customer.entity.CustomerGrade;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerGradeRepositoryTest {
  @Autowired
  private CustomerGradeRepository customerGradeRepository;

  @BeforeEach
  void settingBeforeTest() {
    customerGradeRepository.deleteAll();
  }

  @AfterEach
  void settingAfterTest() {
    customerGradeRepository.deleteAll();
  }

  @Test
  void findFirstByOrderByOrderCount() {
    customerGradeRepository.save(new CustomerGrade(1, "실버", 4000, 2));
    customerGradeRepository.save(new CustomerGrade(2, "골드", 5000, 2));

    CustomerGrade customerGrade = customerGradeRepository.findFirstByOrderByOrderCount().get();

    Assertions.assertThat(customerGrade.getOrderCount()).isEqualTo(1);
    Assertions.assertThat(customerGrade.getTitle()).isEqualTo("실버");
    Assertions.assertThat(customerGrade.getDiscountPrice()).isEqualTo(4000);
    Assertions.assertThat(customerGrade.getVoucherCount()).isEqualTo(2);
  }

  @Test
  void findFirstByOrderCountLessThanEqualOrderByOrderCountDesc() {
    customerGradeRepository.save(new CustomerGrade(1, "실버", 4000, 2));
    customerGradeRepository.save(new CustomerGrade(2, "골드", 5000, 2));

    CustomerGrade customerGrade = customerGradeRepository.findFirstByOrderCountLessThanEqualOrderByOrderCountDesc(3).get();

    Assertions.assertThat(customerGrade.getOrderCount()).isEqualTo(2);
    Assertions.assertThat(customerGrade.getTitle()).isEqualTo("골드");
    Assertions.assertThat(customerGrade.getDiscountPrice()).isEqualTo(5000);
    Assertions.assertThat(customerGrade.getVoucherCount()).isEqualTo(2);
  }
}