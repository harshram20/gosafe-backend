package com.gosafe.repository;

import com.gosafe.model.Journey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JourneyRepository extends JpaRepository<Journey, Long> {
    List<Journey> findByUserIdOrderByCreatedAtDesc(Long userId);
}
