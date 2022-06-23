package com.example.woowa.customer.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.woowa.customer.dto.CreateCustomerDto;
import com.example.woowa.customer.dto.CreateCustomerGradeDto;
import com.example.woowa.customer.dto.CustomerDto;
import com.example.woowa.customer.dto.UpdateCustomerDto;
import com.example.woowa.customer.entity.CustomerGrade;
import com.example.woowa.customer.repository.CustomerGradeRepository;
import com.example.woowa.customer.repository.CustomerRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    CustomerGrade defaultCustomerGrade = customerGradeService.findDefaultCustomerGrade();

    CustomerDto customerDto = customerService.createCustomer(defaultCustomerGrade, createCustomerDto);

    assertThat(customerDto.getLoginId(), is("dev12"));
    assertThat(customerDto.getPoint(), is(0));
    assertThat(customerDto.getBirthdate(), is(LocalDate.of(2000,1,1).toString()));
    assertThat(customerDto.getCustomerGrade().getOrderCount(), is(5));
    assertThat(customerDto.getCustomerGrade().getGrade(), is("일반"));
    assertThat(customerDto.getCustomerGrade().getDiscountPrice(), is(3000));
    assertThat(customerDto.getCustomerGrade().getVoucherCount(), is(2));
  }

  @Test
  void 잘못된_유저_생성1_닉네임() {
    CreateCustomerDto createCustomerDto = new CreateCustomerDto("devcourse","Programmers123!", "2000-01-01");

    assertThrows(AssertionError.class, ()-> {
      CustomerGrade defaultCustomerGrade = customerGradeService.findDefaultCustomerGrade();
      customerService.createCustomer(defaultCustomerGrade, createCustomerDto);
    });
  }

  @Test
  void 잘못된_유저_생성2_비밀번호() {
    CreateCustomerDto createCustomerDto = new CreateCustomerDto("dev12","Programmers123", "2000-01-01");

    assertThrows(AssertionError.class, ()-> {
      CustomerGrade defaultCustomerGrade = customerGradeService.findDefaultCustomerGrade();
      customerService.createCustomer(defaultCustomerGrade, createCustomerDto);
    });
  }

  @Test
  void 잘못된_유저_생성3_생년월일() {
    CreateCustomerDto createCustomerDto = new CreateCustomerDto("dev12","Programmers123!", "hello");

    assertThrows(AssertionError.class, ()-> {
      CustomerGrade defaultCustomerGrade = customerGradeService.findDefaultCustomerGrade();
      customerService.createCustomer(defaultCustomerGrade, createCustomerDto);
    });
  }

  @Test
  void 유저_조회() {
    CreateCustomerDto createCustomerDto = new CreateCustomerDto("dev12","Programmers123!", "2000-01-01");
    CustomerGrade defaultCustomerGrade = customerGradeService.findDefaultCustomerGrade();
    customerService.createCustomer(defaultCustomerGrade, createCustomerDto);

    CustomerDto customerDto = customerService.readCustomer(createCustomerDto.getLoginId());

    assertThat(customerDto.getLoginId(), is("dev12"));
    assertThat(customerDto.getPoint(), is(0));
    assertThat(customerDto.getBirthdate(), is(LocalDate.of(2000,1,1).toString()));
    assertThat(customerDto.getCustomerGrade().getOrderCount(), is(5));
    assertThat(customerDto.getCustomerGrade().getGrade(), is("일반"));
    assertThat(customerDto.getCustomerGrade().getDiscountPrice(), is(3000));
    assertThat(customerDto.getCustomerGrade().getVoucherCount(), is(2));
  }

  @Test
  void 유저_정보_업데이트() {
    CreateCustomerDto createCustomerDto = new CreateCustomerDto("dev12","Programmers123!", "2000-01-01");
    CustomerGrade defaultCustomerGrade = customerGradeService.findDefaultCustomerGrade();
    CustomerDto customerDto = customerService.createCustomer(defaultCustomerGrade, createCustomerDto);

    UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto("Programmers234!");
    customerService.updateCustomer(customerDto.getLoginId(), updateCustomerDto);
    CustomerDto customerDto1 = customerService.readCustomer(customerDto.getLoginId());

    assertThat(customerDto1.getLoginId(), is("dev12"));
    assertThat(customerDto.getPoint(), is(0));
    assertThat(customerDto.getBirthdate(), is(LocalDate.of(2000,1,1).toString()));
    assertThat(customerDto.getCustomerGrade().getOrderCount(), is(5));
    assertThat(customerDto.getCustomerGrade().getGrade(), is("일반"));
    assertThat(customerDto.getCustomerGrade().getDiscountPrice(), is(3000));
    assertThat(customerDto.getCustomerGrade().getVoucherCount(), is(2));
  }

  @Test
  void 잘못된_유저_정보_업데이트() {
    CreateCustomerDto createCustomerDto = new CreateCustomerDto("dev12","Programmers123!", "2000-01-01");
    CustomerGrade defaultCustomerGrade = customerGradeService.findDefaultCustomerGrade();
    CustomerDto customerDto = customerService.createCustomer(defaultCustomerGrade, createCustomerDto);

    assertThrows(AssertionError.class, ()-> {
      UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto("programmers");
      customerService.updateCustomer(customerDto.getLoginId(), updateCustomerDto);
    });
  }

  @Test
  void 유저_삭제() {
    CreateCustomerDto createCustomerDto = new CreateCustomerDto("dev12","Programmers123!", "2000-01-01");
    CustomerGrade defaultCustomerGrade = customerGradeService.findDefaultCustomerGrade();
    CustomerDto customerDto = customerService.createCustomer(defaultCustomerGrade, createCustomerDto);

    customerService.deleteCustomer(customerDto.getLoginId());

    assertThrows(Exception.class, ()-> {
      customerService.readCustomer(customerDto.getLoginId());
    });
  }
}