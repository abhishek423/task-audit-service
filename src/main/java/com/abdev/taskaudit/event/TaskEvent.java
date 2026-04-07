package com.abdev.taskaudit.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskEvent {
    private String eventId;
    private String eventType;
    private Long taskId;
    private LocalDateTime timestamp;
}
