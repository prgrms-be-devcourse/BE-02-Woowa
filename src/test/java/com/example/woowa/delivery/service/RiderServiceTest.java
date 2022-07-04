package com.example.woowa.delivery.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.woowa.TestInitUtil;
import com.example.woowa.delivery.dto.RiderCreateRequest;
import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.delivery.entity.Rider;
import com.example.woowa.delivery.mapper.RiderMapper;
import com.example.woowa.delivery.repository.RiderRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@Transactional
class RiderServiceTest {

    @InjectMocks
    RiderService riderService;

    @Mock
    RiderMapper riderMapper = Mappers.getMapper(RiderMapper.class);
    @Mock
    RiderRepository riderRepository;
    @Mock
    AreaCodeService areaCodeService;

    @Test
    @DisplayName("중복된 로그인 id는 저장할 수 없다.")
    public void failSave() {
        RiderCreateRequest riderCreateRequest = new RiderCreateRequest("id", "password", "name",
            "폰");

        given(riderRepository.existsByLoginId(any())).willReturn(true);

        assertThrows(RuntimeException.class, () -> riderService.save(riderCreateRequest));
    }

    @Test
    @DisplayName("없는 id는 조회할 수 없다.")
    public void failFind() {
        given(riderRepository.findById(any())).willReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> riderService.findResponseById(1L));
    }

    @Test
    @DisplayName("라이더는 배달 지역을 추가 할 수 있다.")
    public void addRiderAreaCode() {
        Rider rider = TestInitUtil.initRider();
        AreaCode areaCode = new AreaCode("0000", "서울특별시 관악구 신대방동", true);
        given(riderRepository.findById(any())).willReturn(Optional.of(rider));
        given(areaCodeService.findEntityById(any())).willReturn(areaCode);

        riderService.addRiderAreaCode(1L, 1L);

        assertThat(rider.getRiderAreaCodeList().size()).isEqualTo(3);
        assertThat(areaCode.getRiderAreaCodeList().size()).isEqualTo(1);
        assertThat(rider.getRiderAreaCodeList().get(2)).isEqualTo(
            areaCode.getRiderAreaCodeList().get(0));
    }
}