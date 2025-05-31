package ru.brombin.assistant_ai_outbox_processor.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.brombin.assistant_ai_outbox_processor.entity.IncidentOutboxEvent;
import ru.brombin.assistant_ai_outbox_processor.repository.OutboxEventRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OutboxProcessor {

    OutboxEventRepository outboxRepository;
    RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 5000) // каждые 5 секунд
    public void processOutboxEvents() {
        List<IncidentOutboxEvent> events = outboxRepository.findAllByProcessedFalseOrderByCreatedAtAsc();

        for (IncidentOutboxEvent event : events) {
            try {
                // отправка события
                rabbitTemplate.convertAndSend("incident.exchange",
                        "incident.created", event.getPayload());

                // Помечаем как обработанное
                event.setProcessed(true);
                outboxRepository.save(event);

                log.info("Outbox event sent: {}", event.getId());

            } catch (Exception e) {
                log.error("Failed to process outbox event: {}", event.getId(), e);
                // добавить retry или сохранить причину
            }
        }
    }
}

