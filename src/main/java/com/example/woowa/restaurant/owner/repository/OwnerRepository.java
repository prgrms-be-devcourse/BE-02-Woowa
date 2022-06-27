package com.example.woowa.restaurant.owner.repository;

import com.example.woowa.restaurant.owner.entity.Owner;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

    Optional<Owner> findOwnerByLoginId(String loginId);

}
