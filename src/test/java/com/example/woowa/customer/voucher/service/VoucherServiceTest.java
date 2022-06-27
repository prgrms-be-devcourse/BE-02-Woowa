package com.example.woowa.customer.voucher.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.example.woowa.customer.customer.dto.CustomerAddressCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerFindResponse;
import com.example.woowa.customer.customer.dto.CustomerGradeCreateRequest;
import com.example.woowa.customer.customer.repository.CustomerGradeRepository;
import com.example.woowa.customer.customer.repository.CustomerRepository;
import com.example.woowa.customer.customer.service.CustomerGradeService;
import com.example.woowa.customer.customer.service.CustomerService;
import com.example.woowa.customer.voucher.dto.VoucherCreateRequest;
import com.example.woowa.customer.voucher.dto.VoucherFindResponse;
import com.example.woowa.customer.voucher.enums.EventType;
import com.example.woowa.customer.voucher.enums.VoucherType;
import com.example.woowa.customer.voucher.repository.VoucherRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VoucherServiceTest {
  @Autowired
  private VoucherService voucherService;

  @Autowired
  private VoucherRepository voucherRepository;

  @Autowired
  private CustomerService customerService;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private CustomerGradeService customerGradeService;

  @Autowired
  private CustomerGradeRepository customerGradeRepository;

  public void makeDefaultCustomerGrade() {
    CustomerGradeCreateRequest customerGradeCreateRequest = new CustomerGradeCreateRequest(5, "일반", 3000, 2);
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
  void settingBeforeTest() {
    makeDefaultCustomerGrade();
  }

  @AfterEach
  void settingAfterTest() {
    customerRepository.deleteAll();
    voucherRepository.deleteAll();
    customerGradeRepository.deleteAll();
  }

  @Test
  @DisplayName("정기 쿠폰 등록")
  void registerMonthlyVoucher() throws Exception {
    VoucherCreateRequest voucherCreateRequest = new VoucherCreateRequest(VoucherType.FiXED.toString(),
        EventType.SPECIAL.toString(), 3000, "2022-12-01 12:00");
    voucherService.createVoucher(voucherCreateRequest);
    String customerLoginId = getCustomerLoginId();

    voucherService.registerMonthlyVoucher(customerLoginId);
    CustomerFindResponse customerFindResponse = customerService.findCustomer(customerLoginId);

    assertThat(customerFindResponse.getVouchers().get(0).getVoucherType(), is(VoucherType.FiXED.toString()));
    assertThat(customerFindResponse.getVouchers().get(0).getEventType(), is(EventType.MONTH.toString()));
    assertThat(customerFindResponse.getVouchers().get(0).getDiscountValue(), is(3000));
  }

  @Test
  @DisplayName("이벤트 쿠폰 등록")
  void registerVoucher() throws Exception {
    VoucherCreateRequest voucherCreateRequest = new VoucherCreateRequest(VoucherType.FiXED.toString(),
        EventType.SPECIAL.toString(), 3000, "2022-12-01 12:00");
    VoucherFindResponse voucherFindResponse = voucherService.createVoucher(voucherCreateRequest);
    String customerId = getCustomerLoginId();
    voucherService.registerVoucher(customerId, voucherFindResponse.getCode());

    List<VoucherFindResponse> vouchers  = voucherService.findUserVoucher(customerId);

    assertThat(vouchers.get(0).getVoucherType(), is(VoucherType.FiXED.toString()));
    assertThat(vouchers.get(0).getEventType(), is(EventType.SPECIAL.toString()));
    assertThat(vouchers.get(0).getDiscountValue(), is(3000));
  }

  @Test
  @DisplayName("쿠폰 발행")
  void createVoucher() throws Exception {
    VoucherCreateRequest voucherCreateRequest = new VoucherCreateRequest(VoucherType.FiXED.toString(),
        EventType.SPECIAL.toString(), 3000, "2022-12-01 12:00");

    VoucherFindResponse voucherFindResponse = voucherService.createVoucher(voucherCreateRequest);

    assertThat(voucherFindResponse.getVoucherType(), is(VoucherType.FiXED.toString()));
    assertThat(voucherFindResponse.getEventType(), is(EventType.SPECIAL.toString()));
    assertThat(voucherFindResponse.getDiscountValue(), is(3000));
    assertThat(voucherFindResponse.getExpirationDate(), is(LocalDateTime.of(2022, 12, 1, 12, 0)));
  }

  @Test
  @DisplayName("쿠폰 조회")
  void findVoucher() throws Exception {
    VoucherCreateRequest voucherCreateRequest = new VoucherCreateRequest(VoucherType.FiXED.toString(),
        EventType.SPECIAL.toString(), 3000, "2022-12-01 12:00");
    VoucherFindResponse voucherFindResponse = voucherService.createVoucher(voucherCreateRequest);

    VoucherFindResponse voucherFindResponse1 = voucherService.findVoucher(voucherFindResponse.getId());

    assertThat(voucherFindResponse1.getVoucherType(), is(VoucherType.FiXED.toString()));
    assertThat(voucherFindResponse1.getEventType(), is(EventType.SPECIAL.toString()));
    assertThat(voucherFindResponse1.getDiscountValue(), is(3000));
    assertThat(voucherFindResponse1.getExpirationDate(), is(LocalDateTime.of(2022, 12, 1, 12, 0)));
  }


  @Test
  @DisplayName("유저 보유 쿠폰 목록 조회")
  void findUserVoucher() throws Exception {
    VoucherCreateRequest voucherCreateRequest = new VoucherCreateRequest(VoucherType.FiXED.toString(),
        EventType.SPECIAL.toString(), 3000, "2022-12-01 12:00");
    VoucherFindResponse voucherFindResponse = voucherService.createVoucher(voucherCreateRequest);
    String customerId = getCustomerLoginId();
    voucherService.registerVoucher(customerId, voucherFindResponse.getCode());

    List<VoucherFindResponse> vouchers  = voucherService.findUserVoucher(customerId);

    assertThat(vouchers.get(0).getVoucherType(), is(VoucherType.FiXED.toString()));
    assertThat(vouchers.get(0).getEventType(), is(EventType.SPECIAL.toString()));
    assertThat(vouchers.get(0).getDiscountValue(), is(3000));
  }

  @Test
  @DisplayName("쿠폰 삭제")
  void deleteVoucher() throws Exception {
    VoucherCreateRequest voucherCreateRequest = new VoucherCreateRequest(VoucherType.FiXED.toString(),
        EventType.SPECIAL.toString(), 3000, "2022-12-01 12:00");
    VoucherFindResponse voucherFindResponse = voucherService.createVoucher(voucherCreateRequest);
    String customerId = getCustomerLoginId();

    voucherService.registerVoucher(customerId, voucherFindResponse.getCode());
    voucherService.deleteVoucher(customerId, voucherFindResponse.getId());
    List<VoucherFindResponse> vouchers  = customerService.findCustomer(customerId).getVouchers();

    assertThat(vouchers.size(), is(0));
  }

}