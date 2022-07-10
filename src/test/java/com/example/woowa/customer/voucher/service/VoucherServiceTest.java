package com.example.woowa.customer.voucher.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.customer.service.CustomerService;
import com.example.woowa.customer.voucher.dto.VoucherCreateRequest;
import com.example.woowa.customer.voucher.dto.VoucherFindResponse;
import com.example.woowa.customer.voucher.entity.Voucher;
import com.example.woowa.customer.voucher.enums.EventType;
import com.example.woowa.customer.voucher.enums.VoucherType;
import com.example.woowa.customer.voucher.repository.VoucherRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
class VoucherServiceTest {
  @Autowired
  private VoucherService voucherService;

  @MockBean
  private CustomerService customerService;

  @MockBean
  private VoucherRepository voucherRepository;

  @Test
  @DisplayName("정기 쿠폰 등록")
  void registerMonthlyVoucher() throws Exception {
    CustomerGrade customerGrade = new CustomerGrade(5, "일반", 3000, 2);
    Customer customer = new Customer("dev12","Programmers123!", LocalDate.of(2000,1,1), customerGrade);
    given(customerService.findCustomerEntity(anyString())).willReturn(customer);

    List<VoucherFindResponse> vouchers = voucherService.registerMonthlyVoucher("dev12");

    Assertions.assertThat(vouchers.size()).isEqualTo(2);
    Assertions.assertThat(vouchers.get(0).getVoucherType()).isEqualTo(VoucherType.FiXED.toString());
    Assertions.assertThat(vouchers.get(0).getEventType()).isEqualTo(EventType.MONTH.toString());
    Assertions.assertThat(vouchers.get(0).getDiscountValue()).isEqualTo(3000);
  }

  @Test
  @DisplayName("이벤트 쿠폰 등록")
  void registerVoucher() throws Exception {
    CustomerGrade customerGrade = new CustomerGrade(5, "일반", 3000, 2);
    Customer customer = new Customer("dev12","Programmers123!", LocalDate.of(2000,1,1), customerGrade);
    given(customerService.findCustomerEntity(anyString())).willReturn(customer);
    given(voucherRepository.findByCode(anyString())).willReturn(Optional.of(new Voucher(VoucherType.FiXED, EventType.SPECIAL, 3000, LocalDateTime.now().plusDays(10))));

    VoucherFindResponse voucherFindResponse = voucherService.registerVoucher("dev12", UUID.randomUUID().toString());

    Assertions.assertThat(voucherFindResponse.getVoucherType()).isEqualTo(VoucherType.FiXED.toString());
    Assertions.assertThat(voucherFindResponse.getEventType()).isEqualTo(EventType.SPECIAL.toString());
    Assertions.assertThat(voucherFindResponse.getDiscountValue()).isEqualTo(3000);
  }

  @Test
  @DisplayName("쿠폰 발행")
  void createVoucher() throws Exception {
    Voucher voucher = new Voucher(VoucherType.FiXED, EventType.SPECIAL, 3000, LocalDateTime.now().plusDays(10));
    given(voucherRepository.save(any())).willReturn(voucher);

    VoucherCreateRequest voucherCreateRequest = new VoucherCreateRequest(VoucherType.FiXED.toString(),
        EventType.SPECIAL.toString(), 3000, "2022-12-01 12:00");
    VoucherFindResponse voucherFindResponse = voucherService.createVoucher(voucherCreateRequest);

    Assertions.assertThat(voucherFindResponse.getVoucherType()).isEqualTo(VoucherType.FiXED.toString());
    Assertions.assertThat(voucherFindResponse.getEventType()).isEqualTo(EventType.SPECIAL.toString());
    Assertions.assertThat(voucherFindResponse.getDiscountValue()).isEqualTo(3000);
  }

  @Test
  @DisplayName("쿠폰 조회")
  void findVoucher() throws Exception {
    given(voucherRepository.findById(anyLong())).willReturn(Optional.of(new Voucher(VoucherType.FiXED, EventType.SPECIAL, 3000, LocalDateTime.now().plusDays(10))));

    VoucherFindResponse voucherFindResponse = voucherService.findVoucher(1l);

    Assertions.assertThat(voucherFindResponse.getVoucherType()).isEqualTo(VoucherType.FiXED.toString());
    Assertions.assertThat(voucherFindResponse.getEventType()).isEqualTo(EventType.SPECIAL.toString());
    Assertions.assertThat(voucherFindResponse.getDiscountValue()).isEqualTo(3000);
  }

  @Test
  @DisplayName("유저 보유 쿠폰 목록 조회")
  void findUserVoucher() throws Exception {
    CustomerGrade customerGrade = new CustomerGrade(5, "일반", 3000, 2);
    Customer customer = new Customer("dev12","Programmers123!", LocalDate.of(2000,1,1), customerGrade);
    customer.addVoucher(new Voucher(VoucherType.FiXED, EventType.SPECIAL, 3000, LocalDateTime.now().plusDays(10)));
    customer.addVoucher(new Voucher(VoucherType.FiXED, EventType.SPECIAL, 3000, LocalDateTime.now().plusDays(10)));
    given(customerService.findCustomerEntity(anyString())).willReturn(customer);

    List<VoucherFindResponse> vouchers  = voucherService.findUserVoucher("dev12");

    Assertions.assertThat(vouchers.get(0).getVoucherType()).isEqualTo(VoucherType.FiXED.toString());
    Assertions.assertThat(vouchers.get(0).getEventType()).isEqualTo(EventType.SPECIAL.toString());
    Assertions.assertThat(vouchers.get(0).getDiscountValue()).isEqualTo(3000);
  }

  @Test
  @DisplayName("쿠폰 삭제")
  void deleteVoucher() throws Exception {
    CustomerGrade customerGrade = new CustomerGrade(5, "일반", 3000, 2);
    Customer customer = new Customer("dev12","Programmers123!", LocalDate.of(2000,1,1), customerGrade);
    Voucher voucher = new Voucher(VoucherType.FiXED, EventType.SPECIAL, 3000, LocalDateTime.now().plusDays(10));
    customer.addVoucher(voucher);
    given(voucherRepository.findById(anyLong())).willReturn(Optional.of(voucher));
    given(customerService.findCustomerEntity(anyString())).willReturn(customer);

    voucherService.deleteVoucher("dev12", 1l);

    verify(voucherRepository).delete(voucher);
  }
}
