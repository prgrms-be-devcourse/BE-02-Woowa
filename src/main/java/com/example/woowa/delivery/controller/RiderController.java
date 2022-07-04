package com.example.woowa.delivery.controller;

import com.example.woowa.delivery.dto.RiderCreateRequest;
import com.example.woowa.delivery.dto.RiderResponse;
import com.example.woowa.delivery.dto.RiderUpdateRequest;
import com.example.woowa.delivery.service.RiderService;
import java.net.URI;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rider")
@RequiredArgsConstructor
public class RiderController {

    private final RiderService riderService;

    @PostMapping
    public ResponseEntity<Void> sign(
        @RequestBody
        @Valid final RiderCreateRequest riderCreateRequest) {
        long id = riderService.save(riderCreateRequest);
        return ResponseEntity.created(URI.create("/api/v1/rider/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<Page<RiderResponse>> pagingRider(
        @RequestParam final int page) {
        PageRequest pageRequest = PageRequest.of(page, 20);
        Page<RiderResponse> riderPage = riderService.findAll(pageRequest);
        return ResponseEntity.ok(riderPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RiderResponse> getRider(
        @PathVariable final Long id) {
        RiderResponse riderResponse = riderService.findResponseById(id);
        return ResponseEntity.ok(riderResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> userUpdate(
        @RequestBody
        @Valid final RiderUpdateRequest riderUpdateRequest,
        @PathVariable final Long id) {
        riderService.update(id, riderUpdateRequest);
        return ResponseEntity.created(URI.create("/api/v1/rider/" + id)).build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> changeIsDelivery(
        @RequestParam
        @NotNull final Boolean isDelivery,
        @PathVariable final Long id) {
        riderService.changeIsDelivery(id, isDelivery);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{riderId}/{areaId}")
    public ResponseEntity<Void> addArea(@PathVariable final Long areaId,
        @PathVariable final Long riderId) {
        riderService.addRiderAreaCode(riderId, areaId);
        return ResponseEntity.noContent().build();
    }
}