package com.example.woowa.customer.customer.controller;

import com.example.woowa.customer.customer.dto.CreateCustomerGradeDto;
import com.example.woowa.customer.customer.dto.CustomerGradeDto;
import com.example.woowa.customer.customer.dto.UpdateCustomerGradeDto;
import com.example.woowa.customer.customer.service.CustomerGradeService;

import javax.validation.Valid;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/customers/grades")
public class CustomerGradeController {
    private CustomerGradeService customerGradeService;

    @PostMapping
    public CustomerGradeDto createCustomerGrade(@RequestBody @Valid CreateCustomerGradeDto createCustomerGradeDto) {
        return customerGradeService.createCustomerGrade(createCustomerGradeDto);
    }

    @GetMapping("/{id}")
    public CustomerGradeDto readCustomerGrade(@PathVariable Long id) {
        return customerGradeService.findCustomerGrade(id);
    }

    @PutMapping("/{id}")
    public CustomerGradeDto updateCustomerGrade(@PathVariable Long id, @RequestBody @Valid UpdateCustomerGradeDto updateCustomerGradeDto) {
        return customerGradeService.updateCustomerGrade(id, updateCustomerGradeDto);
    }

    @DeleteMapping("/{id}")
    public String deleteCustomerGrade(@PathVariable Long id) {
        customerGradeService.deleteCustomerGrade(id);
        return "delete id - " + id;
    }
}
