package com.abdev.taskaudit.consumer;

import com.abdev.taskaudit.entity.TaskAudit;
import com.abdev.taskaudit.event.TaskEvent;
import com.abdev.taskaudit.repository.TaskAuditRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TaskEventConsumer {

    private final TaskAuditRepository taskAuditRepository;

    private static final Logger log =
            LoggerFactory.getLogger(TaskEventConsumer.class);

    public TaskEventConsumer(TaskAuditRepository repository) {
        this.taskAuditRepository = repository;
    }

    @KafkaListener(topics = "task-events", groupId = "audit-group-v2")
    public void consume(TaskEvent event) {
        log.info("Consumed event: {}", event);

        TaskAudit audit = new TaskAudit();
        audit.setEventId(event.getEventId());
        audit.setTaskId(event.getTaskId());
        audit.setEventType(event.getEventType());
        audit.setTimestamp(event.getTimestamp());

        taskAuditRepository.save(audit);
        log.info("Saved audit for taskId={}", event.getTaskId());
    }
}
