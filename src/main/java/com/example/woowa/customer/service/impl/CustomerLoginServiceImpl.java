package com.example.woowa.customer.service.impl;

import com.example.woowa.customer.dto.CustomerLoginDto;
import com.example.woowa.customer.entity.Customer;
import com.example.woowa.customer.repository.CustomerRepository;
import com.example.woowa.customer.service.CustomerLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerLoginServiceImpl implements CustomerLoginService {
  private CustomerRepository customerRepository;

  @Override
  public Customer login(CustomerLoginDto customerLoginDto) {
    Customer customer = customerRepository.findByLoginId(customerLoginDto.getLoginId()).orElseThrow(()-> new RuntimeException("login id not existed"));
    if (!customerLoginDto.getLoginPassword().equals(customer.getLoginPassword())) {
      throw new RuntimeException("login id and password not matched");
    }
    return customer;
  }
}
