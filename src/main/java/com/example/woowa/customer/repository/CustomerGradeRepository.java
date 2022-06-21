package com.example.woowa.customer.repository;

import com.example.woowa.customer.entity.CustomerGrade;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerGradeRepository extends JpaRepository<CustomerGrade, Integer> {
  Optional<CustomerGrade> findFirstByOrderByOrderCount();
  Optional<CustomerGrade> findByGrade(String grade);
  Optional<CustomerGrade> findFirstByOrderCountLessThanEqualOrderByOrderCountDesc(int orderCount);
}
