package com.example.woowa.delivery.repository;

import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.delivery.entity.DeliveryArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryAreaRepository extends JpaRepository<DeliveryArea, Long> {

}
