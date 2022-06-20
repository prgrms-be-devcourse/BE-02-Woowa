package com.example.woowa.owner.service.impl;

import com.example.woowa.owner.repository.OwnerRepository;
import com.example.woowa.owner.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;

}
