package com.example.woowa.delivery.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.woowa.TestInitUtil;
import com.example.woowa.delivery.dto.RiderCreateRequest;
import com.example.woowa.delivery.dto.RiderResponse;
import com.example.woowa.delivery.entity.Rider;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class RiderMapperTest {

    RiderMapper riderMapper = Mappers.getMapper(RiderMapper.class);

    @Test
    @DisplayName("MemberCreateRequest로 Rider로 변환할 수 있다.")
    void toEntity() {
        RiderCreateRequest riderCreateRequest = new RiderCreateRequest("id", "password", "name",
            "폰");
        Rider rider = riderMapper.toRider(riderCreateRequest);

        assertThat(rider.getName()).isEqualTo(riderCreateRequest.getName());
        assertThat(rider.getLoginId()).isEqualTo(riderCreateRequest.getLoginId());
        assertThat(rider.getPassword()).isEqualTo(riderCreateRequest.getPassword());
        assertThat(rider.getPhoneNumber()).isEqualTo(riderCreateRequest.getPhoneNumber());
    }

    @Test
    @DisplayName("Rider로 RiderResponse로 파싱할 수 있다.")
    void toResponse() {
        Rider rider = TestInitUtil.initRider();
        rider.changeUpdatedAt(LocalDateTime.now());
        RiderResponse riderResponse = riderMapper.toResponse(rider);
        int size = riderResponse.getRiderAreaList().size();

        assertThat(riderResponse.getId()).isEqualTo(rider.getId());
        assertThat(riderResponse.getIsDelivery()).isEqualTo(rider.getIsDelivery());

        assertThat(riderResponse.getLoginId()).isEqualTo(rider.getLoginId());
        assertThat(riderResponse.getPassword()).isEqualTo(rider.getPassword());
        assertThat(riderResponse.getName()).isEqualTo(rider.getName());
        assertThat(riderResponse.getPhoneNumber()).isEqualTo(rider.getPhoneNumber());

        for (int i = 0; i < size; i++) {
            assertThat(riderResponse.getRiderAreaList().get(i)).isEqualTo(
                rider.getRiderAreaCodeList().get(i).getAreaCode().getDefaultAddress());
        }
    }
}