package com.example.woowa.delivery.service.impl;

import com.example.woowa.delivery.repository.RiderRepository;
import com.example.woowa.delivery.service.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {

    private final RiderRepository riderRepository;
}
