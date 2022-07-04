package com.example.woowa.delivery.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.woowa.TestInitUtil;
import com.example.woowa.delivery.entity.Delivery;
import com.example.woowa.delivery.entity.Rider;
import com.example.woowa.delivery.enums.DeliveryStatus;
import com.example.woowa.delivery.mapper.DeliveryMapper;
import com.example.woowa.delivery.repository.DeliveryRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @InjectMocks
    DeliveryService deliveryService;

    @Mock
    DeliveryMapper deliveryMapper = Mappers.getMapper(DeliveryMapper.class);

    @Mock
    DeliveryRepository deliveryRepository;

    @Mock
    RiderService riderService;

    @Test
    @DisplayName("없는 배달 정보는 조회할 수 없다.")
    public void findFail() {
        given(deliveryRepository.findById(any())).willReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> deliveryService.findResponseById(1L));
    }

    @Test
    @DisplayName("배달기사가 배달을 수락할 수 있다.")
    public void createDelivery() {
        Delivery delivery = TestInitUtil.initDelivery();
        Rider rider = TestInitUtil.initRider();
        given(deliveryRepository.findById(any())).willReturn(Optional.of(delivery));
        given(riderService.findEntityById(any())).willReturn(rider);

        deliveryService.acceptDelivery(delivery.getId(), rider.getId(), 10, 10);
        assertThat(delivery.getRider()).isEqualTo(rider);
        assertThat(delivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.DELIVERY_REGISTRATION);
    }

    @Test
    @DisplayName("배달기사가 이미 접수된 배달을 수락할 수 없다.")
    public void createDeliveryFail() {
        Delivery delivery = TestInitUtil.initDelivery();
        Rider rider = TestInitUtil.initRider();
        delivery.accept(rider, 10, 10);
        given(deliveryRepository.findById(any())).willReturn(Optional.of(delivery));

        assertThrows(RuntimeException.class,
            () -> deliveryService.acceptDelivery(delivery.getId(), rider.getId(), 10, 10));
    }

    @Test
    @DisplayName("배달 도착 시간을 추가할 수 있다.")
    public void delay() {
        Delivery delivery = TestInitUtil.initDelivery();
        Rider rider = TestInitUtil.initRider();
        delivery.accept(rider, 30, 10);
        LocalDateTime arrivalTime = delivery.getArrivalTime();
        given(deliveryRepository.findById(any())).willReturn(Optional.of(delivery));

        deliveryService.delay(delivery.getId(), 30);

        assertThat(delivery.getArrivalTime()).isEqualTo(arrivalTime.plusMinutes(30));
    }

    @Test
    @DisplayName("배달 픽업상태로 변경할 수 있다.")
    public void pickUp() {
        Delivery delivery = TestInitUtil.initDelivery();
        Rider rider = TestInitUtil.initRider();
        delivery.accept(rider, 30, 10);
        LocalDateTime arrivalTime = delivery.getArrivalTime();
        given(deliveryRepository.findById(any())).willReturn(Optional.of(delivery));

        deliveryService.pickUp(1L);

        assertThat(delivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.DELIVERY_PICKUP);
    }

    @Test
    @DisplayName("배달 완료 상태로 변경할 수 있다.")
    public void finish() {
        Delivery delivery = TestInitUtil.initDelivery();
        Rider rider = TestInitUtil.initRider();
        delivery.accept(rider, 30, 10);
        delivery.pickUp(30);
        given(deliveryRepository.findById(any())).willReturn(Optional.of(delivery));

        deliveryService.finish(1L);

        assertThat(delivery.getDeliveryStatus()).isEqualTo(DeliveryStatus.DELIVERY_FINISH);
    }

}