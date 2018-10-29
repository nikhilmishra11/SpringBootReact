package com.nat.wsr.web.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nat.wsr.model.Reports;

@Repository
public interface ReportsRepository extends JpaRepository<Reports, Long> {

    Optional<Reports> findById(Long reportId);

    Optional<Reports> findByIsActive(Integer isActive);
}
