package com.example.woowa.customer.voucher.service;

import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.customer.service.CustomerService;
import com.example.woowa.customer.voucher.converter.VoucherMapper;
import com.example.woowa.customer.voucher.dto.VoucherCreateRequest;
import com.example.woowa.customer.voucher.dto.VoucherFindResponse;
import com.example.woowa.customer.voucher.entity.Voucher;
import com.example.woowa.customer.voucher.enums.EventType;
import com.example.woowa.customer.voucher.enums.VoucherType;
import com.example.woowa.customer.voucher.repository.VoucherRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherRepository voucherRepository;
    private final CustomerService customerService;
    private final VoucherMapper voucherMapper;

    @Transactional
    public List<VoucherFindResponse> registerMonthlyVoucher(String customerLoginId) {
        List<VoucherFindResponse> result = new ArrayList<>();
        Customer customer = customerService.findCustomerEntity(customerLoginId);
        if (!customer.getIsIssued()) {
            CustomerGrade customerGrade = customer.getCustomerGrade();
            if (customerGrade == null) {
                throw new RuntimeException("고객 등급이 존재하지 않습니다.");
            }
            int disountPrice = customerGrade.getDiscountPrice();
            int voucherCount = customerGrade.getVoucherCount();
            LocalDateTime time = LocalDateTime.now().plusDays(30);
            for (int i = 0; i < voucherCount; i++) {
                Voucher voucher = new Voucher(VoucherType.FiXED, EventType.MONTH, disountPrice, time);
                voucherRepository.save(voucher);
                customer.addVoucher(voucher);
                result.add(voucherMapper.toVoucherDto(voucher));
            }
            customer.setIsIssued(true);
        }
        else {
            throw  new RuntimeException("이미 정기 쿠폰을 발급받았습니다.");
        }
        return result;
    }

    @Transactional
    public VoucherFindResponse registerVoucher(String customerLoginId, String code) {
        Voucher voucher = voucherRepository.findByCode(code).orElseThrow(()-> new RuntimeException("voucher not existed"));
        if (isOkayToRegister(voucher)) {
            Customer customer = customerService.findCustomerEntity(customerLoginId);
            customer.addVoucher(voucher);
        }
        return voucherMapper.toVoucherDto(voucher);
    }

    private boolean isOkayToRegister(Voucher voucher) {
        return LocalDateTime.now().isBefore(voucher.getExpirationDate()) && !voucher.getIsUse();
    }

    @Transactional
    public VoucherFindResponse createVoucher(VoucherCreateRequest voucherCreateRequest) throws Exception {
        Voucher voucher = voucherMapper.toVoucher(voucherCreateRequest);
        voucher = voucherRepository.save(voucher);
        return voucherMapper.toVoucherDto(voucher);
    }

    public VoucherFindResponse findVoucher(Long id) {
        Voucher voucher = findVoucherById(id);
        return voucherMapper.toVoucherDto(voucher);
    }

    public List<VoucherFindResponse> findUserVoucher(String customerLoginId) {
        Customer customer = customerService.findCustomerEntity(customerLoginId);
        return customer.getVouchers().stream().map(voucherMapper::toVoucherDto).collect(Collectors.toList());
    }

    @Transactional
    public void deleteVoucher(String customerLoginId, Long id) {
        Voucher voucher = findVoucherById(id);
        Customer customer = customerService.findCustomerEntity(customerLoginId);
        customer.removeVoucher(voucher);
        voucherRepository.delete(voucher);
    }

    public Voucher findVoucherById(Long voucherId) {
        return voucherRepository.findById(voucherId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 voucherId 입니다."));
    }
}
