package com.abdev.taskaudit.repository;

import com.abdev.taskaudit.entity.TaskAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskAuditRepository extends JpaRepository<TaskAudit, String> {
    boolean existsByEventId(String eventId);
}
