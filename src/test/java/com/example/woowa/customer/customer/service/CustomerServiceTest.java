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
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울시","동작구","집");
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
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울시","동작구","집");
    CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("dev12","Programmers123!", "2000-01-01", customerAddressCreateRequest);
    CustomerFindResponse customerFindResponse = customerService.createCustomer(
        customerCreateRequest);

    assertThat(customerFindResponse.getLoginId(), is("dev12"));
    assertThat(customerFindResponse.getPoint(), is(0));
    assertThat(customerFindResponse.getBirthdate(), is(LocalDate.of(2000,1,1).toString()));
    assertThat(customerFindResponse.getCustomerGrade().getOrderCount(), is(1));
    assertThat(customerFindResponse.getCustomerGrade().getGrade(), is("일반"));
    assertThat(customerFindResponse.getCustomerGrade().getDiscountPrice(), is(3000));
    assertThat(customerFindResponse.getCustomerGrade().getVoucherCount(), is(2));
  }

//  @Test
//  @DisplayName("아이디 입력 오류")
//  void createCustomerFail1() {
//    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울시","동작구","집");
//    CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("devcourse","Programmers123!", "2000-01-01", customerAddressCreateRequest);
//
//    assertThrows(AssertionError.class, ()-> {
//      customerService.createCustomer(customerCreateRequest);
//    });
//  }
//
//  @Test
//  @DisplayName("비밀번호 입력 오류")
//  void createCustomerFail2() {
//    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울시","동작구","집");
//    CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("dev12","Programmers123", "2000-01-01", customerAddressCreateRequest);
//
//    assertThrows(AssertionError.class, ()-> {
//      customerService.createCustomer(customerCreateRequest);
//    });
//  }
//
//  @Test
//  @DisplayName("생년월일 입력 오류")
//  void createCustomerFail3() {
//    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울시","동작구","집");
//    CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("dev12","Programmers123!", "hello", customerAddressCreateRequest);
//
//    assertThrows(AssertionError.class, ()-> {
//      customerService.createCustomer(customerCreateRequest);
//    });
//  }

  @Test
  @DisplayName("유저 정보 조회")
  void findCustomer() {
    String id = getCustomerLoginId();

    CustomerFindResponse customerFindResponse = customerService.findCustomer(id);

    assertThat(customerFindResponse.getLoginId(), is("dev12"));
    assertThat(customerFindResponse.getPoint(), is(0));
    assertThat(customerFindResponse.getBirthdate(), is(LocalDate.of(2000,1,1).toString()));
    assertThat(customerFindResponse.getCustomerGrade().getOrderCount(), is(1));
    assertThat(customerFindResponse.getCustomerGrade().getGrade(), is("일반"));
    assertThat(customerFindResponse.getCustomerGrade().getDiscountPrice(), is(3000));
    assertThat(customerFindResponse.getCustomerGrade().getVoucherCount(), is(2));
  }

  @Test
  @DisplayName("유저 정보 업데이트")
  void updateCustomer() {
    String id = getCustomerLoginId();
    CustomerFindResponse customerFindResponse = customerService.findCustomer(id);

    CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest("Programmers234!");
    customerService.updateCustomer(customerFindResponse.getLoginId(), customerUpdateRequest);
    CustomerFindResponse customerFindResponse1 = customerService.findCustomer(customerFindResponse.getLoginId());

    assertThat(customerFindResponse1.getLoginId(), is("dev12"));
    assertThat(customerFindResponse.getPoint(), is(0));
    assertThat(customerFindResponse.getBirthdate(), is(LocalDate.of(2000,1,1).toString()));
    assertThat(customerFindResponse.getCustomerGrade().getOrderCount(), is(1));
    assertThat(customerFindResponse.getCustomerGrade().getGrade(), is("일반"));
    assertThat(customerFindResponse.getCustomerGrade().getDiscountPrice(), is(3000));
    assertThat(customerFindResponse.getCustomerGrade().getVoucherCount(), is(2));
  }

  @Test
  @DisplayName("잘못된 유저 정보 업데이트")
  void updateCustomerFail() {
    String id = getCustomerLoginId();
    CustomerFindResponse customerFindResponse = customerService.findCustomer(id);

    assertThrows(AssertionError.class, ()-> {
      CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest("programmers");
      customerService.updateCustomer(customerFindResponse.getLoginId(), customerUpdateRequest);
    });
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

    assertThat(customerGrade.getOrderCount(), is(1));
    assertThat(customerGrade.getGrade(), is("일반"));
    assertThat(customerGrade.getDiscountPrice(), is(3000));
    assertThat(customerGrade.getVoucherCount(), is(2));
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

    assertThat(customerGrade.getOrderCount(), is(2));
    assertThat(customerGrade.getGrade(), is("실버"));
    assertThat(customerGrade.getDiscountPrice(), is(3000));
    assertThat(customerGrade.getVoucherCount(), is(2));
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

    assertThat(customerFindResponse.getOrderPerMonth(), is(0));
    assertThat(customerFindResponse.getIsIssued(), is(false));
    assertThat(customerFindResponse.getPoint(), is(5000));
  }
}