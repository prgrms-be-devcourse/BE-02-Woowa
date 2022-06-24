package com.example.woowa.customer.voucher.service;

import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.customer.repository.CustomerRepository;
import com.example.woowa.customer.voucher.converter.VoucherConverter;
import com.example.woowa.customer.voucher.dto.CreateVoucherDto;
import com.example.woowa.customer.voucher.dto.VoucherDto;
import com.example.woowa.customer.voucher.entity.Voucher;
import com.example.woowa.customer.voucher.enums.VoucherType;
import com.example.woowa.customer.voucher.repository.VoucherRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherRepository voucherRepository;
    private final CustomerRepository customerRepository;

    public List<VoucherDto> giveMonthlyVoucher(String customerLoginId) {
        List<VoucherDto> result = new ArrayList<>();
        Customer customer = findCustomerEntity(customerLoginId);
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

    @Transactional
    public VoucherDto createVoucher(CreateVoucherDto createVoucherDto) throws Exception {
        Voucher voucher = VoucherConverter.toVoucher(createVoucherDto);
        voucher = voucherRepository.save(voucher);
        return VoucherConverter.toVoucherDto(voucher);
    }

    public VoucherDto findVoucher(Long id) {
        Voucher voucher = voucherRepository.findById(id).orElseThrow(() -> new RuntimeException("voucher not existed"));
        return VoucherConverter.toVoucherDto(voucher);
    }

    @Transactional
    public void deleteVoucher(Long id) {
        voucherRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Customer findCustomerEntity(String loginId) {
        Customer customer = customerRepository.findByLoginId(loginId).orElseThrow(() -> new RuntimeException("login id not existed"));
        return customer;
    }
}
