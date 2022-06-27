package com.example.woowa.customer.voucher.service;

import com.example.woowa.customer.voucher.entity.Voucher;
import com.example.woowa.customer.voucher.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoucherEntityService {

    private final VoucherRepository voucherRepository;

    public Voucher findVoucherById(Long voucherId) {
        return voucherRepository.findById(voucherId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 voucherId 입니다."));
    }

}