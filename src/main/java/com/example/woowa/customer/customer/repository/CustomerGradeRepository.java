package com.example.woowa.customer.customer.repository;

import com.example.woowa.customer.customer.entity.CustomerGrade;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerGradeRepository extends JpaRepository<CustomerGrade, Integer> {
    Optional<CustomerGrade> findFirstByOrderByOrderCount();

    Optional<CustomerGrade> findFirstByOrderCountLessThanEqualOrderByOrderCountDesc(int orderCount);

    Optional<CustomerGrade> findById(Long id);
}
