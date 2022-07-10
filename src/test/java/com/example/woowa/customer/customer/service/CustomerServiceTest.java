package com.example.woowa.customer.customer.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.woowa.customer.customer.dto.CustomerAddressCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerFindResponse;
import com.example.woowa.customer.customer.dto.CustomerUpdateRequest;
import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.customer.repository.CustomerAddressRepository;
import com.example.woowa.customer.customer.repository.CustomerRepository;
import com.example.woowa.delivery.service.AreaCodeService;
import java.time.LocalDate;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
  @Autowired
  private CustomerService customerService;

  @MockBean
  private CustomerGradeService customerGradeService;

  @MockBean
  private AreaCodeService areaCodeService;

  @MockBean
  private CustomerRepository customerRepository;

  @MockBean
  private CustomerAddressRepository customerAddressRepository;

  @Test
  @DisplayName("유저 생성")
  void createUser() {
    given(customerGradeService.findDefaultCustomerGrade()).willReturn(new CustomerGrade(5, "일반", 3000, 2));

    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울특별시 동작구 상도동","빌라 101호","집");
    CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("dev12","Programmers123!", "2000-01-01", customerAddressCreateRequest);
    CustomerFindResponse customerFindResponse = customerService.createCustomer(
        customerCreateRequest);

    Assertions.assertThat(customerFindResponse.getLoginId()).isEqualTo("dev12");
    Assertions.assertThat(customerFindResponse.getPoint()).isEqualTo(0);
    Assertions.assertThat(customerFindResponse.getBirthdate()).isEqualTo(LocalDate.of(2000,1,1).toString());
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getOrderCount()).isEqualTo(5);
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getTitle()).isEqualTo("일반");
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getDiscountPrice()).isEqualTo(3000);
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getVoucherCount()).isEqualTo(2);
  }

  @Test
  @DisplayName("유저 정보 조회")
  void findCustomer() {
    CustomerGrade customerGrade = new CustomerGrade(5, "일반", 3000, 2);
    Customer customer = new Customer("dev12","Programmers123!", LocalDate.of(2000,1,1), customerGrade);
    given(customerRepository.findByLoginId(anyString())).willReturn(Optional.of(customer));

    CustomerFindResponse customerFindResponse = customerService.findCustomer("dev12");

    Assertions.assertThat(customerFindResponse.getLoginId()).isEqualTo("dev12");
    Assertions.assertThat(customerFindResponse.getPoint()).isEqualTo(0);
    Assertions.assertThat(customerFindResponse.getBirthdate()).isEqualTo(LocalDate.of(2000,1,1).toString());
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getOrderCount()).isEqualTo(5);
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getTitle()).isEqualTo("일반");
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getDiscountPrice()).isEqualTo(3000);
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getVoucherCount()).isEqualTo(2);
  }

  @Test
  @DisplayName("유저 정보 업데이트")
  void updateCustomer() {
    CustomerGrade customerGrade = new CustomerGrade(5, "일반", 3000, 2);
    Customer customer = new Customer("dev12","Programmers123!", LocalDate.of(2000,1,1), customerGrade);
    given(customerRepository.findByLoginId(anyString())).willReturn(Optional.of(customer));

    CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest("Programmers234!");
    CustomerFindResponse customerFindResponse = customerService.updateCustomer("dev12", customerUpdateRequest);

    Assertions.assertThat(customerFindResponse.getLoginId()).isEqualTo("dev12");
    Assertions.assertThat(customerFindResponse.getPoint()).isEqualTo(0);
    Assertions.assertThat(customerFindResponse.getBirthdate()).isEqualTo(LocalDate.of(2000,1,1).toString());
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getOrderCount()).isEqualTo(5);
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getTitle()).isEqualTo("일반");
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getDiscountPrice()).isEqualTo(3000);
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getVoucherCount()).isEqualTo(2);
  }

  @Test
  @DisplayName("유저 삭제")
  void deleteCustomer() {
    CustomerGrade customerGrade = new CustomerGrade(5, "일반", 3000, 2);
    Customer customer = new Customer("dev12","Programmers123!", LocalDate.of(2000,1,1), customerGrade);
    given(customerRepository.findByLoginId(anyString())).willReturn(Optional.of(customer));

    customerService.deleteCustomer("dev12");

    verify(customerRepository).delete(customer);
  }

  @Test
  @DisplayName("매월 고객의 주문 횟수 정보와 정기 쿠폰 발행 여부를 갱신해야 한다.")
  void updateCustomerStatusOn() {
    CustomerGrade customerGrade = new CustomerGrade(5, "일반", 3000, 2);
    Customer customer = new Customer("dev12","Programmers123!", LocalDate.of(2000,1,1), customerGrade);
    orderTwice(customer);
    given(customerRepository.findByLoginId(anyString())).willReturn(Optional.of(customer));

    CustomerFindResponse customerFindResponse = customerService.updateCustomerStatusOnFirstDay("dev12");

    Assertions.assertThat(customerFindResponse.getOrderPerMonth()).isEqualTo(0);
    Assertions.assertThat(customerFindResponse.getIsIssued()).isEqualTo(false);
    Assertions.assertThat(customerFindResponse.getPoint()).isEqualTo(5000);
  }

  void orderTwice(Customer customer) {
    customer.updateCustomerStatusWhenOrder(0, 3000);
    customer.updateCustomerStatusWhenOrder(0,2000);
  }
}
