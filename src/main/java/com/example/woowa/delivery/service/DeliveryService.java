package com.example.woowa.delivery.service;

import com.example.woowa.common.exception.ErrorMessage;
import com.example.woowa.delivery.dto.DeliveryCreateRequest;
import com.example.woowa.delivery.dto.DeliveryResponse;
import com.example.woowa.delivery.entity.Delivery;
import com.example.woowa.delivery.entity.Rider;
import com.example.woowa.delivery.enums.DeliveryStatus;
import com.example.woowa.delivery.mapper.DeliveryMapper;
import com.example.woowa.delivery.repository.DeliveryRepository;
import com.example.woowa.order.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private final DeliveryMapper deliveryMapper;

    private final RiderService riderService;

    @Transactional
    public Long save(DeliveryCreateRequest deliveryCreateRequest) {
        // 작업
        Delivery delivery = deliveryMapper.toEntity(deliveryCreateRequest);
        deliveryRepository.save(delivery);
        return delivery.getId();
    }

    public Page<DeliveryResponse> findWaitingDelivery(PageRequest pageRequest) {
        return deliveryRepository.findByDeliveryStatus(pageRequest, DeliveryStatus.DELIVERY_WAITING)
            .map(deliveryMapper::toResponse);
    }

    public Delivery findEntityById(Long id) {
        return deliveryRepository.findById(id)
            .orElseThrow(
                () -> new RuntimeException(ErrorMessage.NOT_FOUND_DELIVERY_ID.getMessage()));
    }

    public DeliveryResponse findResponseById(Long id) {
        Delivery delivery = findEntityById(id);
        return deliveryMapper.toResponse(delivery);
    }

    /**
     * 추가할 사항 : 동시성 반영.
     */
    @Transactional
    public void acceptDelivery(Long deliveryId, Long riderId, int deliveryMinute, int cookMinute) {
        Delivery delivery = findEntityById(deliveryId);
        if (!delivery.getDeliveryStatus().equals(DeliveryStatus.DELIVERY_WAITING)) {
            throw new RuntimeException(ErrorMessage.ALREADY_RECEIVE_DELIVERY.getMessage());
        }
        Rider rider = riderService.findEntityById(riderId);
        delivery.accept(rider, deliveryMinute, cookMinute);
    }

    @Transactional
    public void delay(Long id, int delayMinute) {
        Delivery delivery = findEntityById(id);
        delivery.delay(delayMinute);
    }

    @Transactional
    public void pickUp(Long id) {
        Delivery delivery = findEntityById(id);
        delivery.pickUp(30);
    }

    @Transactional
    public void finish(Long id) {
        Delivery delivery = findEntityById(id);
        delivery.finish();
    }

    @Transactional
    public Long createDelivery(Order order, String restaurantAddress, String customerAddress,
        int deliveryFee) {
        Delivery delivery = Delivery.createDelivery(order, restaurantAddress, customerAddress,
            deliveryFee);
        return deliveryRepository.save(delivery).getId();
    }
}
