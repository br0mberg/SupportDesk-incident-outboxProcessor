package ru.brombin.assistant_ai_outbox_processor.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.brombin.assistant_ai_outbox_processor.entity.IncidentOutboxEvent;
import ru.brombin.assistant_ai_outbox_processor.repository.OutboxEventRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OutboxProcessor {

    OutboxEventRepository outboxRepository;
    RabbitMqOutboxService rabbitMqOutboxService;

    // каждые 5 секунд
    @Scheduled(fixedDelay = 5000)
    public void processOutboxEvents() {
        List<IncidentOutboxEvent> events = outboxRepository.findAllByProcessedFalseOrderByCreatedAtAsc();
        for (IncidentOutboxEvent event : events) {
            rabbitMqOutboxService.processEvent(event);
        }
    }
}

