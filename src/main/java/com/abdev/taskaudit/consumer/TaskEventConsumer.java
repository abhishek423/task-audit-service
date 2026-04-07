package com.abdev.taskaudit.consumer;

import com.abdev.taskaudit.entity.TaskAudit;
import com.abdev.taskaudit.event.TaskEvent;
import com.abdev.taskaudit.repository.TaskAuditRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TaskEventConsumer {

    private final TaskAuditRepository taskAuditRepository;

    public TaskEventConsumer(TaskAuditRepository repository) {
        this.taskAuditRepository = repository;
    }

    @KafkaListener(topics = "task-events", groupId = "audit-group")
    public void consume(TaskEvent event) {

        TaskAudit audit = new TaskAudit();
        audit.setEventId(event.getEventId());
        audit.setTaskId(event.getTaskId());
        audit.setEventType(event.getEventType());
        audit.setTimestamp(event.getTimestamp());

        taskAuditRepository.save(audit);

        System.out.println("Consumed and saved event: " + event);
    }
}
