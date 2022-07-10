package com.example.woowa.customer.customer.converter;

import com.example.woowa.customer.customer.dto.CustomerAddressCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerAddressFindResponse;
import com.example.woowa.customer.customer.dto.CustomerCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerFindResponse;
import com.example.woowa.customer.customer.dto.CustomerGradeCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerGradeFindResponse;
import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerAddress;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.delivery.entity.AreaCode;
import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerMapperTest {
  @Autowired
  private CustomerMapper customerMapper;

  @Test
  @DisplayName("고객 엔티티 변환")
  void toCustomer() {
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest(
        "서울특별시 동작구 상도동", "빌라 101호", "집");
    CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest("dev12",
        "Programmers123!", "2000-01-01", customerAddressCreateRequest);
    CustomerGrade customerGrade = new CustomerGrade(5, "일반", 3000, 2);

    Customer customer = customerMapper.toCustomer(customerCreateRequest, customerGrade);

    Assertions.assertThat(customer.getLoginId()).isEqualTo(customerCreateRequest.getLoginId());
    Assertions.assertThat(customer.getOrderPerMonth()).isEqualTo(0);
    Assertions.assertThat(customer.getBirthdate()).isEqualTo(customerCreateRequest.getBirthdate());
    Assertions.assertThat(customer.getIsIssued()).isEqualTo(false);
    Assertions.assertThat(customer.getCustomerGrade().getTitle()).isEqualTo(customerGrade.getTitle());
    Assertions.assertThat(customer.getCustomerGrade().getVoucherCount()).isEqualTo(customerGrade.getVoucherCount());
    Assertions.assertThat(customer.getCustomerGrade().getDiscountPrice()).isEqualTo(customerGrade.getDiscountPrice());
    Assertions.assertThat(customer.getCustomerGrade().getOrderCount()).isEqualTo(customerGrade.getOrderCount());
  }

  @Test
  @DisplayName("고객 dto 변환")
  void toCustomerDto() {
    CustomerGrade customerGrade = new CustomerGrade(5, "일반", 3000, 2);
    Customer customer = new Customer("dev12",
        "Programmers123!", LocalDate.now(), customerGrade);

    CustomerFindResponse customerFindResponse = customerMapper.toCustomerDto(customer);

    Assertions.assertThat(customerFindResponse.getLoginId()).isEqualTo(customer.getLoginId());
    Assertions.assertThat(customerFindResponse.getOrderPerMonth()).isEqualTo(customer.getOrderPerMonth());
    Assertions.assertThat(customerFindResponse.getBirthdate()).isEqualTo(customer.getBirthdate().toString());
    Assertions.assertThat(customerFindResponse.getIsIssued()).isEqualTo(customer.getIsIssued());
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getTitle()).isEqualTo(customerGrade.getTitle());
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getVoucherCount()).isEqualTo(customerGrade.getVoucherCount());
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getDiscountPrice()).isEqualTo(customerGrade.getDiscountPrice());
    Assertions.assertThat(customerFindResponse.getCustomerGrade().getOrderCount()).isEqualTo(customerGrade.getOrderCount());
  }

  @Test
  @DisplayName("고객 등급 엔티티 변환")
  void toCustomerGradeDto() {
    CustomerGrade customerGrade = new CustomerGrade(5, "일반", 3000, 2);

    CustomerGradeFindResponse customerGradeFindResponse = customerMapper.toCustomerGradeDto(customerGrade);

    Assertions.assertThat(customerGradeFindResponse.getOrderCount()).isEqualTo(customerGrade.getOrderCount());
    Assertions.assertThat(customerGradeFindResponse.getTitle()).isEqualTo(customerGrade.getTitle());
    Assertions.assertThat(customerGradeFindResponse.getDiscountPrice()).isEqualTo(customerGrade.getDiscountPrice());
    Assertions.assertThat(customerGradeFindResponse.getVoucherCount()).isEqualTo(customerGrade.getVoucherCount());
  }

  @Test
  @DisplayName("고객 등급 dto 변환")
  void toCustomerGrade() {
    CustomerGradeCreateRequest customerGradeCreateRequest = new CustomerGradeCreateRequest(5, "일반", 3000, 2);

    CustomerGrade customerGrade = customerMapper.toCustomerGrade(customerGradeCreateRequest);

    Assertions.assertThat(customerGrade.getOrderCount()).isEqualTo(customerGradeCreateRequest.getOrderCount());
    Assertions.assertThat(customerGrade.getTitle()).isEqualTo(customerGradeCreateRequest.getTitle());
    Assertions.assertThat(customerGrade.getDiscountPrice()).isEqualTo(customerGradeCreateRequest.getDiscountPrice());
    Assertions.assertThat(customerGrade.getVoucherCount()).isEqualTo(customerGradeCreateRequest.getVoucherCount());
  }

  @Test
  @DisplayName("고객 주소 엔티티 변환")
  void toCustomerAddressDto() {
    CustomerGrade customerGrade = new CustomerGrade(5, "일반", 3000, 2);
    Customer customer = new Customer("dev12",
        "Programmers123!", LocalDate.now(), customerGrade);
    AreaCode areaCode = new AreaCode("1000000000", "서울특별시 동작구 상도동", false);
    CustomerAddress customerAddress = new CustomerAddress(areaCode, "아파트 101호", "집", customer);

    CustomerAddressFindResponse customerAddressFindResponse = customerMapper.toCustomerAddressDto(customerAddress);

    Assertions.assertThat(customerAddressFindResponse.getAddress()).isEqualTo(areaCode.getDefaultAddress() + " " + customerAddress.getDetailAddress());
    Assertions.assertThat(customerAddressFindResponse.getNickname()).isEqualTo(customerAddress.getNickname());
  }

  @Test
  @DisplayName("고객 주소 dto 변환")
  void toCustomerAddress() {
    AreaCode areaCode = new AreaCode("1000000000", "서울특별시 동작구 상도동", false);
    CustomerAddressCreateRequest customerAddressCreateRequest = new CustomerAddressCreateRequest("서울특별시 동작구 상도동", "아파트 101호","집");
    CustomerGrade customerGrade = new CustomerGrade(5, "일반", 3000, 2);
    Customer customer = new Customer("dev12",
        "Programmers123!", LocalDate.now(), customerGrade);

    CustomerAddress customerAddress = customerMapper.toCustomerAddress(areaCode, customerAddressCreateRequest, customer);

    Assertions.assertThat(customerAddress.getDetailAddress()).isEqualTo(customerAddressCreateRequest.getDetailAddress());
    Assertions.assertThat(customerAddress.getNickname()).isEqualTo(customerAddressCreateRequest.getNickname());

    Assertions.assertThat(customerAddress.getCustomer().getLoginId()).isEqualTo(customer.getLoginId());
    Assertions.assertThat(customerAddress.getCustomer().getOrderPerMonth()).isEqualTo(customer.getOrderPerMonth());
    Assertions.assertThat(customerAddress.getCustomer().getBirthdate()).isEqualTo(customer.getBirthdate());
    Assertions.assertThat(customerAddress.getCustomer().getIsIssued()).isEqualTo(customer.getIsIssued());

    Assertions.assertThat(customerAddress.getAreaCode().getCode()).isEqualTo(areaCode.getCode());
    Assertions.assertThat(customerAddress.getAreaCode().getDefaultAddress()).isEqualTo(areaCode.getDefaultAddress());
  }
}