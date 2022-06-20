package com.example.woowa.owner.service.impl;

import com.example.woowa.owner.repository.OwnerRepository;
import com.example.woowa.owner.service.OwnerService;
import org.springframework.stereotype.Service;

@Service
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerServiceImpl(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

}
