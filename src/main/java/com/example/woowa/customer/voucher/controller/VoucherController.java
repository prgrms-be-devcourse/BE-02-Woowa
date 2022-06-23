package com.example.woowa.customer.voucher.controller;

import com.example.woowa.customer.voucher.dto.CreateVoucherDto;
import com.example.woowa.customer.voucher.dto.VoucherDto;
import com.example.woowa.customer.voucher.service.VoucherService;

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

    @PostMapping
    public VoucherDto createVoucher(@RequestBody @Valid CreateVoucherDto createVoucherDto)
            throws Exception {
        return voucherService.createVoucher(createVoucherDto);
    }

    @GetMapping("/{id}")
    public VoucherDto readVoucher(@PathVariable Long id) {
        return voucherService.findVoucher(id);
    }

    @DeleteMapping("/{id}")
    public void deleteVoucher(@PathVariable Long id) {
        voucherService.deleteVoucher(id);
    }
}
