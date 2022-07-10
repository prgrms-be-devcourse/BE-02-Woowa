package com.example.woowa.customer.customer.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.woowa.customer.customer.dto.CustomerAddressCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerAddressFindResponse;
import com.example.woowa.customer.customer.dto.CustomerAddressUpdateRequest;
import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerAddress;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.customer.repository.CustomerAddressRepository;
import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.delivery.service.AreaCodeService;
import java.time.LocalDate;
import java.util.List;
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
class CustomerAddressServiceTest {
  @Autowired
  private CustomerAddressService customerAddressService;

  @MockBean
  private CustomerService customerService;

  @MockBean
  private AreaCodeService areaCodeService;

  @MockBean
  private CustomerAddressRepository customerAddressRepository;

  @Test
  @DisplayName("고객 주소 추가")
  void createCustomerAddress() {
    CustomerGrade customerGrade = new CustomerGrade(5, "일반", 3000, 2);
    Customer customer = new Customer("dev12","Programmers123!", LocalDate.of(2000,1,1), customerGrade);
    AreaCode areaCode = new AreaCode("1000000000", "서울특별시 동작구 상도동", false);
    CustomerAddress customerAddress = new CustomerAddress(areaCode, "아파트 101호","집", customer);
    given(areaCodeService.findByAddress(anyString())).willReturn(areaCode);
    given(customerService.findCustomerEntity(anyString())).willReturn(customer);
    given(customerAddressRepository.save(any())).willReturn(customerAddress);

    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울특별시 동작구 상도동", "아파트 101호","집");

    CustomerAddressFindResponse customerAddressFindResponse = customerAddressService.createCustomerAddress("dev12",
        customerAddressCreateRequest);

    Assertions.assertThat(customerAddressFindResponse.getAddress()).isEqualTo("서울특별시 동작구 상도동 아파트 101호");
    Assertions.assertThat(customerAddressFindResponse.getNickname()).isEqualTo("집");
  }

  @Test
  @DisplayName("고객 주소 목록 조회")
  void findCustomerAddress() {
    CustomerGrade customerGrade = new CustomerGrade(5, "일반", 3000, 2);
    Customer customer = new Customer("dev12","Programmers123!", LocalDate.of(2000,1,1), customerGrade);
    AreaCode areaCode = new AreaCode("1000000000", "서울특별시 동작구 상도동", false);
    customer.addCustomerAddress(new CustomerAddress(areaCode, "아파트 101호","집", customer));
    customer.addCustomerAddress(new CustomerAddress(areaCode, "삼호 타워 101호","회사", customer));
    given(customerService.findCustomerEntity(anyString())).willReturn(customer);

    List<CustomerAddressFindResponse> customerAddressFindResponseList = customerAddressService.findCustomerAddresses("dev12");

    Assertions.assertThat(customerAddressFindResponseList.size()).isEqualTo(2);
    Assertions.assertThat(customerAddressFindResponseList.get(0).getNickname()).isEqualTo("집");
    Assertions.assertThat(customerAddressFindResponseList.get(1).getNickname()).isEqualTo("회사");
  }

  @Test
  @DisplayName("고객 주소 수정")
  void updateCustomerAddress() {
    CustomerGrade customerGrade = new CustomerGrade(5, "일반", 3000, 2);
    Customer customer = new Customer("dev12","Programmers123!", LocalDate.of(2000,1,1), customerGrade);
    AreaCode areaCode = new AreaCode("1000000000", "서울특별시 동작구 상도동", false);
    CustomerAddress customerAddress = new CustomerAddress(areaCode, "아파트 101호","집", customer);
    customer.addCustomerAddress(customerAddress);
    given(areaCodeService.findByAddress(anyString())).willReturn(areaCode);
    given(customerAddressRepository.findById(anyLong())).willReturn(Optional.of(customerAddress));

    CustomerAddressUpdateRequest customerAddressUpdateRequest = new CustomerAddressUpdateRequest("서울특별시 서초구 방배동", "빌라 201호","회사");
    AreaCode areaCode1 = new AreaCode("1000000001", "서울특별시 서초구 방배동", false);
    given(areaCodeService.findByAddress(anyString())).willReturn(areaCode1);
    CustomerAddressFindResponse customerAddressFindResponse = customerAddressService.updateCustomerAddress(1l, customerAddressUpdateRequest);

    Assertions.assertThat(customerAddressFindResponse.getNickname()).isEqualTo("회사");
    Assertions.assertThat(customerAddressFindResponse.getAddress()).isEqualTo("서울특별시 서초구 방배동 빌라 201호");
  }

  @Test
  @DisplayName("고객 주소 삭제")
  void deleteCustomerAddress() {
    CustomerGrade customerGrade = new CustomerGrade(5, "일반", 3000, 2);
    Customer customer = new Customer("dev12","Programmers123!", LocalDate.of(2000,1,1), customerGrade);
    AreaCode areaCode = new AreaCode("1000000000", "서울특별시 동작구 상도동", false);
    CustomerAddress customerAddress = new CustomerAddress(areaCode, "아파트 101호","집", customer);
    customer.addCustomerAddress(customerAddress);
    given(customerService.findCustomerEntity(anyString())).willReturn(customer);
    given(customerAddressRepository.findById(anyLong())).willReturn(Optional.of(customerAddress));

    customerAddressService.deleteCustomerAddress("dev12", 1l);

    verify(customerAddressRepository).delete(customerAddress);
  }
}