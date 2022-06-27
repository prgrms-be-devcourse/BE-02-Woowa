package com.example.woowa.customer.customer.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.woowa.customer.customer.dto.CustomerAddressCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerFindResponse;
import com.example.woowa.customer.customer.dto.CustomerGradeCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerUpdateRequest;
import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.customer.repository.CustomerGradeRepository;
import com.example.woowa.customer.customer.repository.CustomerRepository;
import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class CustomerServiceTest {
  @Autowired
  private CustomerService customerService;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private CustomerGradeService customerGradeService;

  @Autowired
  private CustomerGradeRepository customerGradeRepository;

  public void makeDefaultCustomerGrade() {
    CustomerGradeCreateRequest customerGradeCreateRequest = new CustomerGradeCreateRequest(1, "일반", 3000, 2);
    customerGradeService.createCustomerGrade(customerGradeCreateRequest);
  }

  public String getCustomerLoginId() {
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울특별시 동작구 상도동","빌라 101호","집");
    CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("dev12","Programmers123!", "2000-01-01", customerAddressCreateRequest);
    CustomerFindResponse customerFindResponse = customerService.createCustomer(
        customerCreateRequest);
    return customerFindResponse.getLoginId();
  }

  @BeforeEach
  void settingBeforTest() {
    makeDefaultCustomerGrade();
  }

  @AfterEach
  void settingAfterTest() {
    customerRepository.deleteAll();
    customerGradeRepository.deleteAll();
  }

  @Test
  @DisplayName("유저 생성")
  void createUser() {
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울특별시 동작구 상도동","빌라 101호","집");
    CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("dev12","Programmers123!", "2000-01-01", customerAddressCreateRequest);
    CustomerFindResponse customerFindResponse = customerService.createCustomer(
        customerCreateRequest);

    Assertions.assertThat(customerFindResponse.getLoginId()).isEqualTo("dev12");
    Assertions.assertThat(customerFindResponse.getPoint()).isEqualTo(0);
    Assertions.assertThat(customerFindResponse.getBirthdate()).isEqualTo(LocalDate.of(2000,1,1).toString());
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getOrderCount()).isEqualTo(1);
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getGrade()).isEqualTo("일반");
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getDiscountPrice()).isEqualTo(3000);
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getVoucherCount()).isEqualTo(2);
  }

  @Test
  @DisplayName("유저 정보 조회")
  void findCustomer() {
    String id = getCustomerLoginId();

    CustomerFindResponse customerFindResponse = customerService.findCustomer(id);

    Assertions.assertThat(customerFindResponse.getLoginId()).isEqualTo("dev12");
    Assertions.assertThat(customerFindResponse.getPoint()).isEqualTo(0);
    Assertions.assertThat(customerFindResponse.getBirthdate()).isEqualTo(LocalDate.of(2000,1,1).toString());
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getOrderCount()).isEqualTo(1);
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getGrade()).isEqualTo("일반");
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getDiscountPrice()).isEqualTo(3000);
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getVoucherCount()).isEqualTo(2);
  }

  @Test
  @DisplayName("유저 정보 업데이트")
  void updateCustomer() {
    String id = getCustomerLoginId();
    CustomerFindResponse customerFindResponse = customerService.findCustomer(id);

    CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest("Programmers234!");
    customerService.updateCustomer(customerFindResponse.getLoginId(), customerUpdateRequest);
    CustomerFindResponse customerFindResponse1 = customerService.findCustomer(customerFindResponse.getLoginId());

    Assertions.assertThat(customerFindResponse1.getLoginId()).isEqualTo("dev12");
    Assertions.assertThat(customerFindResponse.getPoint()).isEqualTo(0);
    Assertions.assertThat(customerFindResponse.getBirthdate()).isEqualTo(LocalDate.of(2000,1,1).toString());
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getOrderCount()).isEqualTo(1);
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getGrade()).isEqualTo("일반");
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getDiscountPrice()).isEqualTo(3000);
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getVoucherCount()).isEqualTo(2);
  }

  @Test
  @DisplayName("유저 삭제")
  void deleteCustomer() {
    String id = getCustomerLoginId();
    CustomerFindResponse customerFindResponse = customerService.findCustomer(id);

    customerService.deleteCustomer(customerFindResponse.getLoginId());

    assertThrows(Exception.class, ()-> {
      customerService.findCustomer(customerFindResponse.getLoginId());
    });
  }

  @Test
  @DisplayName("고객 등급 기본 등급으로 초기화")
  void initCustomerGrade() {
    CustomerGradeCreateRequest customerGradeCreateRequest1 = new CustomerGradeCreateRequest(10, "실버", 3000, 2);
    customerGradeService.createCustomerGrade(customerGradeCreateRequest1);

    CustomerGrade customerGrade = customerGradeService.findDefaultCustomerGrade();

    Assertions.assertThat(customerGrade.getOrderCount()).isEqualTo(1);
    Assertions.assertThat(customerGrade.getGrade()).isEqualTo("일반");
    Assertions.assertThat(customerGrade.getDiscountPrice()).isEqualTo(3000);
    Assertions.assertThat(customerGrade.getVoucherCount()).isEqualTo(2);
  }

  @Test
  @DisplayName("주문 횟수를 반영한 고객 등급 갱신")
  void updateCustomerGrade() {
    CustomerGradeCreateRequest customerGradeCreateRequest1 = new CustomerGradeCreateRequest(2, "실버", 3000, 2);
    customerGradeService.createCustomerGrade(customerGradeCreateRequest1);
    CustomerGradeCreateRequest customerGradeCreateRequest2 = new CustomerGradeCreateRequest(3, "골드", 3000, 2);
    customerGradeService.createCustomerGrade(customerGradeCreateRequest2);
    String customerId = getCustomerLoginId();
    Customer customer = customerService.findCustomerEntity(customerId);
    customer.updateCustomerStatusWhenOrder(3000);
    customer.updateCustomerStatusWhenOrder(2000);

    CustomerGrade customerGrade = customerGradeService.findCustomerGradeByOrderPerMonthCount(customer.getOrderPerMonth());

    Assertions.assertThat(customerGrade.getOrderCount()).isEqualTo(2);
    Assertions.assertThat(customerGrade.getGrade()).isEqualTo("실버");
    Assertions.assertThat(customerGrade.getDiscountPrice()).isEqualTo(3000);
    Assertions.assertThat(customerGrade.getVoucherCount()).isEqualTo(2);
  }

  @Test
  @Transactional
  @DisplayName("매월 고객의 주문 횟수 정보와 정기 쿠폰 발행 여부를 갱신해야 한다.")
  void updateCustomerStatusOn() {
    String customerId = getCustomerLoginId();
    Customer customer = customerService.findCustomerEntity(customerId);
    customer.updateCustomerStatusWhenOrder(3000);
    customer.updateCustomerStatusWhenOrder(2000);

    CustomerFindResponse customerFindResponse = customerService.updateCustomerStatusOnFirstDay(customerId);

    Assertions.assertThat(customerFindResponse.getOrderPerMonth()).isEqualTo(0);
    Assertions.assertThat(customerFindResponse.getIsIssued()).isEqualTo(false);
    Assertions.assertThat(customerFindResponse.getPoint()).isEqualTo(5000);
  }
}
