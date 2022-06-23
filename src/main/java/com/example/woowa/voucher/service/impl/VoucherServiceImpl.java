package com.example.woowa.voucher.service.impl;

import com.example.woowa.customer.entity.Customer;
import com.example.woowa.customer.entity.CustomerGrade;
import com.example.woowa.voucher.converter.VoucherConverter;
import com.example.woowa.voucher.dto.CreateVoucherDto;
import com.example.woowa.voucher.dto.VoucherDto;
import com.example.woowa.voucher.entity.Voucher;
import com.example.woowa.voucher.enums.VoucherType;
import com.example.woowa.voucher.repository.VoucherRepository;
import com.example.woowa.voucher.service.VoucherService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class VoucherServiceImpl implements VoucherService {
  private VoucherRepository voucherRepository;

  @Override
  public List<VoucherDto> giveMonthlyVoucher(Customer customer) {
    List<VoucherDto> result = new ArrayList<>();
    CustomerGrade customerGrade = customer.getCustomerGrade();
    int disountPrice = customerGrade.getDiscountPrice();
    int voucherCount = customerGrade.getVoucherCount();
    for (int i = 0; i < voucherCount; i++) {
      Voucher voucher = new Voucher(VoucherType.FiXED, disountPrice, LocalDateTime.now().plusDays(30));
      voucherRepository.save(voucher);
      customer.addVoucher(voucher);
      result.add(VoucherConverter.toVoucherDto(voucher));
    }
    return result;
  }

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
