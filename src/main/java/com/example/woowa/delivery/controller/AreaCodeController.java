package com.example.woowa.delivery.controller;

import com.example.woowa.delivery.service.AreaCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/areaCode")
@RequiredArgsConstructor
public class AreaCodeController {

    private final AreaCodeService areaCodeService;

    public ResponseEntity<Void> init() {
        areaCodeService.init();
        return ResponseEntity.noContent().build();
    }
}
