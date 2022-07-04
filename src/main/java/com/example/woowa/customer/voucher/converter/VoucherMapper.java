package com.example.woowa.customer.voucher.converter;

import com.example.woowa.common.EnumFindable;
import com.example.woowa.customer.voucher.dto.VoucherCreateRequest;
import com.example.woowa.customer.voucher.dto.VoucherFindResponse;
import com.example.woowa.customer.voucher.entity.Voucher;
import com.example.woowa.customer.voucher.enums.EventType;
import com.example.woowa.customer.voucher.enums.VoucherType;
import java.time.format.DateTimeFormatter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", imports = {EnumFindable.class, VoucherType.class, EventType.class,
    DateTimeFormatter.class})
public interface VoucherMapper {

  @Mappings({
      @Mapping(target = "voucherType", expression = "java(voucher.getVoucherType().toString())"),
      @Mapping(target = "eventType", expression = "java(voucher.getEventType().toString())")
  })
  VoucherFindResponse toVoucherDto(Voucher voucher);

  @Mappings({
      @Mapping(target = "voucherType", expression = "java(EnumFindable.find(voucherCreateRequest.getVoucherType(), VoucherType.values()))"),
      @Mapping(target = "eventType", expression = "java(EnumFindable.find(voucherCreateRequest.getEventType(), EventType.values()))"),
      @Mapping(target = "expirationDate", expression = "java(LocalDateTime.parse(voucherCreateRequest.getExpirationDate(), DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm\")))")
  })
  Voucher toVoucher(VoucherCreateRequest voucherCreateRequest);
}
