package com.example.woowa.customer.voucher.converter;

import com.example.woowa.customer.voucher.dto.VoucherCreateRequest;
import com.example.woowa.customer.voucher.dto.VoucherFindResponse;
import com.example.woowa.customer.voucher.entity.Voucher;
import com.example.woowa.customer.voucher.enums.EventType;
import com.example.woowa.customer.voucher.enums.VoucherType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class VoucherConverter {
    public static VoucherFindResponse toVoucherDto(Voucher voucher) {
        return new VoucherFindResponse(voucher.getId(), voucher.getVoucherType().toString(), voucher.getEventType().toString(), voucher.getDiscountValue(), voucher.getExpirationDate(),
                voucher.getCode());
    }

    public static Voucher toVoucher(VoucherCreateRequest voucherCreateRequest) throws Exception {
        validateVoucher(VoucherType.of(voucherCreateRequest.getVoucherType()),
            EventType.of(voucherCreateRequest.getEventType()), voucherCreateRequest.getDiscountValue(), voucherCreateRequest.getExpirationDate());
        VoucherType voucherType = VoucherType.of(voucherCreateRequest.getVoucherType());
        EventType eventType = EventType.of(voucherCreateRequest.getEventType());
        LocalDateTime localDateTime = LocalDateTime.parse(voucherCreateRequest.getExpirationDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return new Voucher(voucherType, eventType, voucherCreateRequest.getDiscountValue(),
            localDateTime);
    }

    private static void validateVoucher(
        VoucherType voucherType, EventType eventType,
        Integer discountValue,
        String expirationDate) {
        assert voucherType != null;
        assert voucherType.isValidAmount(discountValue);
        assert eventType != null;
        assert discountValue > 0;
        assert Pattern.matches("^(19|20)\\d{2}[-](0?[1-9]|[12][0-9]|3[01])[-](0?[1-9]|1[012]) (\\d{1,2}[:]\\d{2})$", expirationDate);
    }
}
