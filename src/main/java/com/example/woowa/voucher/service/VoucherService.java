package com.example.woowa.voucher.service;

import com.example.woowa.customer.entity.Customer;
import com.example.woowa.voucher.dto.CreateVoucherDto;
import com.example.woowa.voucher.dto.VoucherDto;
import java.util.List;

public interface VoucherService {
  List<VoucherDto> giveMonthlyVoucher(Customer customer);
  VoucherDto createVoucher(CreateVoucherDto createVoucherDto) throws Exception;
  VoucherDto readVoucher(Long id);
  void deleteVoucher(Long id);
}
