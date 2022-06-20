package com.example.woowa.voucher.service.impl;

import com.example.woowa.voucher.repository.VoucherRepository;
import com.example.woowa.voucher.service.VoucherService;
import org.springframework.stereotype.Service;

@Service
public class VoucherServiceImpl implements VoucherService {
  private VoucherRepository voucherRepository;

  public VoucherServiceImpl(VoucherRepository voucherRepository) {
    this.voucherRepository = voucherRepository;
  }
}
