package com.example.woowa.rider.service.impl;

import com.example.woowa.rider.repository.RiderRepository;
import com.example.woowa.rider.service.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {

    private final RiderRepository riderRepository;
}
