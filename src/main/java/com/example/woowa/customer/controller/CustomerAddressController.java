package com.example.woowa.customer.controller;

import com.example.woowa.customer.dto.CreateCustomerAddressDto;
import com.example.woowa.customer.dto.CustomerAddressDto;
import com.example.woowa.customer.dto.UpdateCustomerAddressDto;
import com.example.woowa.customer.service.CustomerAddressService;
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
@RequestMapping("/api/v1/customers/addresses")
public class CustomerAddressController {
  private CustomerAddressService customerAddressService;

  @PostMapping("/{id}")
  public CustomerAddressDto createCustomerAddress(@PathVariable Long id, @RequestBody @Valid CreateCustomerAddressDto createCustomerAddressDto) {
    return null;
  }

  @GetMapping("/{id}")
  public CustomerAddressDto readCustomerAddress(@PathVariable Long id) {
    return null;
  }

  @PutMapping("/{id}")
  public CustomerAddressDto updateCustomerAddress(@PathVariable Long id, @RequestBody @Valid UpdateCustomerAddressDto updateCustomerAddressDto) {
    return null;
  }

  @DeleteMapping("/{id}")
  public void deleteCustomerAddress(@PathVariable Long id) { return; }
}
