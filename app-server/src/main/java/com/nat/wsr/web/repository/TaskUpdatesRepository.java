package com.nat.wsr.web.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nat.wsr.model.KeyUpdates;
import com.nat.wsr.model.TaskUpdates;

public interface TaskUpdatesRepository extends JpaRepository<TaskUpdates, Long> {

    Optional<TaskUpdates> findById(Long taskUpdateId);
    List<TaskUpdates> findByReportsId(Long reportId);

}
