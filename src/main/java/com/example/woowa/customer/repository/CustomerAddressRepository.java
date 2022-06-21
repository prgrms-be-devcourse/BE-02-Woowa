package com.example.woowa.customer.repository;

import com.example.woowa.customer.entity.CustomerAddress;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {
  Optional<CustomerAddress> findByNickname(String nickname);
}
