package com.example.woowa.voucher.service;

import com.example.woowa.customer.entity.Customer;
import com.example.woowa.voucher.dto.CreateVoucherDto;
import com.example.woowa.voucher.dto.VoucherDto;
import java.util.List;

public interface VoucherService {
  List<VoucherDto> giveMonthlyVoucher(String customerLoginId);
  VoucherDto createVoucher(CreateVoucherDto createVoucherDto) throws Exception;
  VoucherDto findVoucher(Long id);
  void deleteVoucher(Long id);
  Customer findCustomerEntity(String loginId);
}
