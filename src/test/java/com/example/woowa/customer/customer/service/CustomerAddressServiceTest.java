package com.example.woowa.customer.customer.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.example.woowa.customer.customer.dto.CustomerAddressCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerAddressFindResponse;
import com.example.woowa.customer.customer.dto.CustomerAddressUpdateRequest;
import com.example.woowa.customer.customer.dto.CustomerCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerFindResponse;
import com.example.woowa.customer.customer.dto.CustomerGradeCreateRequest;
import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.repository.CustomerAddressRepository;
import com.example.woowa.customer.customer.repository.CustomerGradeRepository;
import com.example.woowa.customer.customer.repository.CustomerRepository;
import com.example.woowa.delivery.service.AreaCodeService;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerAddressServiceTest {
  @Autowired
  private CustomerService customerService;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private CustomerGradeService customerGradeService;

  @Autowired
  private CustomerGradeRepository customerGradeRepository;

  @Autowired
  private CustomerAddressService customerAddressService;

  @Autowired
  private CustomerAddressRepository customerAddressRepository;

  @Autowired
  private AreaCodeService areaCodeService;

  public void makeDefaultCustomerGrade() {
    CustomerGradeCreateRequest customerGradeCreateRequest = new CustomerGradeCreateRequest(5, "일반", 3000, 2);
    customerGradeService.createCustomerGrade(customerGradeCreateRequest);
  }

  public String getCustomerLoginId() {
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울특별시 서초구 방배동","123-4 빌라 101호","집");
    CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("dev12","Programmers123!", "2000-01-01", customerAddressCreateRequest);
    CustomerFindResponse customerFindResponse = customerService.createCustomer(
        customerCreateRequest);
    return customerFindResponse.getLoginId();
  }

  @BeforeEach
  void settingBeforeTest() {
    makeDefaultCustomerGrade();
  }

  @AfterEach
  void settingAfterTest() {
    customerAddressRepository.deleteAll();
    customerRepository.deleteAll();
    customerGradeRepository.deleteAll();
  }

  @Test
  @DisplayName("고객 주소 추가")
  void createCustomerAddress() {
    Customer customer = customerService.findCustomerEntity(getCustomerLoginId());
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울특별시 동작구 상도동", "아파트 101호","집");

    CustomerAddressFindResponse customerAddressFindResponse = customerAddressService.createCustomerAddress(customer.getLoginId(),
        customerAddressCreateRequest);

    Assertions.assertThat(customerAddressFindResponse.getAddress()).isEqualTo("서울특별시 동작구 상도동 아파트 101호");
    Assertions.assertThat(customerAddressFindResponse.getNickname()).isEqualTo("집");
  }

  @Test
  @DisplayName("고객 주소 목록 조회")
  void findCustomerAddress() {
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울특별시 동작구 상도동", "아파트 101호","회사");
    String loginId = getCustomerLoginId();

    customerAddressService.createCustomerAddress(loginId,
        customerAddressCreateRequest);
    List<CustomerAddressFindResponse> customerAddressFindResponseList = customerAddressService.findCustomerAddresses(loginId);

    Assertions.assertThat(customerAddressFindResponseList.size()).isEqualTo(2);
    Assertions.assertThat(customerAddressFindResponseList.get(0).getNickname()).isEqualTo("집");
    Assertions.assertThat(customerAddressFindResponseList.get(1).getNickname()).isEqualTo("회사");
  }

  @Test
  @DisplayName("고객 주소 수정")
  void updateCustomerAddress() {
    String loginId = getCustomerLoginId();
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울특별시 동작구 상도동", "아파트 101호","집");
    CustomerAddressFindResponse customerAddressFindResponse = customerAddressService.createCustomerAddress(loginId,
        customerAddressCreateRequest);

    CustomerAddressUpdateRequest customerAddressUpdateRequest = new CustomerAddressUpdateRequest("서울특별시 서초구 방배동", "빌라 201호","회사");
    customerAddressService.updateCustomerAddress(customerAddressFindResponse.getId(),
        customerAddressUpdateRequest);
    List<CustomerAddressFindResponse> customerAddressFindResponseList = customerAddressService.findCustomerAddresses(loginId);

    Assertions.assertThat(customerAddressFindResponseList.get(1).getNickname()).isEqualTo("회사");
    Assertions.assertThat(customerAddressFindResponseList.get(1).getAddress()).isEqualTo("서울특별시 서초구 방배동 빌라 201호");
  }

  @Test
  @DisplayName("고객 주소 삭제")
  void deleteCustomerAddress() {
    String loginId = getCustomerLoginId();
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울특별시 동작구 상도동", "아파트 101호","집");
    CustomerAddressFindResponse customerAddressFindResponse = customerAddressService.createCustomerAddress(loginId,
        customerAddressCreateRequest);

    customerAddressService.deleteCustomerAddress(customerAddressFindResponse.getId());
    List<CustomerAddressFindResponse> customerAddressFindResponseList = customerAddressService.findCustomerAddresses(loginId);

    Assertions.assertThat(customerAddressFindResponseList.size()).isEqualTo(1);
  }
}