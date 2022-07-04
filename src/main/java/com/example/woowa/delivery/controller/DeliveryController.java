package com.example.woowa.delivery.controller;

import com.example.woowa.delivery.dto.DeliveryCreateRequest;
import com.example.woowa.delivery.dto.DeliveryResponse;
import com.example.woowa.delivery.service.DeliveryService;
import java.net.URI;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponse> findByDelivery(@PathVariable Long id) {
        DeliveryResponse deliveryResponse = deliveryService.findResponseById(id);
        return ResponseEntity.ok(deliveryResponse);
    }

    @GetMapping("/rider")
    public ResponseEntity<Page<DeliveryResponse>> findByWaitingDelivery(@RequestParam int page) {
        PageRequest pageRequest = PageRequest.of(page, 20);
        return ResponseEntity.ok(deliveryService.findWaitingDelivery(pageRequest));
    }

    // 실제 Delivery 생성은 Order 내부에서 실행.
    @PostMapping
    public ResponseEntity<Void> createDelivery(
        @RequestBody @Valid DeliveryCreateRequest deliveryCreateRequest) {
        Long id = deliveryService.save(deliveryCreateRequest);
        return ResponseEntity.created(URI.create("/api/v1/delivery" + id)).build();
    }

    // 추후 security 도입시 riderId받는 부분은 제거.
    @PatchMapping("/accept/{deliveryId}/{riderId}")
    public ResponseEntity<Void> acceptDelivery(@PathVariable Long deliveryId,
        @PathVariable Long riderId, @RequestParam @NotNull @PositiveOrZero Integer deliveryMinute,
        Integer cookMinute) {
        deliveryService.acceptDelivery(deliveryId, riderId, deliveryMinute, cookMinute);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/delay/{deliveryId}/{riderId}")
    public ResponseEntity<Void> delayDelivery(@PathVariable Long deliveryId,
        @PathVariable Long riderId, @RequestParam @NotNull @PositiveOrZero Integer deliveryMinute) {
        deliveryService.delay(deliveryId, deliveryMinute);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/pickup/{deliveryId}/{riderId}")
    public ResponseEntity<Void> pickUpDelivery(@PathVariable Long deliveryId,
        @PathVariable Long riderId) {
        deliveryService.pickUp(riderId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/finish/{deliveryId}/{riderId}")
    public ResponseEntity<Void> finishDelivery(@PathVariable Long deliveryId,
        @PathVariable Long riderId) {
        deliveryService.finish(riderId);
        return ResponseEntity.noContent().build();
    }
}
