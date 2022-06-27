package com.example.woowa.delivery.service;

import com.example.woowa.delivery.entity.AreaCode;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AreaCodeServiceTest {

    @Autowired
    AreaCodeService areaCodeService;

    @Test
    @DisplayName("AreaCode의 기본 데이터를 초기화할 수 있다.")
    @BeforeEach
    public void init() {
        areaCodeService.deleteAll();
        areaCodeService.init();
    }

    @Test
    @DisplayName("defaultAddress를 통해 AreaCode를 찾을 수 있다.")
    public void findByDefaultAddress() {
        String address = "서울특별시 종로구";
        AreaCode areaCode = areaCodeService.findByAddress(address);
        assertThat(areaCode.getCode()).isEqualTo("1111000000");
        assertThat(areaCode.getDefaultAddress()).isEqualTo(address);
    }

    @Test
    @DisplayName("법정동 코드를 통해 AreaCode를 찾을 수 있다.")
    public void findByCode() {
        String code = "1111000000";
        AreaCode areaCode = areaCodeService.findByCode(code);
        assertThat(areaCode.getCode()).isEqualTo(code);
        assertThat(areaCode.getDefaultAddress()).isEqualTo("서울특별시 종로구");
    }


}