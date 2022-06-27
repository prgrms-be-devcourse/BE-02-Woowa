package com.example.woowa.delivery.service;

import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.delivery.entity.Rider;
import com.example.woowa.delivery.entity.RiderAreaCode;
import com.example.woowa.delivery.repository.RiderAreaCodeRepository;
import com.example.woowa.delivery.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RiderService {

    private final RiderRepository riderRepository;

    private final RiderAreaCodeRepository riderAreaCodeRepository;

    @Transactional
    public void deleteAll() {
        riderRepository.deleteAll();
    }

    @Transactional
    public void save(Rider rider) {
        riderRepository.save(rider);
    }

    public Rider findById(Long id) {
        return riderRepository.findById(id).orElseThrow(() -> new RuntimeException("없는 배달기사 입니다."));
    }

    public List<Rider> findAll() {
        return riderRepository.findAll();
    }

    @Transactional
    public void addRiderAreaCode(Rider rider, AreaCode areaCode) {
        Rider retrieveRider = findById(rider.getId());
        RiderAreaCode riderAreaCode = new RiderAreaCode(rider, areaCode);
        rider.addRiderAreaCode(riderAreaCode);
    }
}
