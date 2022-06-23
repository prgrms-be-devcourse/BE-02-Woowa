package com.example.woowa.customer.controller;

import com.example.woowa.customer.dto.CreateCustomerDto;
import com.example.woowa.customer.dto.CustomerDto;
import com.example.woowa.customer.dto.UpdateCustomerDto;
import com.example.woowa.customer.entity.CustomerGrade;
import com.example.woowa.customer.service.CustomerGradeService;
import com.example.woowa.customer.service.CustomerService;
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
@RequestMapping("/api/v1/customers")
public class CustomerController {
  private CustomerService customerService;
  private CustomerGradeService customerGradeService;

  @PostMapping()
  public CustomerDto createCustomer(@RequestBody @Valid CreateCustomerDto createCustomerDto) {
    CustomerGrade defaultGrade = customerGradeService.findDefaultCustomerGrade();
    return customerService.createCustomer(defaultGrade, createCustomerDto);
  }

  @GetMapping("/{loginId}")
  public CustomerDto readCustomer(@PathVariable String loginId) {
    return customerService.readCustomer(loginId);
  }

  @PutMapping("/{loginId}")
  public CustomerDto updateCustomer(@PathVariable String loginId, @RequestBody @Valid UpdateCustomerDto updateCustomerDto) {
    return customerService.updateCustomer(loginId, updateCustomerDto);
  }

  @DeleteMapping("/{loginId}")
  public String deleteCustomer(@PathVariable String loginId) {
    customerService.deleteCustomer(loginId);
    return "delete id - " + loginId;
  }
}
