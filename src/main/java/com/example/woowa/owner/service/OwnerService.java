package com.example.woowa.owner.service;

import com.example.woowa.owner.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

}
