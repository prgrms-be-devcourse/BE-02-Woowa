package com.example.woowa.customer.service;

import com.example.woowa.customer.dto.CustomerLoginDto;
import com.example.woowa.customer.entity.Customer;

public interface CustomerLoginService {
  Customer login(CustomerLoginDto customerLoginDto);
}
