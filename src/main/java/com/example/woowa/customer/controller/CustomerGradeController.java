package com.example.woowa.customer.controller;

import com.example.woowa.customer.dto.CreateCustomerGradeDto;
import com.example.woowa.customer.dto.CustomerDto;
import com.example.woowa.customer.dto.CustomerGradeDto;
import com.example.woowa.customer.service.CustomerGradeService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers/grades")
public class CustomerGradeController {
  private CustomerGradeService customerGradeService;

  @PostMapping
  public CustomerGradeDto createCustomerGrade(@RequestBody @Valid CreateCustomerGradeDto createCustomerGradeDto) {
    return null;
  }

  @GetMapping("/{id}")
  public CustomerGradeDto readCustomerGrade(@PathVariable Long id) {
    return null;
  }

  @PutMapping
  public CustomerDto updateCustomerGrade(@RequestBody @Valid CreateCustomerGradeDto createCustomerGradeDto) {
    return null;
  }

  @DeleteMapping("/{id}")
  public void deleteCustomerGrade(@PathVariable Long id) {
    return;
  }
}
