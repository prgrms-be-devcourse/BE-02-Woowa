package com.example.woowa.delivery.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.delivery.repository.AreaCodeRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AreaCodeServiceTest {

    @InjectMocks
    AreaCodeService areaCodeService;

    @Mock
    AreaCodeRepository areaCodeRepository;

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
        AreaCode areaCode = new AreaCode("1", "서울특별시 종로구", false);
        given(areaCodeRepository.findByDefaultAddress(any())).willReturn(Optional.of(areaCode));
        AreaCode retrieveAreaCode = areaCodeService.findByAddress(areaCode.getDefaultAddress());

        assertThat(retrieveAreaCode.getCode()).isEqualTo(areaCode.getCode());
        assertThat(retrieveAreaCode.getDefaultAddress()).isEqualTo(areaCode.getDefaultAddress());
    }

    @Test
    @DisplayName("법정동 코드를 통해 AreaCode를 찾을 수 있다.")
    public void findByCode() {
        String code = "1";
        AreaCode areaCode = new AreaCode(code, "서울특별시 종로구", false);
        given(areaCodeRepository.findByCode(any())).willReturn(Optional.of(areaCode));

        AreaCode retrieveAreaCode = areaCodeService.findByCode(code);
        assertThat(retrieveAreaCode.getCode()).isEqualTo(code);
        assertThat(retrieveAreaCode.getDefaultAddress()).isEqualTo("서울특별시 종로구");
    }


}