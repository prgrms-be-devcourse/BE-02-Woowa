package com.example.woowa.voucher.converter;

import com.example.woowa.voucher.dto.CreateVoucherDto;
import com.example.woowa.voucher.dto.VoucherDto;
import com.example.woowa.voucher.entity.Voucher;
import com.example.woowa.voucher.enums.VoucherType;

public class VoucherConverter {
    public static VoucherDto toVoucherDto(Voucher voucher) {
        return new VoucherDto(voucher.getId(), voucher.getVoucherType(), voucher.getDiscountValue(), voucher.getExpirationDate(),
                voucher.getCode());
    }

    public static Voucher toVoucher(CreateVoucherDto createVoucherDto) throws Exception {
        return new Voucher(VoucherType.fromString(createVoucherDto.getVoucherType()),
                createVoucherDto.getDiscountValue(), createVoucherDto.getExpirationDate());
    }
}
