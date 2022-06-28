package com.example.woowa.customer.customer.controller;

import com.example.woowa.customer.customer.dto.CustomerUpdateRequest;
import com.example.woowa.customer.customer.dto.CustomerCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerFindResponse;
import com.example.woowa.customer.customer.service.CustomerGradeService;
import com.example.woowa.customer.customer.service.CustomerService;

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
    private final CustomerService customerService;

    @PostMapping
    public CustomerFindResponse createCustomer(@RequestBody @Valid CustomerCreateRequest customerCreateRequest) {
        return customerService.createCustomer(customerCreateRequest);
    }

    @GetMapping("/{loginId}")
    public CustomerFindResponse readCustomer(@PathVariable String loginId) {
        return customerService.findCustomer(loginId);
    }

    @PutMapping("/{loginId}")
    public CustomerFindResponse updateCustomer(@RequestBody @Valid CustomerUpdateRequest customerUpdateRequest, @PathVariable String loginId) {
        return customerService.updateCustomer(loginId, customerUpdateRequest);
    }

    @PutMapping("/firstday/{loginId}")
    public CustomerFindResponse updateCustomerOnFirstDay(@PathVariable String loginId) {
        return customerService.updateCustomerStatusOnFirstDay(loginId);
    }

    @DeleteMapping("/{loginId}")
    public String deleteCustomer(@PathVariable String loginId) {
        customerService.deleteCustomer(loginId);
        return "delete id - " + loginId;
    }
}
