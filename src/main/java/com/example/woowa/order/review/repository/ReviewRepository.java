package com.example.woowa.order.review.repository;

import com.example.woowa.order.review.entity.Review;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  Optional<Review> findById(Long aLong);
}
