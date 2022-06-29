package com.example.woowa.customer.voucher.service;

import com.example.woowa.customer.customer.converter.CustomerMapper;
import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.customer.service.CustomerService;
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
    private final CustomerMapper customerMapper;

    @Transactional
    public List<VoucherFindResponse> registerMonthlyVoucher(String customerLoginId) {
        List<VoucherFindResponse> result = new ArrayList<>();
        Customer customer = customerService.findCustomerEntity(customerLoginId);
        if (!customer.getIsIssued()) {
            CustomerGrade customerGrade = customer.getCustomerGrade();
            int disountPrice = customerGrade.getDiscountPrice();
            int voucherCount = customerGrade.getVoucherCount();
            LocalDateTime time = LocalDateTime.now().plusDays(30);
            for (int i = 0; i < voucherCount; i++) {
                Voucher voucher = new Voucher(VoucherType.FiXED, EventType.MONTH, disountPrice, time);
                voucherRepository.save(voucher);
                customer.addVoucher(voucher);
                result.add(customerMapper.toVoucherDto(voucher));
            }
            customer.setIsIssued(true);
        }
        return result;
    }

    @Transactional
    public VoucherFindResponse registerVoucher(String customerLoginId, String code) {
        Voucher voucher = voucherRepository.findByCode(code).orElseThrow(()-> new RuntimeException("voucher not existed"));
        if (LocalDateTime.now().isBefore(voucher.getExpirationDate())) {
            Customer customer = customerService.findCustomerEntity(customerLoginId);
            customer.addVoucher(voucher);
        }
        return customerMapper.toVoucherDto(voucher);
    }

    @Transactional
    public VoucherFindResponse createVoucher(VoucherCreateRequest voucherCreateRequest) throws Exception {
        Voucher voucher = customerMapper.toVoucher(voucherCreateRequest);
        voucher = voucherRepository.save(voucher);
        return customerMapper.toVoucherDto(voucher);
    }

    public VoucherFindResponse findVoucher(Long id) {
        Voucher voucher = findVoucherEntity(id);
        return customerMapper.toVoucherDto(voucher);
    }

    public List<VoucherFindResponse> findUserVoucher(String customerLoginId) {
        Customer customer = customerService.findCustomerEntity(customerLoginId);
        return customer.getVouchers().stream().map(customerMapper::toVoucherDto).collect(Collectors.toList());
    }

    @Transactional
    public void deleteVoucher(String customerLoginId, Long id) {
        Voucher voucher = findVoucherEntity(id);
        Customer customer = customerService.findCustomerEntity(customerLoginId);
        customer.removeVoucher(voucher);
        voucherRepository.deleteById(id);
    }

    private Voucher findVoucherEntity(Long id) {
        return voucherRepository.findById(id).orElseThrow(() -> new RuntimeException("voucher not existed"));
    }
}
