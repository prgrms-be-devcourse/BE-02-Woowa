package com.example.woowa.customer.voucher.converter;

import com.example.woowa.common.EnumFindable;
import com.example.woowa.customer.voucher.dto.VoucherCreateRequest;
import com.example.woowa.customer.voucher.dto.VoucherFindResponse;
import com.example.woowa.customer.voucher.entity.Voucher;
import com.example.woowa.customer.voucher.enums.EventType;
import com.example.woowa.customer.voucher.enums.VoucherType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VoucherConverter {
    public static VoucherFindResponse toVoucherDto(Voucher voucher) {
        return new VoucherFindResponse(voucher.getId(), voucher.getVoucherType().getType(), voucher.getEventType().getType(), voucher.getDiscountValue(), voucher.getExpirationDate(),
                voucher.getCode());
    }

    public static Voucher toVoucher(VoucherCreateRequest voucherCreateRequest) throws Exception {
        VoucherType voucherType = EnumFindable.find(voucherCreateRequest.getVoucherType(), VoucherType.values());
        EventType eventType = EnumFindable.find(voucherCreateRequest.getEventType(), EventType.values());
        LocalDateTime localDateTime = LocalDateTime.parse(voucherCreateRequest.getExpirationDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return new Voucher(voucherType, eventType, voucherCreateRequest.getDiscountValue(),
            localDateTime);
    }
}
