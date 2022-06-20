package com.example.woowa.customer.service.impl;

import com.example.woowa.customer.repository.CustomerRepository;
import com.example.woowa.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
  private CustomerRepository customerRepository;
}
