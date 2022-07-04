package com.example.woowa.common.base;

import com.example.woowa.common.base.dto.BaseTimeDto;
import com.example.woowa.common.base.dto.BaseUserResponse;
import com.example.woowa.delivery.dto.RiderResponse;
import com.example.woowa.delivery.entity.Rider;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring")
public interface BaseMapper {

    BaseTimeDto toResponse(BaseTimeEntity baseTimeEntity);

    @SubclassMapping(source = Rider.class, target = RiderResponse.class)
    BaseUserResponse toResponse(BaseLoginEntity baseLoginEntity);
}
