package com.example.woowa.customer.customer.controller;

import com.example.woowa.customer.customer.dto.UpdateCustomerAddressDto;
import com.example.woowa.customer.customer.dto.CreateCustomerAddressDto;
import com.example.woowa.customer.customer.dto.CustomerAddressDto;
import com.example.woowa.customer.customer.service.CustomerAddressService;
import com.example.woowa.customer.customer.service.CustomerService;

import java.util.List;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers/addresses")
public class CustomerAddressController {
    private CustomerAddressService customerAddressService;
    private CustomerService customerService;

    @PostMapping("/{loginId}")
    public CustomerAddressDto createCustomerAddress(@PathVariable String loginId, @RequestBody @Valid CreateCustomerAddressDto createCustomerAddressDto) {
        return customerAddressService.createCustomerAddress(loginId, createCustomerAddressDto);
    }

    @GetMapping("/{loginId}")
    public List<CustomerAddressDto> readCustomerAddress(@PathVariable String loginId) {
        return customerAddressService.findCustomerAddress(loginId);
    }

    @PutMapping("/{loginId}/{addressId}")
    public CustomerAddressDto updateCustomerAddress(@PathVariable String loginId, @PathVariable Long addressId, @RequestBody @Valid UpdateCustomerAddressDto updateCustomerAddressDto) {
        return customerAddressService.updateCustomerAddress(loginId, addressId, updateCustomerAddressDto);
    }

    @DeleteMapping("/{loginId}/{addressId}")
    public String deleteCustomerAddress(@PathVariable String loginId, @PathVariable Long addressId) {
        customerAddressService.deleteCustomerAddress(loginId, addressId);
        return "delete id - " + addressId;
    }
}
