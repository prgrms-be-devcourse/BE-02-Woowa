package com.example.woowa.voucher.service.impl;

import com.example.woowa.voucher.converter.VoucherConverter;
import com.example.woowa.voucher.dto.CreateVoucherDto;
import com.example.woowa.voucher.dto.VoucherDto;
import com.example.woowa.voucher.entity.Voucher;
import com.example.woowa.voucher.repository.VoucherRepository;
import com.example.woowa.voucher.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
  private VoucherRepository voucherRepository;

  @Override
  @Transactional
  public VoucherDto createVoucher(CreateVoucherDto createVoucherDto) throws Exception {
    Voucher voucher = VoucherConverter.toVoucher(createVoucherDto);
    voucher = voucherRepository.save(voucher);
    return VoucherConverter.toVoucherDto(voucher);
  }

  @Override
  public VoucherDto readVoucher(Long id) {
    Voucher voucher = voucherRepository.findById(id).orElseThrow(()-> new RuntimeException("voucher not existed"));
    return VoucherConverter.toVoucherDto(voucher);
  }

  @Override
  public void deleteVoucher(Long id) {
    Voucher voucher = voucherRepository.findById(id).orElseThrow(()-> new RuntimeException("voucher not existed"));
    voucherRepository.delete(voucher);
  }
}
