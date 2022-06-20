package com.example.woowa.customer.service.impl;

import com.example.woowa.customer.repository.CustomerGradeRepository;
import com.example.woowa.customer.service.CustomerGradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerGradeServiceImpl implements CustomerGradeService {
  private CustomerGradeRepository customerGradeRepository;
}
