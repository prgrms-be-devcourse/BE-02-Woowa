package com.example.woowa.delivery.mapper;

import com.example.woowa.delivery.dto.RiderCreateRequest;
import com.example.woowa.delivery.dto.RiderResponse;
import com.example.woowa.delivery.entity.Rider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RiderMapper {

    @Mapping(source = "password", target = "loginPassword")
    Rider toRider(final RiderCreateRequest riderCreateRequest);

    @Mapping(target = "riderAreaList", expression = "java(rider.getRiderAreaCodeList().stream().map(data -> data.getAreaCode().getDefaultAddress()).collect(java.util.stream.Collectors.toList()))")
    RiderResponse toResponse(final Rider rider);

}
