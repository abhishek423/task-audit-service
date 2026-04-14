package com.abdev.taskaudit.consumer;

import com.abdev.taskaudit.entity.TaskAudit;
import com.abdev.taskaudit.event.TaskCreatedEventData;
import com.abdev.taskaudit.event.TaskEvent;
import com.abdev.taskaudit.event.TaskEventType;
import com.abdev.taskaudit.repository.TaskAuditRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TaskEventConsumer {

    private final TaskAuditRepository taskAuditRepository;
    private final ObjectMapper objectMapper;
    private static final Logger log =
            LoggerFactory.getLogger(TaskEventConsumer.class);

    public TaskEventConsumer(TaskAuditRepository repository, ObjectMapper objectMapper) {
        this.taskAuditRepository = repository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "task-events", groupId = "audit-group-v2")
    public void consume(TaskEvent event) {
        log.info("Consumed event: {}", event);
        try {

            // Route by event type
            if (event.getEventType() == TaskEventType.TASK_CREATED) {

                // Handle versioning
                switch (event.getEventVersion()) {

                    case "v1":
                        handleTaskCreatedV1(event);
                        break;

                    default:
                        log.warn("Unsupported event version: {}", event.getEventVersion());
                }
            }

        } catch (DataIntegrityViolationException e) {

            // Duplicate handled
            log.warn("Duplicate event detected, skipping eventId={}", event.getEventId());
        } catch (Exception e) {

            // Failure; throw to trigger retry + DLQ
            log.error("Error processing event: {}", event, e);
            throw e;
        }
    }

    private void handleTaskCreatedV1(TaskEvent event) {

        // Convert generic payload to typed object
        TaskCreatedEventData data =
                objectMapper.convertValue(event.getData(), TaskCreatedEventData.class);

        TaskAudit audit = new TaskAudit();
        audit.setEventId(event.getEventId());
        audit.setTaskId(data.getTaskId());
        audit.setEventType(event.getEventType().name());
        audit.setTimestamp(event.getTimestamp());

        taskAuditRepository.save(audit);

        log.info("Saved audit for taskId={}", data.getTaskId());
    }
}
