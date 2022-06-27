package com.example.woowa.delivery.service;

import com.example.woowa.common.util.FileUtil;
import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.delivery.repository.AreaCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AreaCodeService {

    private final AreaCodeRepository areaCodeRepository;

    @Transactional
    public void deleteAll() {
        areaCodeRepository.deleteAll();
        return;
    }
    public List<AreaCode> findAll() {
        return areaCodeRepository.findAll();
    }

    public AreaCode findByAddress(String defaultAddress) {
        return areaCodeRepository.findByDefaultAddress(defaultAddress).orElseThrow(() -> new RuntimeException("없는 행정구역 입니다."));
    }

    @Transactional
    public void init() {
        List<AreaCode> areaCodeList = FileUtil.parseAreaCodeList();
        areaCodeRepository.saveAll(areaCodeList);
    }

    public AreaCode findByCode(String code) {
        return areaCodeRepository.findByCode(code).orElseThrow(() -> new RuntimeException("없는 행정구역 입니다."));
    }
}
