package com.example.woowa.delivery.service;

import com.example.woowa.delivery.entity.AreaCode;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AreaCodeServiceTest {

    @Autowired
    AreaCodeService areaCodeService;

    @Test
    @DisplayName("AreaCode 파일을 읽어와서 초기화 할 수 있다.")
    @BeforeEach
    public void init() {
        areaCodeService.init();
    }

    @Test
    @DisplayName("defaultAddress를 통해 AreaCode를 조회할 수 있다.")
    public void findByDefaultAddress() {
        AreaCode areaCode = areaCodeService.findByAddress("서울특별시 종로구");
        assertThat(areaCode.getCode()).isEqualTo(1111000000);
        assertThat(areaCode.getDefaultAddress()).isEqualTo("서울특별시 종로구");
    }


}