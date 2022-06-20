package com.example.woowa.customer.service.impl;

import com.example.woowa.customer.repository.CustomerAddressRepository;
import com.example.woowa.customer.service.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerAddressServiceImpl implements CustomerService {
  private CustomerAddressRepository customerAddressRepository;

  public CustomerAddressServiceImpl(CustomerAddressRepository customerAddressRepository) {
    this.customerAddressRepository = customerAddressRepository;
  }
}
