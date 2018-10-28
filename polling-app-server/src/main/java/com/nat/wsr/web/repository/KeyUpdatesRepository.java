package com.nat.wsr.web.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nat.wsr.model.KeyUpdates;

public interface KeyUpdatesRepository extends JpaRepository<KeyUpdates, Long> {

    Optional<KeyUpdates> findById(Long keyUpdateId);
    List<KeyUpdates> findByReportsId(Long reportId);

}
