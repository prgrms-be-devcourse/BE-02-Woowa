package com.example.woowa.voucher.service.impl;

import com.example.woowa.voucher.repository.VoucherRepository;
import com.example.woowa.voucher.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
  private VoucherRepository voucherRepository;
}
