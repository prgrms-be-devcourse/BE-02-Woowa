package com.example.woowa.delivery.service;

import com.example.woowa.customer.customer.dto.CreateCustomerDto;
import com.example.woowa.customer.customer.dto.CreateCustomerGradeDto;
import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.voucher.entity.Voucher;
import com.example.woowa.customer.voucher.enums.VoucherType;
import com.example.woowa.delivery.entity.Delivery;
import com.example.woowa.delivery.enums.DeliveryStatus;
import com.example.woowa.delivery.repository.DeliveryRepository;
import com.example.woowa.order.order.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeliveryServiceTest {

    @Autowired
    DeliveryService deliveryService;

    @BeforeEach
    @DisplayName("배차 대기 중인 배달들을 생성할 수 있다.")
    void createDelivery() {

        for (int i = 0; i < 100; i++) {
            CustomerGrade customerGrade = new CustomerGrade(5, "일반", 3000, 2);
            Customer customer = new Customer("dev12","Programmers123!", "2000-01-01",customerGrade);
            Voucher voucher = new Voucher(VoucherType.PERCENT, 2, LocalDateTime.now());
            /**
             *      Order order = Order.createOrder()
             *      오더 생성
             *      배달 생성.
             */
        }
    }
    @Test
    @DisplayName("배차 대기 중인 배달들을 조회 할 수 있다.")
    void findDeliveryStatusWaiting() {
        List<Delivery> deliveryList = deliveryService.findByDeliveryStatus(DeliveryStatus.DELIVERY_WAITING);

    }
}