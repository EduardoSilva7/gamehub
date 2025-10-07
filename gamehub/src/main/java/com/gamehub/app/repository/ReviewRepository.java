package com.gamehub.app.repository;

import com.gamehub.app.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    java.util.List<Review> findByGameId(Long gameId);
}
