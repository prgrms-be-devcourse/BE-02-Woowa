package com.example.woowa.customer.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.example.woowa.customer.dto.CreateCustomerAddressDto;
import com.example.woowa.customer.dto.CreateCustomerDto;
import com.example.woowa.customer.dto.CreateCustomerGradeDto;
import com.example.woowa.customer.dto.CustomerAddressDto;
import com.example.woowa.customer.dto.CustomerDto;
import com.example.woowa.customer.dto.UpdateCustomerAddressDto;
import com.example.woowa.customer.entity.Customer;
import com.example.woowa.customer.repository.CustomerAddressRepository;
import com.example.woowa.customer.repository.CustomerGradeRepository;
import com.example.woowa.customer.repository.CustomerRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

  public void makeCustomer() {

  }

  @BeforeEach
  void 기본_고객_등급_설정() {
    CreateCustomerGradeDto createCustomerGradeDto = new CreateCustomerGradeDto(5, "일반", 3000, 2);
    customerGradeService.createCustomerGrade(createCustomerGradeDto);
  }

  @AfterEach
  void 테스트_엔티티_삭제() {
    customerAddressRepository.deleteAll();
    customerRepository.deleteAll();
    customerGradeRepository.deleteAll();
  }

  @Test
  @Transactional
  void 고객_주소_추가() {
    CreateCustomerDto createCustomerDto = new CreateCustomerDto("dev12","Programmers123!", "2000-01-01");
    CustomerDto customerDto = customerService.createCustomer(createCustomerDto);
    CreateCustomerAddressDto createCustomerAddressDto = new CreateCustomerAddressDto("서울시 동작구", "집");
    Customer customer = customerService.findCustomerEntity(customerDto.getLoginId());

    CustomerAddressDto customerAddressDto = customerAddressService.createCustomerAddress(customer.getLoginId(), createCustomerAddressDto);

    assertThat(customerAddressDto.getAddress(), is("서울시 동작구"));
    assertThat(customerAddressDto.getNickname(), is("집"));
  }

  @Test
  @Transactional
  void 고객_주소_목록_조회() {
    CreateCustomerDto createCustomerDto = new CreateCustomerDto("dev12","Programmers123!", "2000-01-01");
    CustomerDto customerDto = customerService.createCustomer(createCustomerDto);
    CreateCustomerAddressDto createCustomerAddressDto = new CreateCustomerAddressDto("서울시 동작구", "집");
    CreateCustomerAddressDto createCustomerAddressDto1 = new CreateCustomerAddressDto("서울시 동작구", "회사");
    Customer customer = customerService.findCustomerEntity(customerDto.getLoginId());

    customerAddressService.createCustomerAddress(customer.getLoginId(), createCustomerAddressDto);
    customerAddressService.createCustomerAddress(customer.getLoginId(), createCustomerAddressDto1);
    List<CustomerAddressDto> customerAddressDtoList = customerAddressService.findCustomerAddress(customer.getLoginId());

    assertThat(customerAddressDtoList.size(), is(2));
    assertThat(customerAddressDtoList.get(0).getNickname(), is("집"));
    assertThat(customerAddressDtoList.get(1).getNickname(), is("회사"));
  }

  @Test
  @Transactional
  void 고객_주소_수정() {
    CreateCustomerDto createCustomerDto = new CreateCustomerDto("dev12","Programmers123!", "2000-01-01");
    CustomerDto customerDto = customerService.createCustomer(createCustomerDto);
    CreateCustomerAddressDto createCustomerAddressDto = new CreateCustomerAddressDto("서울시 동작구", "집");
    Customer customer = customerService.findCustomerEntity(customerDto.getLoginId());
    CustomerAddressDto customerAddressDto = customerAddressService.createCustomerAddress(customer.getLoginId(), createCustomerAddressDto);

    UpdateCustomerAddressDto updateCustomerAddressDto = new UpdateCustomerAddressDto("서울시 서초구", "회사");

    customerAddressService.updateCustomerAddress(customer.getLoginId(), customerAddressDto.getId(), updateCustomerAddressDto);
    List<CustomerAddressDto> customerAddressDtoList = customerAddressService.findCustomerAddress(customer.getLoginId());

    assertThat(customerAddressDtoList.get(0).getNickname(), is("회사"));
    assertThat(customerAddressDtoList.get(0).getAddress(), is("서울시 서초구"));
  }

  @Test
  @Transactional
  void 고객_주소_삭제() {
    CreateCustomerDto createCustomerDto = new CreateCustomerDto("dev12","Programmers123!", "2000-01-01");
    CustomerDto customerDto = customerService.createCustomer(createCustomerDto);
    CreateCustomerAddressDto createCustomerAddressDto = new CreateCustomerAddressDto("서울시 동작구", "집");
    Customer customer = customerService.findCustomerEntity(customerDto.getLoginId());
    CustomerAddressDto customerAddressDto = customerAddressService.createCustomerAddress(customer.getLoginId(), createCustomerAddressDto);

    customerAddressService.deleteCustomerAddress(customer.getLoginId(), customerAddressDto.getId());
    List<CustomerAddressDto> customerAddressDtoList = customerAddressService.findCustomerAddress(customer.getLoginId());

    assertThat(customerAddressDtoList.size(), is(0));
  }
}