package com.abdev.taskaudit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class TaskAudit {

    @Id
    private String eventId;

    private Long taskId;

    private String eventType;

    private LocalDateTime timestamp;
}
