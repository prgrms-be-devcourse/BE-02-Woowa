package com.example.woowa.delivery.service;

import com.example.woowa.delivery.repository.RiderAreaCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiderAreaCodeService {

    private final RiderAreaCodeRepository riderAreaCodeRepository;
}
