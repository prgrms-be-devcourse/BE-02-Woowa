package com.example.woowa.customer.impl;

import com.example.woowa.customer.repository.CustomerRepository;
import com.example.woowa.customer.service.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
  private CustomerRepository customerRepository;

  public CustomerServiceImpl(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }
}
