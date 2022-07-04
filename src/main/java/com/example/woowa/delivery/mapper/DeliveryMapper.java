package com.example.woowa.delivery.mapper;

import com.example.woowa.delivery.dto.DeliveryCreateRequest;
import com.example.woowa.delivery.dto.DeliveryResponse;
import com.example.woowa.delivery.entity.Delivery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    DeliveryResponse toResponse(Delivery delivery);

    Delivery toEntity(DeliveryCreateRequest deliveryCreateRequest);
}
