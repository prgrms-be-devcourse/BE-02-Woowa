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

    public AreaCode findByAddress(String defaultAddress) {
        AreaCode areaCode = areaCodeRepository.findByDefaultAddress(defaultAddress).orElseThrow(() -> new RuntimeException("없는 행정구역 입니다."));
        return areaCode;
    }

    public void init() {
        List<AreaCode> areaCodeList = FileUtil.parseAreaCodeList();
        areaCodeRepository.saveAll(areaCodeList);
    }
}
