package com.example.woowa.admin.repository;

import com.example.woowa.admin.entity.Admin;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
  Optional<Admin> findByLoginId(String loginId);
}
