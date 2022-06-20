package com.example.woowa.customer.impl;

import com.example.woowa.customer.repository.CustomerGradeRepository;
import com.example.woowa.customer.service.CustomerGradeService;
import org.springframework.stereotype.Service;

@Service
public class CustomerGradeServiceImpl implements CustomerGradeService {
  private CustomerGradeRepository customerGradeRepository;

  public CustomerGradeServiceImpl(CustomerGradeRepository customerGradeRepository) {
    this.customerGradeRepository = customerGradeRepository;
  }
}
