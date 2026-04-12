package com.abdev.taskaudit.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class TaskAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String eventId;

    private Long taskId;

    private String eventType;

    private LocalDateTime timestamp;
}
