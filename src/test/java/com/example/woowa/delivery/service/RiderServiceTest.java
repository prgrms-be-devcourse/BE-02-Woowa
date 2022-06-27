package com.example.woowa.delivery.service;

import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.delivery.entity.Delivery;
import com.example.woowa.delivery.entity.Rider;
import com.example.woowa.delivery.enums.DeliveryStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RiderServiceTest {

    @Autowired
    RiderService riderService;

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    AreaCodeService areaCodeService;

    @BeforeEach
    public void save() {
        riderService.deleteAll();
        Rider rider = new Rider("id", "password", "name", "폰");
        riderService.save(rider);
    }

    @Test
    @DisplayName("라이더는 배차대기중인 배달들을 조회할 수 있다.")
    public void find() {
        List<Delivery> deliveryList = deliveryService.findByDeliveryStatus(DeliveryStatus.DELIVERY_WAITING);
    }

    @Test
    @DisplayName("라이더는 배달 지역을 추가 할 수 있다.")
    @Transactional
    public void addRiderAreaCode() {
        List<Rider> all = riderService.findAll();
        Rider rider = all.get(0);
        AreaCode areaCode = areaCodeService.findByAddress("서울특별시 종로구");
        riderService.addRiderAreaCode(rider, areaCode);
    }

    @Test
    @DisplayName("라이더 배차대기중인 배달을 배달할 수 있다.")
    public void chooseDelivery() {
        List<Delivery> all = deliveryService.findAll();
        Delivery delivery = all.get(0);
    }
}