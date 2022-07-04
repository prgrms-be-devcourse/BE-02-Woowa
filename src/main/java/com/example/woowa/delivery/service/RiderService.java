package com.example.woowa.delivery.service;

import com.example.woowa.common.exception.ErrorMessage;
import com.example.woowa.delivery.dto.RiderCreateRequest;
import com.example.woowa.delivery.dto.RiderResponse;
import com.example.woowa.delivery.dto.RiderUpdateRequest;
import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.delivery.entity.Rider;
import com.example.woowa.delivery.entity.RiderAreaCode;
import com.example.woowa.delivery.mapper.RiderMapper;
import com.example.woowa.delivery.repository.RiderRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RiderService {

    private final RiderRepository riderRepository;

    private final RiderMapper riderMapper;

    private final AreaCodeService areaCodeService;

    @Transactional
    public void deleteAll() {
        riderRepository.deleteAll();
    }

    @Transactional
    public Long save(RiderCreateRequest riderCreateRequest) {
        Rider rider = riderMapper.toRider(riderCreateRequest);
        boolean isId = riderRepository.existsByLoginId(riderCreateRequest.getLoginId());
        if (isId) {
            throw new RuntimeException(ErrorMessage.DUPLICATE_LOGIN_ID.getMessage());
        }

        riderRepository.save(rider);
        return rider.getId();
    }

    @Transactional
    public void update(Long id, RiderUpdateRequest riderUpdateRequest) {
        Rider rider = findEntityById(id);
        rider.update(riderUpdateRequest.getName(), riderUpdateRequest.getPhoneNumber());
    }

    @Transactional
    public void changeIsDelivery(Long id, Boolean isDelivery) {
        findEntityById(id).changeIsDelivery(isDelivery);
    }

    public RiderResponse findResponseById(Long id) {
        return riderMapper.toResponse(findEntityById(id));
    }

    public Rider findEntityById(Long id) {
        return riderRepository.findById(id).orElseThrow(() -> new RuntimeException("없는 배달기사 입니다."));
    }

    public Page<RiderResponse> findAll(PageRequest pageRequest) {
        return riderRepository.findAllBy(pageRequest).map(riderMapper::toResponse);
    }

    @Transactional
    public void addRiderAreaCode(Long riderId, Long areaCodeId) {
        Rider rider = findEntityById(riderId);
        AreaCode areaCode = areaCodeService.findEntityById(areaCodeId);
        RiderAreaCode riderAreaCode = new RiderAreaCode(rider, areaCode);
    }

    @Transactional
    public void removeRiderAreaCode(Long riderId, Long areaCodeId) {
        Rider rider = findEntityById(riderId);
        List<RiderAreaCode> riderAreaCodeList = rider.getRiderAreaCodeList();
        riderAreaCodeList.stream()
            .filter(riderAreaCode -> riderAreaCode.getAreaCode().getId() == areaCodeId).findFirst()
            .ifPresent(riderAreaCodeList::remove);
    }
}
