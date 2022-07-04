package com.example.woowa.customer.customer.controller;

import com.example.woowa.customer.customer.dto.CustomerAddressUpdateRequest;
import com.example.woowa.customer.customer.dto.CustomerAddressCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerAddressFindResponse;
import com.example.woowa.customer.customer.service.CustomerAddressService;

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
    private final CustomerAddressService customerAddressService;

    @PostMapping("/{loginId}")
    public CustomerAddressFindResponse createCustomerAddress(@PathVariable String loginId, @RequestBody @Valid CustomerAddressCreateRequest customerAddressCreateRequest) {
        return customerAddressService.createCustomerAddress(loginId, customerAddressCreateRequest);
    }

    @GetMapping("/{loginId}")
    public List<CustomerAddressFindResponse> readCustomerAddresses(@PathVariable String loginId) {
        return customerAddressService.findCustomerAddresses(loginId);
    }

    @PutMapping("/{id}")
    public CustomerAddressFindResponse updateCustomerAddress(@PathVariable Long id, @RequestBody @Valid CustomerAddressUpdateRequest customerAddressUpdateRequest) {
        return customerAddressService.updateCustomerAddress(id, customerAddressUpdateRequest);
    }

    @DeleteMapping("/{id}")
    public String deleteCustomerAddress(@PathVariable Long id) {
        customerAddressService.deleteCustomerAddress(id);
        return "delete id - " + id;
    }
}
