package com.example.woowa.customer.controller;

import com.example.woowa.customer.dto.CreateCustomerDto;
import com.example.woowa.customer.dto.CustomerDto;
import com.example.woowa.customer.dto.UpdateCustomerDto;
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

  @PostMapping
  public CustomerDto createCustomer(@RequestBody @Valid CreateCustomerDto createCustomerDto) {
    return customerService.createCustomer(createCustomerDto);
  }

  @GetMapping("/{id}")
  public CustomerDto readCustomer(@PathVariable Long id) {
    return customerService.readCustomer(id);
  }

  @PutMapping
  public CustomerDto updateCustomer(@RequestBody @Valid UpdateCustomerDto updateCustomerDto) {
    return customerService.updateCustomer(updateCustomerDto);
  }

  @DeleteMapping("/{id}")
  public String deleteCustomer(@PathVariable Long id) {
    customerService.deleteCustomer(id);
    return "delete id - " + id;
  }
}
