package com.example.woowa.voucher.service;

import com.example.woowa.voucher.dto.CreateVoucherDto;
import com.example.woowa.voucher.dto.VoucherDto;

public interface VoucherService {
  VoucherDto createVoucher(CreateVoucherDto createVoucherDto) throws Exception;
  VoucherDto readVoucher(Long id);
  void deleteVoucher(Long id);
}
