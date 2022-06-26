package com.example.woowa.customer.voucher.controller;

import com.example.woowa.customer.voucher.dto.VoucherCreateRequest;
import com.example.woowa.customer.voucher.dto.VoucherFindResponse;
import com.example.woowa.customer.voucher.service.VoucherService;

import java.util.List;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vouchers")
public class VoucherController {
    private final VoucherService voucherService;

    @GetMapping("/month/{loginId}")
    public List<VoucherFindResponse> registerMonthlyVoucher(@PathVariable String loginId)
        throws Exception {
        return voucherService.registerMonthlyVoucher(loginId);
    }

    @GetMapping("/{loginId}/{code}")
    public VoucherFindResponse registerVoucher(@PathVariable String loginId, @PathVariable String code)
        throws Exception {
        return voucherService.registerVoucher(loginId, code);
    }

    @PostMapping
    public VoucherFindResponse createVoucher(@RequestBody @Valid VoucherCreateRequest voucherCreateRequest)
            throws Exception {
        return voucherService.createVoucher(voucherCreateRequest);
    }

    @GetMapping("/{id}")
    public VoucherFindResponse readVoucher(@PathVariable Long id) {
        return voucherService.findVoucher(id);
    }

    @DeleteMapping("/{loginId}/{id}")
    public String deleteVoucher(@PathVariable String loginId, @PathVariable Long id) {
        voucherService.deleteVoucher(loginId, id);
        return "delete id - " + id;
    }
}
