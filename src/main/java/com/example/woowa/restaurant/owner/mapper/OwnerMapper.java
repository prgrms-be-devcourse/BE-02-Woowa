package com.example.woowa.restaurant.owner.mapper;

import com.example.woowa.restaurant.owner.dto.request.OwnerCreateRequest;
import com.example.woowa.restaurant.owner.dto.request.OwnerUpdateRequest;
import com.example.woowa.restaurant.owner.dto.response.OwnerCreateResponse;
import com.example.woowa.restaurant.owner.dto.response.OwnerFindResponse;
import com.example.woowa.restaurant.owner.entity.Owner;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface OwnerMapper {

    Owner toEntity(OwnerCreateRequest ownerCreateRequest);

    OwnerCreateResponse toCreateResponse(Owner owner);

    OwnerFindResponse toFindResponse(Owner owner);

    default void updateEntity(OwnerUpdateRequest ownerUpdateRequest, @MappingTarget Owner owner) {
        owner.update(ownerUpdateRequest.getName(), ownerUpdateRequest.getPhoneNumber());
        owner.changePassword(ownerUpdateRequest.getPassword());
    }

}
