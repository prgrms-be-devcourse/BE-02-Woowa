package com.example.woowa.restaurant.advertisement.mapper;

import com.example.woowa.common.EnumFindable;
import com.example.woowa.restaurant.advertisement.dto.request.AdvertisementCreateRequest;
import com.example.woowa.restaurant.advertisement.dto.request.AdvertisementUpdateRequest;
import com.example.woowa.restaurant.advertisement.dto.response.AdvertisementCreateResponse;
import com.example.woowa.restaurant.advertisement.dto.response.AdvertisementFindResponse;
import com.example.woowa.restaurant.advertisement.entity.Advertisement;
import com.example.woowa.restaurant.advertisement.enums.RateType;
import com.example.woowa.restaurant.advertisement.enums.UnitType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(imports = {RateType.class, UnitType.class},
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdvertisementMapper {

    @Mappings({
        @Mapping(target = "rateType", expression = "java(com.example.woowa.common.EnumFindable.find(advertisementCreateRequest.getRateType(), RateType.values()))"),
        @Mapping(target = "unitType", expression = "java(com.example.woowa.common.EnumFindable.find(advertisementCreateRequest.getUnitType(), UnitType.values()))")
    })
    Advertisement toEntity(AdvertisementCreateRequest advertisementCreateRequest);

    @Mappings({
        @Mapping(target = "rateType", expression = "java(advertisement.getRateType().getType())"),
        @Mapping(target = "unitType", expression = "java(advertisement.getUnitType().getType())")
    })
    AdvertisementCreateResponse toCreateResponse(Advertisement advertisement);

    @Mappings({
        @Mapping(target = "rateType", expression = "java(advertisement.getRateType().getType())"),
        @Mapping(target = "unitType", expression = "java(advertisement.getUnitType().getType())")
    })
    AdvertisementFindResponse toFindResponse(Advertisement advertisement);

    default void updateEntity(AdvertisementUpdateRequest advertisementUpdateRequest, @MappingTarget Advertisement advertisement) {
        advertisement.changeTitle(advertisementUpdateRequest.getTitle());
        advertisement.changeUnitType(EnumFindable.find(advertisementUpdateRequest.getUnitType(), UnitType.values()));
        advertisement.changeRateType(EnumFindable.find(advertisementUpdateRequest.getRateType(), RateType.values()));
        advertisement.changeRate(advertisementUpdateRequest.getRate());
        advertisement.changeDescription(advertisementUpdateRequest.getDescription());
    }

}
