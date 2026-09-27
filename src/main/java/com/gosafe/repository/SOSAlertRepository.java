package com.gosafe.repository;

import com.gosafe.model.SOSAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SOSAlertRepository extends JpaRepository<SOSAlert, Long> {
    List<SOSAlert> findByUserIdOrderByCreatedAtDesc(Long userId);
}
