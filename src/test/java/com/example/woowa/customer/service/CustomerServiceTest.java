package com.example.woowa.customer.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.woowa.customer.customer.dto.CreateCustomerDto;
import com.example.woowa.customer.customer.dto.CreateCustomerGradeDto;
import com.example.woowa.customer.customer.dto.CustomerDto;
import com.example.woowa.customer.customer.dto.UpdateCustomerDto;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.customer.repository.CustomerGradeRepository;
import com.example.woowa.customer.customer.repository.CustomerRepository;
import java.time.LocalDate;

import com.example.woowa.customer.customer.service.CustomerGradeService;
import com.example.woowa.customer.customer.service.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

  @BeforeEach
  void 유저_생성_세팅() {
    CreateCustomerGradeDto createCustomerGradeDto = new CreateCustomerGradeDto(5, "일반", 3000, 2);
    customerGradeService.createCustomerGrade(createCustomerGradeDto);
  }

  @AfterEach
  void 유저_생성_세팅_뒷정리() {
    customerRepository.deleteAll();
    customerGradeRepository.deleteAll();
  }

  @Test
  void 유저_생성() {
    CreateCustomerDto createCustomerDto = new CreateCustomerDto("dev12","Programmers123!", "2000-01-01");
    CustomerDto customerDto = customerService.createCustomer(createCustomerDto);

    assertThat(customerDto.getLoginId(), is("dev12"));
    assertThat(customerDto.getPoint(), is(0));
    assertThat(customerDto.getBirthdate(), is(LocalDate.of(2000,1,1).toString()));
    assertThat(customerDto.getCustomerGrade().getOrderCount(), is(5));
    assertThat(customerDto.getCustomerGrade().getGrade(), is("일반"));
    assertThat(customerDto.getCustomerGrade().getDiscountPrice(), is(3000));
    assertThat(customerDto.getCustomerGrade().getVoucherCount(), is(2));
  }

  @Test
  @DisplayName("아이디 입력 오류")
  void createCustomerFail1() {
    CreateCustomerDto createCustomerDto = new CreateCustomerDto("devcourse","Programmers123!", "2000-01-01");

    assertThrows(AssertionError.class, ()-> {
      customerService.createCustomer(createCustomerDto);
    });
  }

  @Test
  @DisplayName("비밀번호 입력 오류")
  void createCustomerFail2() {
    CreateCustomerDto createCustomerDto = new CreateCustomerDto("dev12","Programmers123", "2000-01-01");

    assertThrows(AssertionError.class, ()-> {
      customerService.createCustomer(createCustomerDto);
    });
  }

  @Test
  @DisplayName("생년월일 입력 오류")
  void createCustomerFail3() {
    CreateCustomerDto createCustomerDto = new CreateCustomerDto("dev12","Programmers123!", "hello");

    assertThrows(AssertionError.class, ()-> {
      customerService.createCustomer(createCustomerDto);
    });
  }

  @Test
  @DisplayName("유저 정보 조회")
  void findCustomer() {
    CreateCustomerDto createCustomerDto = new CreateCustomerDto("dev12","Programmers123!", "2000-01-01");
    customerService.createCustomer(createCustomerDto);

    CustomerDto customerDto = customerService.findCustomer(createCustomerDto.getLoginId());

    assertThat(customerDto.getLoginId(), is("dev12"));
    assertThat(customerDto.getPoint(), is(0));
    assertThat(customerDto.getBirthdate(), is(LocalDate.of(2000,1,1).toString()));
    assertThat(customerDto.getCustomerGrade().getOrderCount(), is(5));
    assertThat(customerDto.getCustomerGrade().getGrade(), is("일반"));
    assertThat(customerDto.getCustomerGrade().getDiscountPrice(), is(3000));
    assertThat(customerDto.getCustomerGrade().getVoucherCount(), is(2));
  }

  @Test
  @DisplayName("유저 정보 업데이트")
  void updateCustomer() {
    CreateCustomerDto createCustomerDto = new CreateCustomerDto("dev12","Programmers123!", "2000-01-01");
    CustomerDto customerDto = customerService.createCustomer(createCustomerDto);

    UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto("Programmers234!");
    customerService.updateCustomer(customerDto.getLoginId(), updateCustomerDto);
    CustomerDto customerDto1 = customerService.findCustomer(customerDto.getLoginId());

    assertThat(customerDto1.getLoginId(), is("dev12"));
    assertThat(customerDto.getPoint(), is(0));
    assertThat(customerDto.getBirthdate(), is(LocalDate.of(2000,1,1).toString()));
    assertThat(customerDto.getCustomerGrade().getOrderCount(), is(5));
    assertThat(customerDto.getCustomerGrade().getGrade(), is("일반"));
    assertThat(customerDto.getCustomerGrade().getDiscountPrice(), is(3000));
    assertThat(customerDto.getCustomerGrade().getVoucherCount(), is(2));
  }

  @Test
  @DisplayName("잘못된 유저 정보 업데이트")
  void updateCustomerFail() {
    CreateCustomerDto createCustomerDto = new CreateCustomerDto("dev12","Programmers123!", "2000-01-01");
    CustomerDto customerDto = customerService.createCustomer(createCustomerDto);

    assertThrows(AssertionError.class, ()-> {
      UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto("programmers");
      customerService.updateCustomer(customerDto.getLoginId(), updateCustomerDto);
    });
  }

  @Test
  @DisplayName("유저 삭제")
  void deleteCustomer() {
    CreateCustomerDto createCustomerDto = new CreateCustomerDto("dev12","Programmers123!", "2000-01-01");
    CustomerDto customerDto = customerService.createCustomer(createCustomerDto);

    customerService.deleteCustomer(customerDto.getLoginId());

    assertThrows(Exception.class, ()-> {
      customerService.findCustomer(customerDto.getLoginId());
    });
  }

  @Test
  @DisplayName("고객 등급 기본 등급으로 초기화")
  void initCustomerGrade() {
    CreateCustomerGradeDto createCustomerGradeDto1 = new CreateCustomerGradeDto(10, "실버", 3000, 2);
    customerGradeService.createCustomerGrade(createCustomerGradeDto1);

    CustomerGrade customerGrade = customerService.findDefaultCustomerGrade();

    assertThat(customerGrade.getOrderCount(), is(5));
    assertThat(customerGrade.getGrade(), is("일반"));
    assertThat(customerGrade.getDiscountPrice(), is(3000));
    assertThat(customerGrade.getVoucherCount(), is(2));
  }

  @Test
  @DisplayName("주문 횟수를 반영한 고객 등급 갱신")
  void updateCustomerGrade() {
    CreateCustomerGradeDto createCustomerGradeDto1 = new CreateCustomerGradeDto(10, "실버", 3000, 2);
    customerGradeService.createCustomerGrade(createCustomerGradeDto1);
    CreateCustomerGradeDto createCustomerGradeDto2 = new CreateCustomerGradeDto(15, "골드", 3000, 2);
    customerGradeService.createCustomerGrade(createCustomerGradeDto2);

    //CustomerGrade customerGrade = customerGradeService.findCustomerGrade();

//    assertThat(customerGrade.getOrderCount(), is(5));
//    assertThat(customerGrade.getGrade(), is("일반"));
//    assertThat(customerGrade.getDiscountPrice(), is(3000));
//    assertThat(customerGrade.getVoucherCount(), is(2));
  }
}