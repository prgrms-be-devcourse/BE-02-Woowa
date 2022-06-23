package com.example.woowa.customer.repository;

import com.example.woowa.customer.entity.Customer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByLoginId(String loginId);
}
